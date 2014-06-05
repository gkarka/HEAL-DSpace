/**
 * 
 */
package gr.heal.rdfSearch.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import gr.heal.rdfSearch.domain.IConstants;
import gr.heal.rdfSearch.domain.SKOSConcept;
import gr.heal.rdfSearch.domain.SKOSConceptScheme;
import me.champeau.ld.UberLanguageDetector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.core.ConfigurationManager;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

/**
 * @author aanagnostopoulos
 * 
 */
@Service
public class RdfSearchServiceImpl implements IRdfSearchService {

	private static Logger log = Logger.getLogger(RdfSearchServiceImpl.class);

	@Override
	public List<SKOSConcept> search(String term, String scheme) throws Exception {

		// TODO: see what happens with fields
		UberLanguageDetector langDetector = null;
		Set<String> languages = null;

		if (langDetector == null || languages == null) {
			langDetector = UberLanguageDetector.getInstance();
			languages = new HashSet<String>();
			languages.add("el");
			languages.add("en");
		}

		String language = langDetector.detectLang(term, languages);
		log.info(language);
		String queryString = null;
		ResultSet results = null;
		List<String> phrases = new ArrayList<String>();
		String contains = "";
		String exactMatches = "";
		List<SKOSConcept> resultSet = new ArrayList<SKOSConcept>();

		if (!term.contains("http://")) {
			term = term.replaceAll("[^\\p{L}\\p{Digit}\"\'\\.\\p{Space}-]", "");
			log.info(term);
			queryString = createQuery(term, language, phrases, contains, exactMatches, scheme);
			if ("".equals(queryString))
				return new ArrayList<SKOSConcept>();
			log.info(queryString.toString());
			results = executeQuery(queryString);

			List<SKOSConceptScheme> schemes = fetchSchemes();
			while (results.hasNext()) {
				QuerySolution nextSolution = results.nextSolution();
				SKOSConcept skosConcept = new SKOSConcept();
				skosConcept.setUri(nextSolution.getResource("s1").getURI());
				findDetails(language, skosConcept, schemes);
				resultSet.add(skosConcept);
			}
		} else {
			List<SKOSConceptScheme> schemes = fetchSchemes();
			SKOSConcept skosConcept = new SKOSConcept();
			skosConcept.setUri(term);
			findDetails(language, skosConcept, schemes);
			resultSet.add(skosConcept);
		}

		return resultSet;
	}

	private String createExactGP(String term, String language, String scheme) {
		String queryGP;
		List<String> phrases = new ArrayList<String>();
		String exactMatches = "";
		term = extractPhrases(term, phrases);
		StringTokenizer tokenizer = new StringTokenizer(term, "  \"\t\n\r\f");
		int numTokens = tokenizer.countTokens();
		if (phrases.size() > 0) {
			exactMatches = createExact(phrases);
		} else if (numTokens > 0 && phrases.size() == 0) {
			String totalPhrase = tokenizer.nextToken();
			while (tokenizer.hasMoreTokens())
				totalPhrase = totalPhrase + " " + tokenizer.nextToken();
			phrases.add(totalPhrase);
			exactMatches = createExact(phrases);
		} else
			return "";

		queryGP =
				"SELECT DISTINCT ?s1 FROM <"
						+ IConstants.HEALP_SUBJECTS_GRAPH
						+ "> WHERE {"
						+ " ?s1 a <http://www.w3.org/2004/02/skos/core#Concept> ."
						+ " ?s1 <http://www.w3.org/2004/02/skos/core#inScheme> "
						+ ((scheme != null) ? "<" + scheme + "> ." : " ?o3 . ")
						+ " ?s1 <http://www.w3.org/2004/02/skos/core#prefLabel> ?o2  "
						+ " FILTER(langMatches(lang(?o2), \""
						+ language
						+ "\")) . "
						+ exactMatches
						+ "} order by desc "
						+ "(<sql:sum_rank> ((<sql:S_SUM> ( <SHORT_OR_LONG::IRI_RANK> (?s1),"
						// order by the first phrase
						+ " <SHORT_OR_LONG::>(?pexact0), <SHORT_OR_LONG::>(?oexact0), ?sc0 ) ) ) ) limit 10 offset 0";
		return queryGP;
	}

