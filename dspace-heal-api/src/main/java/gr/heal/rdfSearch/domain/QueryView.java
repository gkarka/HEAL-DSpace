/**
 * 
 */
package gr.heal.rdfSearch.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author aanagnostopoulos
 * 
 */
@XmlRootElement(name = "view")
public class QueryView {

	public static final String TYPE_LIST = "list";
	
	public QueryView() {
		// Must exist, otherwise REST fails
	}
	
	public QueryView(String type, Integer limit) {
		super();
		this.type = type;
		this.limit = limit;
	}

	private String type;

	private Integer limit;

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
