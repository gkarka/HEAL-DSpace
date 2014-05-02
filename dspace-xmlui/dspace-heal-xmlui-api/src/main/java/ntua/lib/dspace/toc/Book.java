package ntua.lib.dspace.toc;

import java.util.ArrayList;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Book implements ITocItem {
  public Book() {
  }

  public int getType() {
    return ITocItem.TYPE_BOOK;
  }

  ArrayList tocItems = new ArrayList();
  private String title;
  private int pageStart;
  private int pageEnd;
  public void add(ITocItem tocItem) {
    if (tocItem.getType() == ITocItem.TYPE_PAGE || tocItem.getType() == ITocItem.TYPE_CHAPTER || tocItem.getType() == ITocItem.TYPE_PAGERANGE) {
      tocItems.add(tocItem);
    }
  }
  public java.util.Iterator itemsIterator() {
    return tocItems.iterator();
  }

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public int getPageStart() {
    return pageStart;
  }
  public void setPageStart(int pageStart) {
    this.pageStart = pageStart;
  }
  public int getPageEnd() {
    return pageEnd;
  }
  public void setPageEnd(int pageEnd) {
    this.pageEnd = pageEnd;
  }
}
