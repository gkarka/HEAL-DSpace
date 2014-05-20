/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package gr.heal.dspace.submit.step;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.mediafilter.FormatFilter;
import org.dspace.app.mediafilter.MediaFilterManager;
import org.dspace.app.util.SubmissionInfo;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.DCDate;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.MetadataSchema;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.core.PluginManager;
import org.dspace.core.SelfNamedPlugin;

/**
 * @author aanagnostopoulos
 * 
 */
public class ProcessMetadataStep extends DescribeStep {

	public static final String HEAL_SCHEMA = "heal";

	public static final String SOURCE_PREFIX = " (URL: ";

	public static final String SOURCE_SUFFIX = ")";

	public static final String SOURCE_DUMMY = "**N/A**-";

	public static final String DEFAULT_LICENSE = "Default License";
	
	/** log4j logger */
	private static Logger log = Logger.getLogger(ProcessMetadataStep.class);

	private static Map<String, List<String>> filterFormats = null;

	private static void getEnabledFullTextCapableFilterFormats() {

		filterFormats = new HashMap<String, List<String>>();

		// retrieve list of all enabled, full-text extraction capable, media
		// filter plugins!
		String enabledPlugins = ConfigurationManager
				.getProperty(MediaFilterManager.MEDIA_FILTER_PLUGINS_KEY);
		String[] filterNames = enabledPlugins.split(",\\s*");

		// set up each filter
		for (int i = 0; i < filterNames.length; i++) {
			FormatFilter filter = (FormatFilter) PluginManager.getNamedPlugin(
					FormatFilter.class, filterNames[i]);

			// only process filters having "TEXT" as their target bundle
			if (filter.getBundleName().equals("TEXT")) {

				String filterClassName = filter.getClass().getName();

				String pluginName = null;

				if (SelfNamedPlugin.class.isAssignableFrom(filter.getClass())) {
					// Get the plugin instance name for this class
					pluginName = ((SelfNamedPlugin) filter)
							.getPluginInstanceName();
				}

				// Retrieve our list of supported formats from dspace.cfg
				String formats = ConfigurationManager
						.getProperty(MediaFilterManager.FILTER_PREFIX + "."
								+ filterClassName
								+ (pluginName != null ? "." + pluginName : "")
								+ "." + MediaFilterManager.INPUT_FORMATS_SUFFIX);

				// add to internal map of filters to supported formats
				if (formats != null) {
					filterFormats.put(filterNames[i],
							Arrays.asList(formats.split(",[\\s]*")));
				}
			}
		}

		// // retrieve list of all full-text extraction capable media filter
		// plugins!
		// String fullTextCapablePlugins = ConfigurationManager
		// .getProperty(MediaFilterManager.MEDIA_FILTER_PLUGINS_KEY);
		// String[] fullTextCapableFilterNames =
		// fullTextCapablePlugins.split(",\\s*");
		//
		// for(String fulllTextCapableFilterName: fullTextCapableFilterNames) {
		// filterFormats.remove(fulllTextCapableFilterName);
		// }

	}

	private void clearLanguageAttribute(Item item, String element,
			String qualifier) {
		DCValue[] values = item.getMetadata(HEAL_SCHEMA, element, qualifier,
				Item.ANY);
		item.clearMetadata(HEAL_SCHEMA, element, qualifier, Item.ANY);
		for (DCValue value : values) {
			item.addMetadata(HEAL_SCHEMA, element, qualifier, null, value.value);
		}
	}

	public ProcessMetadataStep() throws ServletException {
		// implicitly initialize the current DCInputsReader
		super();

		if (filterFormats == null) {
			getEnabledFullTextCapableFilterFormats();
		}

	}

