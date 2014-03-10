/**
 * 
 */
package gr.heal.dspace.app.util;

import java.util.List;
import java.util.Map;


/**
 * @author aanagnostopoulos
 * 
 */
public class DCInput extends org.dspace.app.util.DCInput {

    /** if the field is internationalizable */
    private boolean i18nable = false;
	
	public DCInput(Map<String, String> fieldMap,
			Map<String, List<String>> listMap) {

		super(fieldMap, listMap);
        
		// is i18nable ?
        String i18nableStr = fieldMap.get("i18n");
        i18nable = "true".equalsIgnoreCase(i18nableStr)
                || "yes".equalsIgnoreCase(i18nableStr);

	}
	
	public boolean isI18nable() {
		return i18nable;
	}

}
