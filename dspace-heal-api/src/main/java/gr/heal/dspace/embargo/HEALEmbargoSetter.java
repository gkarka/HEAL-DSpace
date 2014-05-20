/**
 * 
 */
package gr.heal.dspace.embargo;

import gr.heal.dspace.submit.step.ProcessMetadataStep;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.DCDate;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.MetadataSchema;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.embargo.DefaultEmbargoSetter;
import org.dspace.eperson.Group;
import org.dspace.license.CreativeCommons;

/**
 * @author aanagnostopoulos
 * 
 */
public class HEALEmbargoSetter extends DefaultEmbargoSetter {

	private String authenticatedGroupName = null;
	
	public HEALEmbargoSetter() {
		super();
		authenticatedGroupName = ConfigurationManager.getProperty("embargo.authenticated.groupName");
	}
	
	@Override
	public DCDate parseTerms(Context context, Item item, String terms)
			throws SQLException, AuthorizeException, IOException {

		if (terms != null && terms.length() > 0) {

			if (terms.equals("embargo")) {

				// impose a date based embargo, using the # of months specified
				// in dspace.cfg
				DCDate now = DCDate.getCurrent();
				DCDate dateAvailable = new DCDate(now.getYear(), now.getMonth(),
						now.getDay(), -1, -1, -1);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateAvailable.toDate());
				calendar.add(Calendar.MONTH, Integer.parseInt(termsOpen));
				return new DCDate(calendar.getTime());

			} else if (terms.equals("account")) {
				// only allow logged in users (i.e members of 'authenticated' group)
				Group group = Group.findByName(context, authenticatedGroupName);
				if (group != null) {
					for (Bundle bn : item.getBundles()) {
						// Skip the LICENSE and METADATA bundles, they stay
						// world-readable
						String bnn = bn.getName();
						if (!(bnn.equals(Constants.LICENSE_BUNDLE_NAME)
								|| bnn.equals(Constants.METADATA_BUNDLE_NAME) || bnn
									.equals(CreativeCommons.CC_BUNDLE_NAME))) {
							
							AuthorizeManager.removePoliciesActionFilter(
									context, bn, Constants.READ);
							AuthorizeManager.addPolicy(context, bn,
									Constants.READ, group);
							
							for (Bitstream bs : bn.getBitstreams()) {
								AuthorizeManager.removePoliciesActionFilter(
										context, bs, Constants.READ);
								AuthorizeManager.addPolicy(context, bs,
										Constants.READ, group);
							}
						}
					}
				}
				return null;
			}else if(terms.equals("campus")) {
				//TODO: implement IP based access
				return null;
			}else if(terms.equals("free")) {
				//always set heal.dateAvailable, since HEAL-meta XSD requires it 
				DCValue[] dateAvailable = item.getMetadata(MetadataSchema.DC_SCHEMA, "date", "available", null);
				DCDate date = null;
				if(dateAvailable.length>0) {
					date = new DCDate(dateAvailable[0].value);
				}
				
				return date;
			}
		}
		return null;

	}

}
