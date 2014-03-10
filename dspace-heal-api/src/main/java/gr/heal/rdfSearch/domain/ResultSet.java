/**
 * 
 */
package gr.heal.rdfSearch.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author aanagnostopoulos
 * 
 */
@XmlRootElement(namespace = IConstants.NAMESPACE, name = "facets")
@XmlType(propOrder = { "sparql", "time", "complete", "timeout", "dbActivity",
		"resultRows" })
public class ResultSet {

	private String sparql;

	private Float time;

	private String complete;

	private Float timeout;

	private String dbActivity;

	private List<ResultRow> resultRows;

	@XmlElement(namespace = IConstants.NAMESPACE, name = "sparql")
	public String getSparql() {
		return sparql;
	}

	public void setSparql(String sparql) {
		this.sparql = sparql;
	}

	@XmlElement(namespace = IConstants.NAMESPACE, name = "time")
	public Float getTime() {
		return time;
	}

	public void setTime(Float time) {
		this.time = time;
	}

	@XmlElement(namespace = IConstants.NAMESPACE, name = "complete")
	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}

	@XmlElement(namespace = IConstants.NAMESPACE, name = "timeout")
	public Float getTimeout() {
		return timeout;
	}

	public void setTimeout(Float timeout) {
		this.timeout = timeout;
	}

	@XmlElement(namespace = IConstants.NAMESPACE, name = "db-activity")
	public String getDbActivity() {
		return dbActivity;
	}

	public void setDbActivity(String dbActivity) {
		this.dbActivity = dbActivity;
	}

	@XmlElementWrapper(namespace = IConstants.NAMESPACE, name = "result")
	@XmlElement(namespace = IConstants.NAMESPACE, name = "row")
	public List<ResultRow> getResultRows() {
		return resultRows;
	}

	public void setResultRows(List<ResultRow> resultRows) {
		this.resultRows = resultRows;
	}
	

}
