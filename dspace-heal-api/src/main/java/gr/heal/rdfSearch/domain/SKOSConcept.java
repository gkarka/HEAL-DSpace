package gr.heal.rdfSearch.domain;

public class SKOSConcept {
	
	private String uri = null;
	private String prefLabel = null;
	private SKOSConceptScheme inScheme = null;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getPrefLabel() {
		return prefLabel;
	}
	public void setPrefLabel(String prefLabel) {
		this.prefLabel = prefLabel;
	}
	public SKOSConceptScheme getInScheme() {
		return inScheme;
	}
	public void setInScheme(SKOSConceptScheme inScheme) {
		this.inScheme = inScheme;
	}
	

	@Override
	public String toString() {
		return prefLabel;
	}

}
