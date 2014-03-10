/**
 * 
 */
package gr.heal.rdfSearch.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author aanagnostopoulos
 * 
 */
@XmlRootElement(namespace = IConstants.NAMESPACE, name = "column")
public class ResultColumn {

	private String dataType;

	private String shortForm;

	private String sparqlSer;

	private String value;

	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String column) {
		this.value = column;
	}

	@XmlAttribute(name = "datatype")
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@XmlAttribute(name = "shortform")
	public String getShortForm() {
		return shortForm;
	}

	public void setShortForm(String shortForm) {
		this.shortForm = shortForm;
	}

	@XmlAttribute(name = "sparql_ser")
	public String getSparqlSer() {
		return sparqlSer;
	}

	public void setSparqlSer(String sparqlSer) {
		this.sparqlSer = sparqlSer;
	}

}
