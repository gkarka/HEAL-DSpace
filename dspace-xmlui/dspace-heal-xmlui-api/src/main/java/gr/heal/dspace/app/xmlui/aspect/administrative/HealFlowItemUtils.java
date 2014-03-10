/**
 * 
 */
package gr.heal.dspace.app.xmlui.aspect.administrative;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.cocoon.environment.Request;
import org.apache.commons.lang.StringUtils;
import org.dspace.app.xmlui.aspect.administrative.FlowItemUtils;
import org.dspace.app.xmlui.aspect.administrative.FlowResult;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Item;
import org.dspace.content.MetadataField;
import org.dspace.content.MetadataSchema;
import org.dspace.content.authority.Choices;
import org.dspace.core.Context;

/**
 * @author aanagnostopoulos
 *
 */
public class HealFlowItemUtils extends FlowItemUtils {

	private static final Message T_metadata_updated = new Message("default","The Item's metadata was successfully updated.");
	private static final Message T_metadata_added = new Message("default","New metadata was added.");
	
	public static FlowResult processAddMetadata(Context context, int itemID, Request request) throws SQLException, AuthorizeException, UIException, IOException
	{
		FlowResult result = new FlowResult();
		result.setContinue(false);

		Item item = Item.find(context, itemID);
		
		
		String fieldID = request.getParameter("field");
		String value = request.getParameter("value");

		// modified by aanagnostopoulos: trim language value to null, to avoid empty string values
		String language = StringUtils.trimToNull(request.getParameter("language"));
		// END aanagnostopoulos
		
		MetadataField field = MetadataField.find(context,Integer.valueOf(fieldID));
		MetadataSchema schema = MetadataSchema.find(context,field.getSchemaID());
		
		item.addMetadata(schema.getName(), field.getElement(), field.getQualifier(), language, value);
		
		item.update();
		context.commit();
		
		result.setContinue(true);
		
		result.setOutcome(true);
		result.setMessage(T_metadata_added);
		
		return result;
	}

	
	public static FlowResult processEditItem(Context context, int itemID, Request request) throws SQLException, AuthorizeException, UIException, IOException
	{
		FlowResult result = new FlowResult();
		result.setContinue(false);

		Item item = Item.find(context, itemID);
		
		
		// STEP 1:
		// Clear all metadata within the scope
		// Only metadata values within this scope will be considered. This
		// is so ajax request can operate on only a subset of the values.
		String scope = request.getParameter("scope");
		if ("*".equals(scope))
		{
			item.clearMetadata(Item.ANY, Item.ANY, Item.ANY, Item.ANY);
		}
		else
		{
			String[] parts = parseName(scope);
			item.clearMetadata(parts[0],parts[1],parts[2],Item.ANY);
		}
		
		// STEP 2:
		// First determine all the metadata fields that are within
		// the scope parameter
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		Enumeration parameters = request.getParameterNames();
		while(parameters.hasMoreElements())
		{

			// Only consider the name_ fields
			String parameterName = (String) parameters.nextElement();
			if (parameterName.startsWith("name_"))
			{
				// Check if the name is within the scope
				String parameterValue = request.getParameter(parameterName);
				if ("*".equals(scope) || scope.equals(parameterValue))
				{
					// Extract the index from the name.
					String indexString = parameterName.substring("name_".length());
					Integer index = Integer.valueOf(indexString);
					indexes.add(index);
				}
			}
		}
		
		// STEP 3:
		// Iterate over all the indexes within the scope and add them back in.
		for (Integer index=1; index <= indexes.size(); ++index)
		{
			String name = request.getParameter("name_"+index);
			String value = request.getParameter("value_"+index);
                        String authority = request.getParameter("value_"+index+"_authority");
                        String confidence = request.getParameter("value_"+index+"_confidence");
            // modified by aanagnostopoulos: trim language value to null, to avoid empty string values
			String lang = StringUtils.trimToNull(request.getParameter("language_"+index));
			// END aanagnostopoulos
			String remove = request.getParameter("remove_"+index);
			
			// the user selected the remove checkbox.
			if (remove != null)
                        {
				continue;
                        }
			
			// get the field's name broken up
			String[] parts = parseName(name);
			
                        // probe for a confidence value
                        int iconf = Choices.CF_UNSET;
                        if (confidence != null && confidence.length() > 0)
                        {
                            iconf = Choices.getConfidenceValue(confidence);
                        }
                        // upgrade to a minimum of NOVALUE if there IS an authority key
                        if (authority != null && authority.length() > 0 && iconf == Choices.CF_UNSET)
                        {
                            iconf = Choices.CF_NOVALUE;
                        }
                        item.addMetadata(parts[0], parts[1], parts[2], lang,
                                             value, authority, iconf);
		}
		
		item.update();
		context.commit();
		
		result.setContinue(true);
		
		result.setOutcome(true);
		result.setMessage(T_metadata_updated);
		
		return result;
	}

	private static String[] parseName(String name) throws UIException
	{
		String[] parts = new String[3];
		
		String[] split = name.split("_");
		if (split.length == 2) {
			parts[0] = split[0];
			parts[1] = split[1];
			parts[2] = null;
		} else if (split.length == 3) {
			parts[0] = split[0];
			parts[1] = split[1];
			parts[2] = split[2];
		} else {
			throw new UIException("Unable to parse metedata field name: "+name);
		}
		return parts;
	}

}
