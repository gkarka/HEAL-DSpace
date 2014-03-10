/**
 * 
 */
package gr.heal.rdfSearch.service;

import gr.heal.rdfSearch.domain.Query;
import gr.heal.rdfSearch.domain.QueryClass;
import gr.heal.rdfSearch.domain.QueryView;
import gr.heal.rdfSearch.domain.ResultSet;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.dspace.core.ConfigurationManager;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author aanagnostopoulos
 * 
 */
@Service
public class RdfSearchServiceImpl implements IRdfSearchService {

	// private String virtuosoUrl = null;

	@Override
	public ResultSet search(String term) throws Exception {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		// WebResource service = client.resource(virtuosoUrl);
		WebResource service = client.resource(ConfigurationManager
				.getProperty("rdfSearch.virtuosoUrl"));
		Query query = new Query();
		query.setText(term);
		query.setView(new QueryView(QueryView.TYPE_LIST, ConfigurationManager
				.getIntProperty("rdfSearch.maxResults")));
		query.setQclass(new QueryClass(QueryClass.IRI_SKOS));

		try {
			JAXBContext jc = JAXBContext.newInstance(Query.class,
					QueryClass.class, QueryView.class);

			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(query, System.err);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ClientResponse response = service.accept(MediaType.TEXT_XML)
				.entity(query, MediaType.TEXT_XML).post(ClientResponse.class);

		if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
			throw new Exception();
		}

		ResultSet resultSet = response.getEntity(ResultSet.class);

		return resultSet;
	}

	// public String getVirtuosoUrl() {
	// return virtuosoUrl;
	// }
	//
	// public void setVirtuosoUrl(String virtuosoUrl) {
	// this.virtuosoUrl = virtuosoUrl;
	// }

}
