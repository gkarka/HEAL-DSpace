package ntua.lib.dspace.toc;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Chapter implements ITocItem {
  public Chapter() {
  }

  public int getType() {
    return ITocItem.TYPE_CHAPTER;
  }

  ArrayList tocItems = new ArrayList();
  private String title;
  private String pageStart = "";
  private String pageEnd = "";
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
  public String getPageStart() {
    return pageStart;
  }
  public void setPageStart(String pageStart) {
    this.pageStart = pageStart;
  }
  public String getPageEnd() {
    return pageEnd;
  }
  public void setPageEnd(String pageEnd) {
    this.pageEnd = pageEnd;
  }

}
