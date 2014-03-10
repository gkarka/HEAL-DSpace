/**
 * 
 */
package gr.heal.rdfSearch.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author aanagnostopoulos
 *
 */
@XmlRootElement(name = "query")
public class Query {

	
	private QueryClass qclass;
	private String text;
	private QueryView view;

	@XmlElement(name="class")
	public QueryClass getQclass() {
		return qclass;
	}

	public void setQclass(QueryClass qclass) {
		this.qclass = qclass;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public QueryView getView() {
		return view;
	}

	public void setView(QueryView view) {
		this.view = view;
	}

}
