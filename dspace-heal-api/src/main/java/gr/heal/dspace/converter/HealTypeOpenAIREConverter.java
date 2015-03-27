/**
 * 
 */
package gr.heal.dspace.converter;

import org.dspace.content.crosswalk.IConverter;

/**
 * @author aanagnostopoulos
 *
 */
public class HealTypeOpenAIREConverter implements IConverter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dspace.content.crosswalk.IConverter#makeConversion(java.lang.String)
	 */
	@Override
	public String makeConversion(String value) {

		if (value == null) {
			return null;
		}

		if (value.equals("bachelorThesis")) {
			return "info:eu-repo/semantics/bachelorThesis ";
		} else if (value.equals("masterThesis")) {
			return "info:eu-repo/semantics/masterThesis";
		} else if (value.equals("doctoralThesis")) {
			return "info:eu-repo/semantics/doctoralThesis";
		} else if (value.equals("conferenceItem")) {
			return "info:eu-repo/semantics/conferenceObject";
		} else if (value.equals("journalArticle")) {
			return "info:eu-repo/semantics/article";
		} else if (value.equals("bookChapter")) {
			return "info:eu-repo/semantics/bookPart";
		} else if (value.equals("book")) {
			return "info:eu-repo/semantics/book";
		} else if (value.equals("report")) {
			return "info:eu-repo/semantics/report";
		}
		// learningMaterial, dataset, image, video, audio & other are mapped to
		// 'other'
		else {
			return "info:eu-repo/semantics/other";
		}

	}

}
