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

public class TocNavigateTag extends BaseTag {
	
	public TocNavigateTag() {
	}

	public int doStartTag() {
		try {
			JspWriter out = this.pageContext.getOut();
			this.processBook(getBook(), out);
		} catch (Exception ex) {
			throw new Error(ex.getMessage());
		}
		return this.SKIP_BODY;
	}


	private void processBook(Book book, JspWriter out) throws IOException {
		String title = book.getTitle();
		String pageStart = String.valueOf(book.getPageStart());
		String pageEnd = String.valueOf(book.getPageEnd());

		this.currentProcPageIdx = 0;
		currentPage = 1;

		String firstPageLink = getPageViewBase() + (book.getPageStart());
		String lastPageLink = getPageViewBase() + book.getPageEnd();
		String currentPageIdxStr = (String) pageContext.getRequest()
				.getAttribute("PageNumber");
		if (currentPageIdxStr == null || currentPageIdxStr.length() == 0)
			currentPageIdxStr = "0";

		int currentPageIdx = Integer.parseInt(currentPageIdxStr);
		if (currentPageIdx == 0) {
			currentPageIdx = book.getPageStart();
		}

		String prevPageLink = String.valueOf(currentPageIdx - 1);
		String nextPageLink = String.valueOf(currentPageIdx + 1);

		if (currentPageIdx <= book.getPageStart())
			prevPageLink = String.valueOf(currentPageIdx);

		if (currentPageIdx >= book.getPageEnd())
			nextPageLink = String.valueOf(currentPageIdx);

		out.println(this.tagA(firstPageLink, "&lt;&lt;") + "&nbsp;&nbsp");
		out.println(this.tagA(prevPageLink, "&lt;") + "&nbsp;&nbsp");
		// out.println(currentPageIdxStr + "&nbsp;&nbsp");
		out.println(this.tagA(nextPageLink, "&gt;") + "&nbsp;&nbsp");
		out.println(this.tagA(lastPageLink, "&gt;&gt;"));

	}

	private String tagA(String href, String text, String attrs) {
		if (attrs == null)
			attrs = "";
		return "<a class='tocNavigate' href='" + href + "' " + attrs + ">"
				+ text + "</a>";
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
