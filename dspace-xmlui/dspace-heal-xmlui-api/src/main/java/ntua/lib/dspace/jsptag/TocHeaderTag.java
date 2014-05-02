package ntua.lib.dspace.jsptag;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.dspace.content.Bitstream;
import java.io.InputStream;
import java.util.Iterator;
import ntua.lib.dspace.TocUtil;
import ntua.lib.dspace.toc.*;
import java.io.IOException;

public class TocHeaderTag extends BaseTag {

	public TocHeaderTag() {
	}

	public int doStartTag() {
		try {
			JspWriter out = this.pageContext.getOut();
			this.processBook(getBook(), out);
			// this.processTocBitstream(out);
		} catch (Exception ex) {
			throw new Error(ex.getMessage());
		}
		return this.SKIP_BODY;
	}

	/*
	 * private void processTocBitstream(JspWriter out) { try { InputStream is =
	 * this.tocBitstream.retrieve(); Book book = TocUtil.parseTocXml(is);
	 * 
	 * this.processBook(book, out); } catch (Exception ex) { throw new
	 * Error(ex.getMessage()); } }
	 */


	private void processBook(Book book, JspWriter out) throws IOException {
		String title = book.getTitle();
		String pageStart = String.valueOf(book.getPageStart());
		String pageEnd = String.valueOf(book.getPageEnd());

		this.currentProcPageIdx = 0;
		currentPage = 1;
		// currentPage = Integer.parseInt(pageStart);

		String firstPageLink = getPageViewBase() + (book.getPageStart());
		out.println(tagA(firstPageLink, title));
	}

	private String tagA(String href, String text, String attrs) {
		if (attrs == null)
			attrs = "";
		return "<a class='tocHeader' href='" + href + "' " + attrs + ">" + text
				+ "</a>";
	}

	private String tagA(String href, String text) {
		return tagA(href, text, null);
	}

	private String tagNewRow() {
		return "</td></tr><tr><td>";
	}

	private String indent(int spaceCount) {
		String ind = "";
		for (int i = 0; i < spaceCount; i++)
			ind += "&nbsp;";
		return ind;
	}

}
