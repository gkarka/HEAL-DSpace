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
import org.dspace.content.Item;
import java.io.InputStream;
import java.util.Iterator;
import ntua.lib.dspace.TocUtil;
import ntua.lib.dspace.toc.*;
import java.io.IOException;

public class TocAllPagesTag extends TocAllPagesBaseTag {
	
	public TocAllPagesTag() {
		super();
	}

	public int doStartTag() {
		try {
			JspWriter out = this.pageContext.getOut();
			// this.processBook(book, out);
			this.processBitstream(out);
		} catch (Exception ex) {
			throw new Error(ex.getMessage());
		}
		return this.SKIP_BODY;
	}

	private void processBitstream(JspWriter out) {
		try {
			InputStream is = getTocBitstream().retrieve();
			Book book = TocUtil.parseBitstream(is, getItem());

			this.processBook(book, out);
		} catch (Exception ex) {
			throw new Error(ex.getMessage());
		}
	}


	protected void processBook(Book book, JspWriter out) throws IOException {
		String title = book.getTitle();
		String pageStart = String.valueOf(book.getPageStart());
		String pageEnd = String.valueOf(book.getPageEnd());

		this.currentProcPageIdx = 0;
		currentPage = 1;

		out.print("<div>");
		Iterator iter = book.itemsIterator();
		while (iter.hasNext()) {
			ITocItem tocItem = (ITocItem) iter.next();
			this.processTocItem(tocItem, out);
		}
		out.print("</div>");

	}

	private void processTocItem(ITocItem tocItem, JspWriter out)
			throws IOException {
		if (tocItem.getType() == ITocItem.TYPE_PAGE) {
			this.processPage((Page) tocItem, out);
		}
	}

	private int findFirstPageInChapter(Chapter chapter) {
		int res = -1;

		Iterator iter = chapter.itemsIterator();
		while (iter.hasNext() && res == -1) {
			ITocItem tocItem = (ITocItem) iter.next();
			if (tocItem.getType() == ITocItem.TYPE_PAGE) {
				Page page = (Page) tocItem;
				res = page.getPageNumber();
			} else if (tocItem.getType() == ITocItem.TYPE_PAGERANGE) {
				PageRange pageRange = (PageRange) tocItem;
				res = pageRange.getPageStart();
			} else if (tocItem.getType() == ITocItem.TYPE_CHAPTER) {
				Chapter childChapter = (Chapter) tocItem;
				res = this.findFirstPageInChapter(childChapter);
			}
		}

		return res;
	}

	private void processPage(Page page, JspWriter out) throws IOException {
		String pageNum = String.valueOf(page.getPageNumber());
		String pageLink = getPageViewBase() + pageNum;
		String link = this.tagA(pageLink, String.valueOf(currentPage));
		out.print("<span class='Toc'>");
		out.print(link + "  ");
		out.print("</span>");
		currentPage++;
	}

	private String tagA(String href, String text, String attrs) {
		if (attrs == null)
			attrs = "";
		return "<a href='" + href + "' " + attrs + ">" + text + "</a>";
	}

	private String tagA(String href, String text) {
		return tagA(href, text, "class='toc'");
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
