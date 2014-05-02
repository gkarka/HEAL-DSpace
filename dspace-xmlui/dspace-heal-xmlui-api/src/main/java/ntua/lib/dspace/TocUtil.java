package ntua.lib.dspace;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.apache.xpath.*;
import javax.xml.transform.*;
import org.dspace.content.Bitstream;
import org.dspace.content.Item;
import org.dspace.content.DCValue;
import org.dspace.authorize.*;
import java.sql.*;
import java.util.*;

import ntua.lib.dspace.toc.*;

public class TocUtil {
  public TocUtil() {
  }

  public static Book parseBitstream(InputStream stream, Item item) {
    if (item == null || stream == null)
      return null;

    Book book = new Book();
    TarUtil tar = null;
    try {
      //InputStream stream = bitstream.retrieve();
      tar = new TarUtil(stream);

      Chapter chapter = new Chapter();

      DCValue[] dcValues = item.getDC("title", null, Item.ANY);
      String name = dcValues[0].value;
      String title = dcValues[0].value;
      String[] images = tar.listImages();
      int lastPageIndex = tar.listImages().length;

      String firstPage = images[0].substring(0, 8);
      String lastPage = images[images.length - 1].substring(0, 8);
      if (lastPage.toLowerCase().startsWith("thumb"))
        lastPage = images[images.length - 2];

      book.setTitle(title);

      for (int idx = 0; idx < images.length; idx++) {
        Page page = new Page();
        String filename = images[idx].substring(0, 8);
        try {
          page.setPageNumber(Integer.parseInt(filename));
          book.add(page);
        } catch (NumberFormatException ex) {
          page.setPageNumber(0);
        }
      }

      Iterator iter = book.itemsIterator();
      if (iter.hasNext()) {
        Page page = (Page)iter.next();
        book.setPageStart(page.getPageNumber());
      }
      Object pageObj = null;
      while (iter.hasNext()) {
        pageObj = iter.next();
      }
      if (pageObj != null) {
        Page page = (Page) pageObj;
        book.setPageEnd(page.getPageNumber());
      }



      //chapter.setIndex(Integer.parseInt(seq));
/*
      chapter.setPageStart(1);
      chapter.setPageEnd(lastPageIndex);
      //chapter.setName(name);
      chapter.setTitle(name + " - " + title);

      PageRange pageRange = new PageRange();
      pageRange.setPageStart(1);
      pageRange.setPageEnd(lastPageIndex);

      chapter.add(pageRange);
      //toc.chapters.put(new Integer(chapter.getIndex()), chapter);
      book.add(chapter);
 */
    }
//    catch (AuthorizeException ex) {
//    }
//    catch (SQLException ex) {
//    }
    catch (IOException ex) {
    }
    finally {
      if (stream != null)
        try {
          stream.close();
        }
        catch (IOException ex1) {
        }
    }

    return book;
  }


  public static ntua.lib.dspace.toc.Book parseTocXml(InputStream tocStream, InputStream bookStream) {
    Book book = new Book();

    TarUtil tar = null;

    try {
      tar = new TarUtil(bookStream);
      String firstPageFileName = tar.listImages()[0];

      Document tocDocument = javax.xml.parsers.DocumentBuilderFactory.
          newInstance().
          newDocumentBuilder().parse(tocStream);

      Element elmBook = (Element)tocDocument.getElementsByTagName("book").item(0);
      book.setTitle(elmBook.getAttribute("name"));

      //String pageStart = elmBook.getAttribute("pageStart");
      String pageStart = firstPageFileName.substring(0, firstPageFileName.indexOf("."));
      if (pageStart != null && pageStart.length() > 0)
        book.setPageStart(Integer.parseInt(pageStart));

      book.setPageEnd(book.getPageStart() + tar.listImages().length - 1);
      //String pageEnd = elmBook.getAttribute("pageEnd");
      //if (pageEnd != null && pageEnd.length() > 0)
      //  book.setPageEnd(Integer.parseInt(pageEnd));

      NodeList childNodes = elmBook.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node node = childNodes.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) node;
          if ("chapter".equals(node.getNodeName())) {
            processChapter(element, book);
          }
          else if ("page".equals(node.getNodeName())) {
            processPage(element, book);
          }
          else if ("pageRange".equals(node.getNodeName())) {
            processPageRange(element, book);
          }
        }
      }
    }
    catch (ParserConfigurationException ex) {
      ex.printStackTrace();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    catch (SAXException ex) {
      ex.printStackTrace();
    }
    return book;
  }

  private static void processChapter(Element element, ITocItem tocItem) {
    Chapter chapter = new Chapter();
    chapter.setTitle(element.getAttribute("title"));

    String pageStart = element.getAttribute("pageStart");
    if (pageStart != null && pageStart.length() > 0)
      //chapter.setPageStart(Integer.parseInt(pageStart));
      chapter.setPageStart(pageStart);

    String pageEnd = element.getAttribute("pageEnd");
    if (pageEnd != null && pageEnd.length() > 0)
      //chapter.setPageEnd(Integer.parseInt(pageEnd));
      chapter.setPageEnd(pageEnd);

    NodeList childNodes = element.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node node = childNodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element elmChild = (Element) node;
        if ("chapter".equals(elmChild.getNodeName())) {
          processChapter(elmChild, chapter);
        }
        else if ("page".equals(elmChild.getNodeName())) {
          processPage(elmChild, chapter);
        }
        else if ("pageRange".equals(elmChild.getNodeName())) {
          processPageRange(elmChild, chapter);
        }
      }
    }

    tocItem.add(chapter);
  }

  private static void processPage(Element element, ITocItem tocItem) {
    Page page = new Page();

    String pageNum = element.getAttribute("num");
    if (pageNum != null && pageNum.length() > 0)
      page.setPageNumber(Integer.parseInt(pageNum));

    tocItem.add(page);
  }

  private static void processPageRange(Element element, ITocItem tocItem) {
    PageRange pageRange = new PageRange();

    String pageStart = element.getAttribute("start");
    if (pageStart != null && pageStart.length() > 0)
      pageRange.setPageStart(Integer.parseInt(pageStart));

    String pageEnd = element.getAttribute("end");
    if (pageEnd != null && pageEnd.length() > 0)
      pageRange.setPageEnd(Integer.parseInt(pageEnd));

    tocItem.add(pageRange);
  }

