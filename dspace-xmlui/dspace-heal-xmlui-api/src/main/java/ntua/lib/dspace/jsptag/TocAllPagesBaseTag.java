package ntua.lib.dspace.jsptag;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.jsp.JspWriter;

import ntua.lib.dspace.TocUtil;
import ntua.lib.dspace.toc.Book;

import org.dspace.content.Item;

public class TocAllPagesBaseTag extends BaseTag {

	private Item item;

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	
}
