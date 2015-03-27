/**
 * 
 */
package gr.heal.dspace.converter;

import org.dspace.content.crosswalk.IConverter;

/**
 * @author aanagnostopoulos
 *
 */
public class HealDateAvailableOpenAIREConverter implements IConverter {

	/* (non-Javadoc)
	 * @see org.dspace.content.crosswalk.IConverter#makeConversion(java.lang.String)
	 */
	@Override
	public String makeConversion(String value) {
		
		if(value==null) {
			return null;
		}

		return " info:eu-repo/date/embargoEnd/"+value;
		
	}

}
