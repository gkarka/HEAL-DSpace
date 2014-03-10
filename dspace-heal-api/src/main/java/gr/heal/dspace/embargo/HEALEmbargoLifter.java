/**
 * 
 */
package gr.heal.dspace.embargo;

import gr.heal.dspace.submit.step.ProcessMetadataStep;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DCDate;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.embargo.DefaultEmbargoLifter;

/**
 * @author aanagnostopoulos
 *
 */
public class HEALEmbargoLifter extends DefaultEmbargoLifter {

	private static Log log = LogFactory.getLog(HEALEmbargoLifter.class);
	
	@Override
	public void liftEmbargo(Context context, Item item) throws SQLException,
			AuthorizeException, IOException {

		super.liftEmbargo(context, item);
		
		//set 'heal.access' field value to 'free', since default polices are enforced by the lifter
		item.clearMetadata("heal", "access", null, Item.ANY);
		item.addMetadata("heal", "access", null, ConfigurationManager.getProperty("default.language"), "free");
		
		item.clearMetadata("heal", "dateAvailable", null, Item.ANY);
		DCValue[] dcDateIssuedValues = item.getMetadata("dc.date.issued");
		if (dcDateIssuedValues.length == 1) {// set the date to the existing
												// dc.date.issued value

			item.addMetadata(ProcessMetadataStep.HEAL_SCHEMA, "dateAvailable", null, null,
					dcDateIssuedValues[0].value);

		} else { // set date available to the current date (essentially same as
					// dc.date.issued that will be set during the item
					// installation)
			DCDate now = DCDate.getCurrent();
			DCDate dateAvailable = new DCDate(now.getYear(), now.getMonth(),
					now.getDay(), -1, -1, -1);
			item.addMetadata(ProcessMetadataStep.HEAL_SCHEMA, "dateAvailable", null, null,
					dateAvailable.toString());
		}
		
		// Save changes to database
		item.update();

		// commit changes
		context.commit();
		
	}
	
}