	private String createContainsGP(String term, String language, String scheme) {
		String queryGP;
		String contains = "";
		StringTokenizer tokenizer = new StringTokenizer(term, "  \"\t\n\r\f");
		int numTokens = tokenizer.countTokens();
		if (numTokens > 0)
			contains = createContains(tokenizer, numTokens);
		else
			return "";

		queryGP =
				"SELECT DISTINCT ?s1 FROM <"
						+ IConstants.HEALP_SUBJECTS_GRAPH
						+ "> WHERE {"
						+ " ?s1 a <http://www.w3.org/2004/02/skos/core#Concept> ."
						+ " ?s1 <http://www.w3.org/2004/02/skos/core#inScheme> "
						+ ((scheme != null) ? "<" + scheme + "> ." : " ?o3 . ")
						+ " ?s1 <http://www.w3.org/2004/02/skos/core#prefLabel> ?o2  "
						+ " FILTER(langMatches(lang(?o2), \""
						+ language
						+ "\")) . "
						+ contains
						+ "} order by desc "
						+ "(<sql:sum_rank> ((<sql:S_SUM> ( <SHORT_OR_LONG::IRI_RANK> (?s1),"
						+ " <SHORT_OR_LONG::>(?s1textp), <SHORT_OR_LONG::>(?o1), ?sc ) ) ) ) limit 10 offset 0";
		return queryGP;
	}

	private String createQuery(String term, String language, List<String> phrases, String contains,
			String exactMatches, String scheme) {
		String queryString = "";
		String exactGP = createExactGP(term, language, scheme);
		String containsGP = createContainsGP(term, language, scheme);
		String selectFrom =
				"SELECT DISTINCT ?s1 FROM <" + IConstants.HEALP_SUBJECTS_GRAPH + "> WHERE { ";
		if (!"".equals(exactGP) && !"".equals(containsGP)) {
			queryString =
					selectFrom + " { " + exactGP + " } UNION { " + containsGP
							+ " } } limit 10 offset 0";
		} else if (!"".equals(exactGP))
			queryString = selectFrom + exactGP + " } limit 10 offset 0";
		else if (!"".equals(containsGP))
			queryString = selectFrom + containsGP + " } limit 10 offset 0";
		return queryString;
	}

	private String extractPhrases(String term, List<String> phrases) {
		if (StringUtils.countMatches(term, "\"") % 2 == 0) {
			while (term.contains("\"")) {
				int lastind = 0;
				int ind = term.indexOf("\"", lastind + 1);
				String phrase =
						term.substring(term.indexOf("\"", lastind) + 1, term.indexOf("\"", ind));
				phrases.add(phrase);
				term = term.replace("\"" + phrase + "\"", "");
				lastind = ind;
			}
		}
		log.info("term after: " + term);
		return term;
	}

	private String createExact(List<String> phrases) {
		String exactMatches = "";
		int j = 0;
		for (String phrase : phrases)
			exactMatches =
					exactMatches + "?s1 ?pexact" + j + " ?oexact" + j + " . ?oexact" + j
							+ " <bif:contains> \'\\\"" + phrase + "\\\"\' OPTION ( score ?sc" + j
							+ ") FILTER regex(" + "?oexact" + j + ", " + "\"^" + phrase
							+ "$\", \"i\")" + ".";
		log.info("exact matches: " + exactMatches);
		return exactMatches;
	}