/*
    public static TocInfo parseBitstream(InputStream stream, Item item) {
      if (item == null || stream == null)
        return null;

      TocInfo toc = new TocInfo();
      TarUtil tar = null;
      try {
        //InputStream stream = bitstream.retrieve();
        tar = new TarUtil(stream);

        TocItem chapter = new TocItem();

        String seq = "1";

        DCValue[] dcValues = item.getDC("title", null, Item.ANY);
        String name = dcValues[0].value;
        String title = dcValues[0].value;
        int lastPageIndex = tar.listImages().length-1;

        toc.setTitle(title);
        toc.setPageStart(1);
        toc.setPageEnd(lastPageIndex);

        chapter.setIndex(Integer.parseInt(seq));
        chapter.setPageStart(1);
        chapter.setPageEnd(lastPageIndex);
        chapter.setName(name);
        chapter.setTitle(title);

        //toc.chapters.put(new Integer(chapter.getIndex()), chapter);
        toc.chapters.add(chapter);
      }
//    catch (AuthorizeException ex) {
//    }
//    catch (SQLException ex) {
//    }
      catch (IOException ex) {
      }
      finally {
        if (stream != null)
          try {
            stream.close();
          }
          catch (IOException ex1) {
          }
      }

      return toc;
    }


  public static TocInfo __parseTocXml(InputStream tocStream) {
    TocInfo toc = new TocInfo();

    toc.setPageStart(1);
    int lastPageIndex = 0;

    try {
      Document tocDocument = javax.xml.parsers.DocumentBuilderFactory.
          newInstance().
          newDocumentBuilder().parse(tocStream);

      Element tocRoot = tocDocument.getDocumentElement();

      try {
        NodeIterator nodeIter = XPathAPI.selectNodeIterator(tocRoot,
            "//book/*");
        Node node = nodeIter.nextNode();
        while (node != null) {
          if (node.getNodeType() == Node.ELEMENT_NODE) {
            if ("chapter".equals(node.getNodeName())) {
              TocItem chapter = new TocItem();
              chapter.setType(TocItem.TYPE_CHAPTER);

              String seq = node.getAttributes().getNamedItem("seq").
                  getNodeValue();

              //     String pageIndexStart = node.getAttributes().getNamedItem(
              //              "pageIndexStart").getNodeValue();
              //     String pageIndexEnd = node.getAttributes().getNamedItem(
              //              "pageIndexEnd").getNodeValue();

              String name = node.getAttributes().getNamedItem(
                  "name").getNodeValue();
              String title = node.getAttributes().getNamedItem(
                  "title").getNodeValue();

              NodeList pageNodes = node.getChildNodes();
              for (int i = 0; i < pageNodes.getLength(); i++) {
                Node pageNode = pageNodes.item(i);
                if (pageNode.getNodeType() == Node.ELEMENT_NODE) {
                  PageRange pageRange = new PageRange();

                  if ("page".equals(pageNode.getNodeName())) {
                    String num = pageNode.getAttributes().getNamedItem("num").
                        getNodeValue();
                    pageRange.setFrom(num);
                    pageRange.setTo(num);
                  }
                  else if ("pageRange".equals(pageNode.getNodeName())) {
                    String start = pageNode.getAttributes().getNamedItem(
                        "start").
                        getNodeValue();
                    String end = pageNode.getAttributes().getNamedItem("end").
                        getNodeValue();
                    pageRange.setFrom(start);
                    pageRange.setTo(end);
                  }
                  chapter.getPageRanges().add(pageRange);
                  if (Integer.parseInt(pageRange.getTo()) > lastPageIndex)
                    lastPageIndex = Integer.parseInt(pageRange.getTo());
                }
              }

              chapter.setIndex(Integer.parseInt(seq));
              chapter.setName(name);
              chapter.setTitle(title);

              //toc.chapters.put(new Integer(chapter.getIndex()), chapter);
              toc.chapters.add(chapter);
            }
            else if ("page".equals(node.getNodeName())) {
              TocItem page = new TocItem();
              page.setType(TocItem.TYPE_PAGE);
              PageRange pageRange = new PageRange();
              String num = node.getAttributes().getNamedItem("num").
                  getNodeValue();
              pageRange.setFrom(num);
              pageRange.setTo(num);
              page.getPageRanges().add(pageRange);
              page.setPageStart(Integer.parseInt(num));
              page.setPageEnd(Integer.parseInt(num));
              toc.chapters.add(page);
              if (Integer.parseInt(pageRange.getTo()) > lastPageIndex)
                lastPageIndex = Integer.parseInt(pageRange.getTo());
            }
          }
          node = nodeIter.nextNode();
        }
      }
      catch (TransformerException ex1) {
      }
    }
    catch (ParserConfigurationException ex) {
      ex.printStackTrace();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    catch (SAXException ex) {
      ex.printStackTrace();
    }

    toc.setPageEnd(lastPageIndex);
    return toc;
  }
*/
}
