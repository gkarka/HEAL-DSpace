package ntua.lib.dspace;

import com.ice.tar.FastTarStream;
import com.ice.tar.TarEntry;
import com.ice.tar.TarBuffer;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class NtuaFastTarStream extends FastTarStream {
  private TarEntry currEntry = null;
  private long entrySize;
  private long entryOffset;
  private byte[] readBuf;
  private InputStream inStream = null;

  public NtuaFastTarStream(InputStream in) {
    super(in);
    this.inStream = in;
  }

  public NtuaFastTarStream(InputStream in, int recordSize) {
    super(in, recordSize);
    this.inStream = in;
  }


  public TarEntry getNextEntry() throws IOException {
    this.currEntry = super.getNextEntry();
    this.entryOffset = 0;
    if (this.currEntry != null)
        this.entrySize = this.currEntry.getSize();
    return currEntry;
  }


  /**
   * Reads bytes from the current tar archive entry.
   *
   * This method is aware of the boundaries of the current
   * entry in the archive and will deal with them as if they
   * were this stream's start and EOF.
   *
   * @param buf The buffer into which to place bytes read.
   * @param offset The offset at which to place bytes read.
   * @param numToRead The number of bytes to read.
   * @return The number of bytes read, or -1 at EOF.
   */


  public int read( byte[] buf, int offset, int numToRead ) throws IOException {
    int totalRead = 0;

    if ( this.entryOffset >= this.entrySize )
      return -1;

    if ( (numToRead + this.entryOffset) > this.entrySize )
    {
      numToRead = (int) (this.entrySize - this.entryOffset);
    }

    if ( this.readBuf != null )
    {
      int sz = ( numToRead > this.readBuf.length )
          ? this.readBuf.length : numToRead;

      System.arraycopy( this.readBuf, 0, buf, offset, sz );

      if ( sz >= this.readBuf.length )
      {
        this.readBuf = null;
      }
      else
      {
        int newLen = this.readBuf.length - sz;
        byte[] newBuf = new byte[ newLen ];
        System.arraycopy( this.readBuf, sz, newBuf, 0, newLen );
        this.readBuf = newBuf;
      }

      totalRead += sz;
      numToRead -= sz;
      offset += sz;
    }

    for ( ; numToRead > 0 ; )
    {
      byte[] rec = this.readBlock();
      if ( rec == null )
      {
        // Unexpected EOF!
        throw new IOException
            ( "unexpected EOF with " + numToRead + " bytes unread" );
      }

      int sz = numToRead;
      int recLen = rec.length;

      if ( recLen > sz )
      {
        System.arraycopy( rec, 0, buf, offset, sz );
        this.readBuf = new byte[ recLen - sz ];
        System.arraycopy( rec, sz, this.readBuf, 0, recLen - sz );
      }
      else
      {
        sz = recLen;
        System.arraycopy( rec, 0, buf, offset, recLen );
      }

      totalRead += sz;
      numToRead -= sz;
      offset += sz;
    }

    this.entryOffset += totalRead;

    return totalRead;
  }


  private byte[] readBlock() throws IOException {
    int blockSize = TarBuffer.DEFAULT_BLKSIZE;
    byte[] blockBuffer = new byte[ blockSize ];

    int offset = 0;
    int bytesNeeded = blockSize;
    for ( ; bytesNeeded > 0 ; )
    {
      long numBytes =
          this.inStream.read
          ( blockBuffer, offset, bytesNeeded );

      if ( numBytes == -1 )
        break;

      offset += numBytes;
      bytesNeeded -= numBytes;
    }

    return blockBuffer;
  }


  public void copyEntryContents( OutputStream out ) throws IOException {
    byte[] buf = new byte[ 32 * 1024 ];

    for ( ; ; )
    {
      int numRead = this.read( buf, 0, buf.length );
      if ( numRead == -1 )
        break;
      out.write( buf, 0, numRead );
    }
  }

  public void close() {
    try {
      this.inStream.close();
    }
    catch (IOException ex) {
    }
  }
}
