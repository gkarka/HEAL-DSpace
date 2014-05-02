package ntua.lib.dspace.toc;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface ITocItem {

  static final int TYPE_BOOK = 1;
  static final int TYPE_CHAPTER = 2;
  static final int TYPE_PAGERANGE = 3;
  static final int TYPE_PAGE = 4;

  int getType();
  void add(ITocItem tocItem);
  java.util.Iterator itemsIterator();
}
