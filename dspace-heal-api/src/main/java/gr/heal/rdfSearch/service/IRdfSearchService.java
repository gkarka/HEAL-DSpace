/**
 * 
 */
package gr.heal.rdfSearch.service;

import gr.heal.rdfSearch.domain.ResultSet;

/**
 * @author aanagnostopoulos
 *
 */
public interface IRdfSearchService {

	ResultSet search(String term) throws Exception;
	
}
