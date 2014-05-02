package ntua.lib.dspace.jsptag;

import javax.servlet.jsp.tagext.TagSupport;

import ntua.lib.dspace.toc.Book;

import org.dspace.content.Bitstream;

public class BaseTag extends TagSupport {

	private Bitstream bitstream;

	private String pageViewBase;

	private Book book;

	private Bitstream tocBitstream;

	protected int currentProcPageIdx;

	protected int chapterDepth = 0;

	protected int currentPage = 1;

	public Bitstream getBitstream() {
		return bitstream;
	}

	public void setBitstream(Bitstream bitstream) {
		this.bitstream = bitstream;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getPageViewBase() {
		return pageViewBase;
	}

	public void setPageViewBase(String pageViewBase) {
		this.pageViewBase = pageViewBase;
	}

	public Bitstream getTocBitstream() {
		return tocBitstream;
	}

	public void setTocBitstream(Bitstream tocBitstream) {
		this.tocBitstream = tocBitstream;
	}

}