	private String createContains(StringTokenizer tokenizer, int numTokens) {
		String contains;
		String[] tokens = new String[numTokens];
		int i = 1;
		while (i < numTokens + 1) {
			tokens[numTokens - i] = tokenizer.nextToken();
			i++;
		}
		Iterator<String> it = Arrays.asList(tokens).iterator();
		String firstToken = it.next();
		if (firstToken.length() > 3)
			firstToken = firstToken + IConstants.LIKE_OPERAND;
		contains = "\'(" + "\\\"" + firstToken + "\\\"";
		while (it.hasNext()) {
			String nextToken = it.next();
			if (nextToken.length() > 3)
				nextToken = nextToken + IConstants.LIKE_OPERAND;
			contains = contains + " and \\\"" + nextToken + "\\\"";
		}
		contains = contains + ")\'";
		contains =
				" ?s1 ?s1textp ?o1 . ?o1 <bif:contains>  " + contains + " OPTION ( score ?sc ) . ";
		log.info("contains: " + contains);
		return contains;
	}

	@Override
	public List<SKOSConceptScheme> fetchSchemes() throws Exception {
		String queryString =
				new String("select distinct ?o1 ?o2 FROM <" + IConstants.HEALP_SUBJECTS_GRAPH
						+ "> where {?s1 a <http://www.w3.org/2004/02/skos/core#Concept> ."
						+ " ?s1 <http://www.w3.org/2004/02/skos/core#inScheme> ?o1 . "
						+ " OPTIONAL {?o1 <http://www.w3.org/2004/02/skos/core#prefLabel> ?o2 ."
						+ " FILTER(langMatches(lang(?o2),'en'))} }");

		ResultSet results = executeQuery(queryString);

		List<SKOSConceptScheme> resultSet = new ArrayList<SKOSConceptScheme>();

		while (results.hasNext()) {
			QuerySolution nextSolution = results.nextSolution();
			SKOSConceptScheme skosConceptScheme = new SKOSConceptScheme();
			String uri = nextSolution.getResource("o1").getURI();
			skosConceptScheme.setUri(uri);
			Literal label = nextSolution.getLiteral("o2");
			if (label != null)
				skosConceptScheme.setPrefLabel(label.getString());
			else
				skosConceptScheme.setPrefLabel(uri);
			resultSet.add(skosConceptScheme);
		}

		return resultSet;

	}

	private void findDetails(String language, SKOSConcept skosConcept,
			List<SKOSConceptScheme> schemes) {
		String infoQuery =
				"SELECT ?o2, ?o3, ?o4  FROM <" + IConstants.HEALP_SUBJECTS_GRAPH + "> WHERE {"
						+ " <" + skosConcept.getUri()
						+ "> <http://www.w3.org/2004/02/skos/core#inScheme> ?o3 . " + " <"
						+ skosConcept.getUri()
						+ "> <http://www.w3.org/2004/02/skos/core#prefLabel> ?o2 . "
						+ " FILTER(langMatches(lang(?o2), \"" + language + "\"))" + "} ";
		log.info(infoQuery);
		ResultSet infoResults = executeQuery(infoQuery);
		if (infoResults.hasNext()) {
			QuerySolution infoSolution = infoResults.nextSolution();
			skosConcept.setPrefLabel(infoSolution.getLiteral("o2").getString());
			Resource scheme = infoSolution.getResource("o3");
			SKOSConceptScheme schemeObj = findScheme(scheme.getURI(), schemes);
			if (schemeObj != null)
				skosConcept.setInScheme(schemeObj);
		}
	}

	private ResultSet executeQuery(String queryString) {
		ResultSet results = null;
		QueryEngineHTTP qe =
				new QueryEngineHTTP(ConfigurationManager.getProperty("rdfSearch.virtuosoUrl"),
						queryString);
		((QueryEngineHTTP) qe).addParam("timeout", "10000");
		results = qe.execSelect();
		return results;
	}

	private SKOSConceptScheme findScheme(String uri, List<SKOSConceptScheme> schemes) {
		for (SKOSConceptScheme scheme : schemes)
			if (scheme.getUri().equals(uri))
				return scheme;
		return null;
	}

}
