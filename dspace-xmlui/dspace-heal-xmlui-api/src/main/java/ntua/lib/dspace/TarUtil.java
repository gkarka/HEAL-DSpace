package ntua.lib.dspace;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */


import com.ice.tar.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.io.IOException;
//import com.sun.image.codec.jpeg.*;
import java.awt.image.*;
import java.awt.geom.*;

//import javax.media.jai.*;
//import java.awt.image.renderable.ParameterBlock;
//import com.sun.media.jai.codec.*;
import javax.imageio.*;
import java.awt.*;
import org.apache.log4j.Logger;

public class TarUtil {

//  private TarInputStream tarInputStream;
  private NtuaFastTarStream tarInputStream;
  private FastTarStream fastTarStream;
  private InputStream inputStream;
  //private FastTarStream tarInputStream;

  private static Logger log = Logger.getLogger(TarUtil.class);


  public TarUtil(InputStream is)
  {
    this.inputStream = is;
//    this.tarInputStream = new TarInputStream(is);
  }

//  private TarInputStream getTarInputStream() {
  private NtuaFastTarStream getTarInputStream() {
    if (this.tarInputStream == null)
      this.tarInputStream = new NtuaFastTarStream(this.inputStream);
//      this.tarInputStream = new TarInputStream(this.inputStream);

    return this.tarInputStream;
  }
  private FastTarStream getFastTarStream() {
    if (this.fastTarStream == null)
      this.fastTarStream = new FastTarStream(this.inputStream);
    return this.fastTarStream;
  }

  String[] images = null;
  public String[] listImages() throws IOException {
    if (images == null)
    {
      log.debug("Using FastTarStream...");

      ArrayList imageList = new ArrayList();

      TarEntry tarEntry = getFastTarStream().getNextEntry();
      while (tarEntry != null) {
        TarHeader header = tarEntry.getHeader();
        String fullName = header.getName();
        String fileName = fullName;
        if (fullName.lastIndexOf('/') != -1)
          fileName = fullName.substring(fullName.lastIndexOf('/') + 1);

        if (fileName.toLowerCase().endsWith(".jpg"))
          imageList.add(fileName);
          //imageList.add(fullName);

        tarEntry = fastTarStream.getNextEntry();
      }

      images = (String[])imageList.toArray(new String[] {});

      log.debug("End using FastTarStream...");
    }
    //tarInputStream.close();
    return images;
  }


  public void writeImage(String filename, OutputStream out) throws IOException
  {
    int idx = 0;


    boolean found = false;
    TarEntry tarEntry = getTarInputStream().getNextEntry();

    if ("0".equals(filename))
      found = true;
    else {
      int zerocount = 8 - filename.length();
      for (int i = 0; i < zerocount; i++)
        filename = "0" + filename;
      filename = filename + ".JPG";
    }

    while (tarEntry != null && !found)
    {
      idx++;
      if (tarEntry.getName().equalsIgnoreCase(filename))
        found = true;
      else
        tarEntry = tarInputStream.getNextEntry();
    }

    tarInputStream.copyEntryContents(out);
    tarInputStream.close();
  }

  public void writeThumbnail(int index, OutputStream out) throws IOException
  {
    int idx = 0;
    TarEntry tarEntry = getTarInputStream().getNextEntry();
    while (tarEntry != null && idx < index)
    {
      idx++;
      tarEntry = tarInputStream.getNextEntry();
    }

    PipedOutputStream pos = new PipedOutputStream();
    PipedInputStream pis = new PipedInputStream(pos);

    DataOutputStream dos = new DataOutputStream(pos);
    BufferedInputStream bis = new BufferedInputStream(pis);
    BufferedOutputStream bos = new BufferedOutputStream(pos);
//    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//    BufferedOutputStream bos = new BufferedOutputStream(outstream);

    tarInputStream.copyEntryContents(dos);
    tarInputStream.close();
    try
    {
      resize(bis, out, 100);

      /*
      ByteArrayInputStream instream = new ByteArrayInputStream(outstream.toByteArray());
      JPEGImageDecoder dec = JPEGCodec.createJPEGDecoder(instream);
      BufferedImage inImg = dec.decodeAsBufferedImage();

      // Scale down to half the size
      AffineTransform xform = AffineTransform.getScaleInstance(0.3,0.3);
      AffineTransformOp xformOp = new AffineTransformOp(xform,AffineTransformOp.TYPE_BILINEAR);
      BufferedImage outImg = new BufferedImage(150,112, BufferedImage.TYPE_INT_RGB);
      xformOp.filter(inImg, outImg);

      JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(out);
      enc.encode(outImg);
      instream.close();
*/
      bos.close();
    }
    catch (IOException ie) {}
  }


  public void extractImage() throws IOException
  {
    JFileChooser dlg = new JFileChooser("d:\\scan");

    if (dlg.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
    {
      File tarFile = dlg.getSelectedFile();
      InputStream is = new FileInputStream(tarFile);
      TarInputStream tis = new TarInputStream(is);

      TarEntry tarEntry = tis.getNextEntry();
      File jpgDir = null;
      if (tarEntry != null)
      {
        jpgDir = new File(tarFile.getParentFile().getPath() + File.separator +
                          "jpeg");
        if (!jpgDir.exists())
          jpgDir.mkdir();
      }

      while (tarEntry != null)
      {
        TarHeader header = tarEntry.getHeader();
        String fullName = header.getName();
        String fileName = fullName;
        if (fullName.lastIndexOf('/') != - 1)
          fileName = fullName.substring(fullName.lastIndexOf('/')+1);

        System.out.println("full name : " + fullName);
        System.out.println("file name : " + fileName);

        File file = new File(jpgDir, fileName);
        file.createNewFile();
        FileOutputStream fout = new FileOutputStream(file);
        tis.copyEntryContents(fout);
        //while (tis.read() != -1);

//        tarEntry

        tarEntry = tis.getNextEntry();
      }
    }
  }


  private void resize(InputStream original, OutputStream out, int maxSize) {
    try{
      BufferedImage img = ImageIO.read(original);
      ImageIcon ii = new ImageIcon(img);
      Image i = ii.getImage();
      Image resizedImage = null;

      int iWidth = i.getWidth(null);
      int iHeight = i.getHeight(null);

      if (iWidth > iHeight) {
        resizedImage = i.getScaledInstance(maxSize,(maxSize*iHeight)/iWidth,Image.SCALE_SMOOTH);
      } else {
        resizedImage = i.getScaledInstance((maxSize*iWidth)/iHeight,maxSize,Image.SCALE_SMOOTH);
      }

      // This code ensures that all the
      // pixels in the image are loaded.
      Image temp = new ImageIcon(resizedImage).getImage();

      // Create the buffered image.
      BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

      // Copy image to buffered image.
      Graphics g = bufferedImage.createGraphics();

      // Clear background and paint the image.
      g.setColor(Color.white);
      g.fillRect(0, 0, temp.getWidth(null),temp.getHeight(null));
      g.drawImage(temp, 0, 0, null);
      g.dispose();
      
      javax.imageio.ImageIO.write(bufferedImage, "jpeg", out);
      
      /* encodes image as a JPEG data stream */
      //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

      //com.sun.image.codec.jpeg.JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);

//      // writeParam = new JPEGImageWriteParam(null);
//      // writeParam.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
//      //writeParam.setProgressiveMode(JPEGImageWriteParam.MODE_DEFAULT);
      //param.setQuality(1.0f, true);
      //encoder.setJPEGEncodeParam(param);
      //encoder.encode(bufferedImage);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }


}
