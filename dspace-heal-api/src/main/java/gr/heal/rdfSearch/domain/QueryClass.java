package gr.heal.rdfSearch.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "class")
public class QueryClass {
	
	public static final String IRI_SKOS = "http://www.w3.org/2004/02/skos/core#Concept";
	
	private String iri;
	
	public QueryClass() {
		// TODO Auto-generated constructor stub
	}

	
	
	public QueryClass(String iri) {
		super();
		this.iri = iri;
	}


	@XmlAttribute
	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}
	

}
