package gr.heal.rdfSearch.domain;

public class SKOSConceptScheme {
	
	private String uri = null;
	private String prefLabel = null;
	
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
	
	@Override
	public String toString() {
		return prefLabel;
	}

}
