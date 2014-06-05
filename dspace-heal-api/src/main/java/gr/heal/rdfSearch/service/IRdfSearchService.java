/**
 * 
 */
package gr.heal.rdfSearch.service;

import java.util.List;

import gr.heal.rdfSearch.domain.SKOSConcept;
import gr.heal.rdfSearch.domain.SKOSConceptScheme;

/**
 * @author aanagnostopoulos
 *
 */
public interface IRdfSearchService {

	List<SKOSConcept> search(String term, String scheme) throws Exception;
	
	List<SKOSConceptScheme> fetchSchemes() throws Exception;
}
