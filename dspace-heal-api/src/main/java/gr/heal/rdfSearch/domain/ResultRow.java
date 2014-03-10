/**
 * 
 */
package gr.heal.rdfSearch.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author aanagnostopoulos
 * 
 */
@XmlRootElement(namespace = IConstants.NAMESPACE, name = "row")
// @XmlType(namespace = IConstants.NAMESPACE, propOrder = { "subject", "label"
// })
public class ResultRow {

	private List<ResultColumn> resultColumns;

	@XmlElement(namespace = IConstants.NAMESPACE, name = "column")
	public List<ResultColumn> getResultColumns() {
		return resultColumns;
	}

	public void setResultColumns(List<ResultColumn> resultColumns) {
		this.resultColumns = resultColumns;
	}

	public String getShortForm() {
		return (resultColumns != null && !resultColumns.isEmpty()) ? resultColumns
				.get(0).getShortForm() : null;
	}

	public String getLongForm() {
		return (resultColumns != null && !resultColumns.isEmpty()) ? resultColumns
				.get(0).getValue() : null;
	}
	
	public String getValue() {
		return (resultColumns != null && !resultColumns.isEmpty()) ? resultColumns
				.get(1).getValue() : null;
	}

}
