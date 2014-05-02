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

public class TocTag extends BaseTag {
  public TocTag() {
  }

  public int doStartTag() {
    try {
      JspWriter out = this.pageContext.getOut();
      this.processBook(getBook(), out);
//      this.processTocBitstream(out);
    }
    catch (Exception ex) {
      throw new Error(ex.getMessage());
    }
    return this.SKIP_BODY;
  }

/*
  private void processTocBitstream(JspWriter out) {
    try {
      InputStream is = this.tocBitstream.retrieve();
      Book book = TocUtil.parseTocXml(is);

      this.processBook(book, out);
    }
    catch (Exception ex) {
      throw new Error(ex.getMessage());
    }
  }
*/

/*  private int currentProcPageIdx;
  int chapterDepth = 0;
  int currentPage = 1;
*/
  
  private void processBook(Book book, JspWriter out) throws IOException {
    String title = book.getTitle();
    String pageStart = String.valueOf(book.getPageStart());
    String pageEnd = String.valueOf(book.getPageEnd());

    this.currentProcPageIdx = 0;
    currentPage = 1;

    out.print("<div>");
    out.println("<ul class='tocList'>");
    Iterator iter = book.itemsIterator();
    while (iter.hasNext()) {
      ITocItem tocItem = (ITocItem) iter.next();
      this.processTocItem(tocItem, out);
    }
    out.println("</ul>");
    out.print("</div>");

  }

  private void processTocItem(ITocItem tocItem, JspWriter out) throws IOException {
    if (tocItem.getType() == ITocItem.TYPE_CHAPTER) {
      this.processChapter((Chapter) tocItem, out);
    }
    if (tocItem.getType() == ITocItem.TYPE_PAGERANGE) {
      //this.processPageRange((PageRange) tocItem, out);
    }
    if (tocItem.getType() == ITocItem.TYPE_PAGE) {
      //this.processPage((Page) tocItem, out);
    }
  }

  private void processChapter(Chapter chapter, JspWriter out) throws IOException {
    this.processChapterStart(chapter, out);

    //this.currentPage = chapter.getPageStart();

    Iterator iter = chapter.itemsIterator();
    while (iter.hasNext()) {
      ITocItem tocItem = (ITocItem) iter.next();
      if (tocItem.getType() == ITocItem.TYPE_CHAPTER)
      {

//      out.print("<div style='margin-left: 20px;'>");
        out.println("<ul class='tocList'>");
        this.processTocItem(tocItem, out);
        out.println("</ul>");
//      out.println("</div>");
      }
    }

    this.processChapterEnd(chapter, out);
  }


  private void processChapterStart(Chapter chapter, JspWriter out) throws IOException {
    String pageStart = chapter.getPageStart();
    String pageEnd = chapter.getPageEnd();
    String title = chapter.getTitle();

    String page = "";
    if (pageStart != null && pageStart.length() > 0 && !"0".equals(pageStart))
      page = "(\u03a3\u03b5\u03bb\u03af\u03b4\u03b1 " + pageStart + ")";

    String chapterPage = String.valueOf(this.findFirstPageInChapter(chapter));

    chapterDepth++;
//    out.print(this.indent(3 * chapterDepth));
    //span"this.indent(3 * chapterDepth));
    out.println("<li>");
    out.print(tagA(getPageViewBase() + chapterPage, title));
    out.println("<br><span class='toc tocPage'>" + page + "</span>");
    out.println("</li>");
//    out.println("<br>");
  }

  private int findFirstPageInChapter(Chapter chapter) {
    int res = -1;

    Iterator iter = chapter.itemsIterator();
    while (iter.hasNext() && res == -1) {
      ITocItem tocItem = (ITocItem) iter.next();
      if (tocItem.getType() == ITocItem.TYPE_PAGE) {
        Page page = (Page) tocItem;
        res = page.getPageNumber();
      }
      else if (tocItem.getType() == ITocItem.TYPE_PAGERANGE) {
        PageRange pageRange = (PageRange) tocItem;
        res = pageRange.getPageStart();
      }
      else if (tocItem.getType() == ITocItem.TYPE_CHAPTER) {
        Chapter childChapter = (Chapter) tocItem;
        res = this.findFirstPageInChapter(childChapter);
      }
    }

    return res;
  }

  private void processChapterEnd(Chapter chapter, JspWriter out) throws IOException {
    chapterDepth--;
    out.println("</li>");
  }


  private void processPageRange(PageRange pageRange, JspWriter out) throws IOException {
    String pageStart = String.valueOf(pageRange.getPageStart());
    String pageEnd = String.valueOf(pageRange.getPageEnd());

    out.print("<span class='TocPage'>");
    for (int idx = pageRange.getPageStart(); idx < pageRange.getPageEnd(); idx++) {
      String pageLink = getPageViewBase() + String.valueOf(idx);
      String link = this.tagA(pageLink, String.valueOf(this.currentPage));
      out.print(link + "  ");
      currentPage++;
    }
    out.print("</span>");
    out.print("<br>");
  }

  private void processPage(Page page, JspWriter out) throws IOException {
    String pageNum = String.valueOf(page.getPageNumber());
    String pageLink = getPageViewBase() + pageNum;
    String link = tagA(pageLink, String.valueOf(currentPage));
    out.print("<span class='TocPage'>");
    out.print(link + "  ");
    out.print("</span>");
    out.print("<br>");
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
    for (int i=0; i < spaceCount; i++)
      ind += "&nbsp;";
    return ind;
  }

}
