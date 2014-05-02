package ntua.lib.dspace;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ntua.lib.dspace.policy.AllowRule;
import ntua.lib.dspace.policy.DenyRule;
import ntua.lib.dspace.policy.IPolicy;
import ntua.lib.dspace.policy.IRule;
import ntua.lib.dspace.policy.Policy;
import ntua.lib.dspace.policy.Rule;

public class PolicyUtil {

	public static boolean isAccessAuthorized(IPolicy policy, String ip,
			Date initialDate) {

		boolean isAccessAthorized = false;

		List rules = policy.getRules();
		Iterator it = rules.iterator();
		while (it.hasNext()) {
			IRule rule = (IRule) it.next();
			boolean isIpAllowed = rule.isIpAllowed(ip);
			boolean isDateAllowed = rule.isDateAllowed(initialDate);

			boolean ruleResult = isIpAllowed & isDateAllowed;

			isAccessAthorized = isAccessAthorized | ruleResult;

		}

		return isAccessAthorized;
	}

	/**
	 * Parses an XML input Stream and creates Policy business object
	 * 
	 * @param policyStream
	 *            the XML to parse
	 * @return a policy object, containing the rules (if any), or null if a
	 *         parsing error occurs.
	 */
	public static IPolicy parsePolicyXml(InputStream policyStream) {

		IPolicy policy = null;
		String policyId = null;
		List rules = null;

		try {
			Document policyDocument = javax.xml.parsers.DocumentBuilderFactory
					.newInstance().newDocumentBuilder().parse(policyStream);

			Element elmPolicy = (Element) policyDocument.getElementsByTagName(
					"policy").item(0);

			policyId = elmPolicy.getAttribute(IPolicy.ATTR_ID);

			if (policyId == null || policyId.equals("")) {
				return null;
			}

			NodeList childNodeList = elmPolicy.getChildNodes();
			if (childNodeList.getLength() <= 0) {
				return null;
			}

			rules = new ArrayList();

			loop: for (int i = 0; i < childNodeList.getLength(); i++) {
				Node node = childNodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					processRule(element, rules);
					continue loop;
				}
			}

		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}

		policy = new Policy(policyId, rules);
		return policy;

	}

	private static void processRule(Element element, List rules) {
		String ip = element.getAttribute(IRule.ATTR_IP);
		Integer duration = null;
		try {
			duration = new Integer(element.getAttribute(IRule.ATTR_DURATION));
		} catch (NumberFormatException e) {
		}
		IRule rule = null;
		if (element.getNodeName().equalsIgnoreCase(IRule.RULE_DENY)) {
			rule = new DenyRule(ip, duration);
		} else if (element.getNodeName().equalsIgnoreCase(IRule.RULE_ALLOW)) {
			rule = new AllowRule(ip, duration);
		}

		rules.add(rule);
		return;

	}

}
