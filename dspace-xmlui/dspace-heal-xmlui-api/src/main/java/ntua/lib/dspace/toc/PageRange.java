package ntua.lib.dspace.toc;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PageRange implements ITocItem {
  private int pageStart;
  private int pageEnd;
  public PageRange() {
  }

  public int getType() {
    return ITocItem.TYPE_PAGERANGE;
  }

  public void add(ITocItem tocItem) {
    throw new RuntimeException("Method not implemented");
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

  public java.util.Iterator itemsIterator() {
    throw new RuntimeException("Method not implemented");
  }

}
