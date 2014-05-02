package ntua.lib.dspace.toc;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Page implements ITocItem {
  private int pageNumber;
  public Page() {
  }

  public int getType() {
    return ITocItem.TYPE_PAGE;
  }

  public void add(ITocItem tocItem) {
    throw new RuntimeException("Method not implemented");
  }
  public int getPageNumber() {
    return pageNumber;
  }
  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public java.util.Iterator itemsIterator() {
    throw new RuntimeException("Method not implemented");
  }
}
