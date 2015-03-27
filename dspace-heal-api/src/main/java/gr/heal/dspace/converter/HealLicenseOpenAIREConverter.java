/**
 * 
 */
package gr.heal.dspace.converter;

import org.dspace.content.crosswalk.IConverter;

/**
 * @author aanagnostopoulos
 *
 */
public class HealLicenseOpenAIREConverter implements IConverter {

	/* (non-Javadoc)
	 * @see org.dspace.content.crosswalk.IConverter#makeConversion(java.lang.String)
	 */
	@Override
	public String makeConversion(String value) {
		
		if(value==null) {
			return null;
		}
		
		if(value.equals("free")) {
			return "info:eu-repo/semantics/openAccess";
		}else if(value.equals("campus")) {
			return "info:eu-repo/semantics/restrictedAccess";
		}else if(value.equals("account")) {
			return "info:eu-repo/semantics/restrictedAccess";
		}else if(value.equals("embargo")) {
			return "info:eu-repo/semantics/embargoedAccess";
		}else if(value.equals("other")) {
			return "info:eu-repo/semantics/restrictedAccess";
		}else {
			return "info:eu-repo/semantics/restrictedAccess";
		}
		
	}

}
