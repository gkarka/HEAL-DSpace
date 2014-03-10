/**
 * 
 */
package gr.heal.dspace.app.xmlui.aspect.rdf.json;

import gr.heal.dspace.submit.step.ProcessMetadataStep;
import gr.heal.rdfSearch.domain.ResultRow;
import gr.heal.rdfSearch.domain.ResultSet;
import gr.heal.rdfSearch.service.IRdfSearchService;
import gr.heal.rdfSearch.service.RdfSearchServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.AbstractReader;
import org.apache.log4j.Logger;
import org.dspace.utils.DSpace;
import org.json.JSONArray;
import org.json.JSONStringer;
import org.json.JSONWriter;
import org.xml.sax.SAXException;

/**
 * @author aanagnostopoulos
 * 
 */
public class JSONRdfKeywordSearcher extends AbstractReader implements
		Recyclable {

	private static Logger log = Logger.getLogger(JSONRdfKeywordSearcher.class);
	
	private InputStream JSONStream;

	/** The Cocoon response */
	protected Response response;

	protected IRdfSearchService getSearchService() {
		DSpace dspace = new DSpace();

		org.dspace.kernel.ServiceManager manager = dspace.getServiceManager();

		return manager.getServiceByName(IRdfSearchService.class.getName(),
				IRdfSearchService.class);
	}

	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters par) throws ProcessingException, SAXException,
			IOException {

		// Retrieve all the given parameters
		Request request = ObjectModelHelper.getRequest(objectModel);
		this.response = ObjectModelHelper.getResponse(objectModel);

		String term = request.getParameter("term");

		try {
			ResultSet resultSet = getSearchService().search(term);
			log.info(resultSet.getResultRows());

			List<ResultRow> resultRows = resultSet.getResultRows();
			// convert to JSON
			 JSONWriter jsonWriter = new JSONStringer().array();
			 
			 for(ResultRow resultRow: resultRows) {
				 jsonWriter.object();
				 jsonWriter.key("label");
				 jsonWriter.value(resultRow.getValue()+ProcessMetadataStep.SOURCE_PREFIX+resultRow.getShortForm()+ProcessMetadataStep.SOURCE_SUFFIX);
				 jsonWriter.key("value");
				 jsonWriter.value(resultRow.getValue()+ProcessMetadataStep.SOURCE_PREFIX+resultRow.getLongForm()+ProcessMetadataStep.SOURCE_SUFFIX);
				 jsonWriter.endObject();
			 }
			 jsonWriter.endArray();
			 
			 // convert String into InputStream
			 JSONStream = new
			 ByteArrayInputStream(jsonWriter.toString().getBytes());

		} catch (Exception e) {
			log.error(
					"Error while retrieving JSON string for RDF auto complete",
					e);
		}

		log.info(request.getParameters());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.cocoon.reading.Reader#generate()
	 */
	@Override
	public void generate() throws IOException, SAXException,
			ProcessingException {
		if (JSONStream != null) {
			byte[] buffer = new byte[8192];

			response.setHeader("Content-Length",
					String.valueOf(JSONStream.available()));
			int length;
			while ((length = JSONStream.read(buffer)) > -1) {
				out.write(buffer, 0, length);
			}
			out.flush();
		}
	}

}
