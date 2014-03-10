/**
 * Copyright (C) 2011 SeDiCI <info@sedici.unlp.edu.ar>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gr.heal.dspace.app.xmlui.aspect.submission;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Locale;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.PageMeta;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.I18nUtil;
import org.xml.sax.SAXException;

/**
 * Adds supported locales to the pageMeta
 * 
 * @author Nestor Oviedo
 */
public class SupportedLocales extends AbstractDSpaceTransformer implements
		CacheableProcessingComponent {

	/**
	 * Generate the cache validity object.
	 */
	public SourceValidity getValidity() {
		return NOPValidity.SHARED_INSTANCE;
	}

	public void addPageMeta(PageMeta pageMeta) throws SAXException,
			WingException, UIException, SQLException, IOException,
			AuthorizeException {
		// Adds supported locales from metadata.languages config property
		String supportedLocalesString = ConfigurationManager
				.getProperty("metadata.languages");
		Locale[] locales = I18nUtil.parseLocales(supportedLocalesString);
		for (Locale locale : locales) {
			pageMeta.addMetadata("page", "supported_locale").addContent(locale.toString());
        	// now add the appropriate labels
        	pageMeta.addMetadata("supported_locale", locale.toString()).addContent(locale.getDisplayName(locale));
		}
	}

	@Override
	public Serializable getKey() {
		return "1";
	}
}