	@Override
	public int doProcessing(Context context, HttpServletRequest request,
			HttpServletResponse response, SubmissionInfo subInfo)
			throws ServletException, IOException, SQLException,
			AuthorizeException {

		// get the item
		Item item = subInfo.getSubmissionItem().getItem();

		// add system required DC metadata, by copying the corresponding HEAL
		// values. Delete the HEAL values, to avoid redundant data (especially when using OAI) 
		DCValue[] healTitles = item.getMetadata("heal.title");
		item.clearMetadata(HEAL_SCHEMA, "title", null, Item.ANY);
		for (DCValue healTitle : healTitles) {
			item.addMetadata(MetadataSchema.DC_SCHEMA, "title", null,
					healTitle.language, healTitle.value);
		}

		DCValue[] healCreatorNames = item.getMetadata("heal.creatorName");
		item.clearMetadata(HEAL_SCHEMA, "creatorName", null, Item.ANY);
		for (DCValue healCreatorName : healCreatorNames) {
			item.addMetadata(MetadataSchema.DC_SCHEMA, "contributor", "author",
					healCreatorName.language, healCreatorName.value);
		}

		/* tweak elements, to make them adhere to the HEAL XML schema */

		// heal:type
		clearLanguageAttribute(item, "type", null);

		// heal:classification & heal:classificationURI: split and create, if
		// classification comes from an external schema
		DCValue[] values = item.getMetadata(HEAL_SCHEMA, "classification", null,
				Item.ANY);
		item.clearMetadata(HEAL_SCHEMA, "classification", null, Item.ANY);
		for (DCValue dcValue : values) {
			String value = null;
			String uri = null;
			if (dcValue.value.contains(SOURCE_PREFIX)) {
				try {
				value = dcValue.value.substring(0,
						dcValue.value.lastIndexOf(SOURCE_PREFIX));
				uri = dcValue.value.substring(
						(dcValue.value.lastIndexOf(SOURCE_PREFIX)+SOURCE_PREFIX.length()-1),
						dcValue.value.lastIndexOf(SOURCE_SUFFIX));
				}catch(Exception e) { //handle any bad user input
					value = dcValue.value;
//					uri = SOURCE_DUMMY + dcValue.value;
				}
				
			} else {
				value = dcValue.value;
//				uri = SOURCE_DUMMY + dcValue.value;
			}
			item.addMetadata(HEAL_SCHEMA, "classification", null, dcValue.language,
					value);
			if(uri!=null) {
				item.addMetadata(HEAL_SCHEMA, "classificationURI", null, null, uri);
			}
		}


		// heal:keyword & heal:keywordURI: split and create, if keyword comes
		// from an external schema
		values = item.getMetadata(MetadataSchema.DC_SCHEMA, "subject", null,
				Item.ANY);
		item.clearMetadata(MetadataSchema.DC_SCHEMA, "subject", null, Item.ANY);
		for (DCValue dcValue : values) {
			String value = null;
			String uri = null;
			if (dcValue.value.contains(SOURCE_PREFIX)) {
				try {				
					value = dcValue.value.substring(0,
							dcValue.value.lastIndexOf(SOURCE_PREFIX));
					uri = dcValue.value.substring(
							(dcValue.value.lastIndexOf(SOURCE_PREFIX)+SOURCE_PREFIX.length()-1),
							dcValue.value.lastIndexOf(SOURCE_SUFFIX));
				}catch(Exception e) { //handle any bad user input
					value = dcValue.value;
				}

			} else {
				value = dcValue.value;
//				uri = SOURCE_DUMMY + dcValue.value;
			}
			// add keyword to DC schema, to preserver system integrity
			item.addMetadata(MetadataSchema.DC_SCHEMA, "subject", null,
					dcValue.language, value);
			if(uri!=null) {
				item.addMetadata(HEAL_SCHEMA, "keywordURI", null, null, uri);
			}
		}

		// heal:contributorID
		// TODO in the future

		// heal:identifierSecondary
		clearLanguageAttribute(item, "identifier", "secondary");

		// heal:language
		clearLanguageAttribute(item, "language", null);

		// heal:access
		clearLanguageAttribute(item, "access", null);

		// heal:accessText
		// TODO implement?

		// dc:rights
		// TODO implement?
		DCValue[] dcValues = item.getMetadata(MetadataSchema.DC_SCHEMA, "rights", null, Item.ANY);
		if(dcValues.length==0) {
			item.addMetadata(MetadataSchema.DC_SCHEMA, "rights", null, null, DEFAULT_LICENSE);
		}

		// heal:references
		// TODO implement?

		// heal:referencedBy
		// TODO implement?

		// heal:advisorID
		// TODO in the future

		// heal:committeeMemberID
		// TODO in the future

		// heal:academicPublisherID
		clearLanguageAttribute(item, "academicPublisherID", null);

		// heal:numberOfPages
		clearLanguageAttribute(item, "numberOfPages", null);

		// heal:journalType
		clearLanguageAttribute(item, "journalType", null);

		// heal:fullTextAvailability
		/*
		 * can't know for sure if full text will be available, since filter
		 * hasn't run yet. Try to determine based on bitstreams in ORIGINAL
		 * bundle, as well as on enabled, full-text extraction capable, media
		 * filters
		 */
		boolean hasFullTextAvailability = false;
		Bundle[] bundles = item.getBundles("ORIGINAL");
		for (Bundle bundle : bundles) {
			Bitstream[] bitstreams = bundle.getBitstreams();
			for (Bitstream bitstream : bitstreams) {
				for (String filter : filterFormats.keySet()) {
					List<String> formats = filterFormats.get(filter);
					// one of the bitstreams matching a full-text capable
					// enabled filter will suffice
					if (formats.contains(bitstream.getFormat()
							.getShortDescription())) {
						hasFullTextAvailability = true;
						break;
					}
				}
			}
		}
		if (hasFullTextAvailability) {
			item.addMetadata(HEAL_SCHEMA, "fullTextAvailability", null, null,
					"true");
		}else {
			item.addMetadata(HEAL_SCHEMA, "fullTextAvailability", null, null,
					"false");
		}

		// heal:conferenceItemType
		clearLanguageAttribute(item, "conferenceItemType", null);

		// Save changes to database
		subInfo.getSubmissionItem().update();

		// commit changes
		context.commit();

		// completed without errors
		return STATUS_COMPLETE;
	}

	@Override
	public int getNumberOfPages(HttpServletRequest request,
			SubmissionInfo subInfo) throws ServletException {
		// This class represents a non-interactive processing step
		// (so it should only be processed once!)
		return 1;

	}

}
