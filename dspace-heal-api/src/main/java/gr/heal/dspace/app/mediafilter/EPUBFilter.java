/**
 * 
 */
package gr.heal.dspace.app.mediafilter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;

import org.apache.log4j.Logger;
import org.dspace.app.mediafilter.HTMLFilter;
import org.dspace.app.mediafilter.MediaFilter;

/**
 * @author aanagnostopoulos
 * 
 *         Uses Paul Siegmann's Epublib to extract full text from epub
 *         bitstreams.
 * 
 */
public class EPUBFilter extends MediaFilter {

	/** log4j logger */
	private static Logger log = Logger.getLogger(EPUBFilter.class);

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dspace.app.mediafilter.FormatFilter#getFilteredName(java.lang.String)
	 */
	@Override
	public String getFilteredName(String sourceName) {
		return sourceName + ".txt";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dspace.app.mediafilter.FormatFilter#getBundleName()
	 */
	@Override
	public String getBundleName() {
		return "TEXT";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dspace.app.mediafilter.FormatFilter#getFormatString()
	 */
	@Override
	public String getFormatString() {
		return "Text";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dspace.app.mediafilter.FormatFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Extracted text";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dspace.app.mediafilter.FormatFilter#getDestinationStream(java.io.
	 * InputStream)
	 */
	@Override
	public InputStream getDestinationStream(InputStream source)
			throws Exception {

		// try to read epub
		EpubReader epubReader = new EpubReader();
		Book book = epubReader.readEpub(source);
		
		// get the files containing the book 'core' (except for the preface)
		Spine spine = book.getSpine();
		StringBuffer sb = new StringBuffer();
		for(int i=0; i< spine.size();i++) {
			Resource resource = spine.getResource(i);
			MediaType mediaType = resource.getMediaType();
			if(mediaType.getName().equals(MediatypeService.XHTML.getName())) {
				//extract full text, using default HTMLFilter
		        HTMLEditorKit kit = new HTMLEditorKit();
		        Document doc = kit.createDefaultDocument();

		        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

		        kit.read(resource.getInputStream(), doc, 0);

		        String extractedText = doc.getText(0, doc.getLength());
		        sb.append(extractedText);
			}
		}
		
		if(sb.length()==0) {
			//no text extracted
			return null;
		}
		
        // generate an input stream with the extracted text
        byte[] textBytes = sb.toString().getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(textBytes);
		
		return bais;
	}

}
