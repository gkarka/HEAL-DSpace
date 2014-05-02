package ntua.lib.dspace.policy;

import java.util.Date;

/**
 * An interface representing an allow/deny rule (typically, an XML file), for
 * controlling item policies
 * 
 * @author anagnostopoulos
 * 
 */
public interface IRule {

	public static final String RULE_ALLOW = "allow";

	public static final String RULE_DENY = "deny";

	public static final String ATTR_IP = "ip";

	public static final String ATTR_DURATION = "duration";

	/**
	 * @return a String containing the rule type
	 */
	public String getRuleType();

	/**
	 * @return the IP range (in the form of eg. XXX.XXX.XXX.*, or *) that the
	 *         rule specifies
	 */
	public String getIp();

	/**
	 * @return the duration (in years) that this rule will be applied for,
	 *         starting from the item upload date.
	 */
	public Integer getDuration();

	/**
	 * @param ip
	 *            the remote IP address to check
	 * @return true, if allowed, based on the current rule. False, if not.
	 */
	public boolean isIpAllowed(String ip);

	/**
	 * Checks whether the rule should be applied or not, for a given item upload
	 * date. Only works if the rule has a duration specified (in years).
	 * 
	 * @param date
	 *            the initial date to start counting from.
	 * @return true, if allowed, based on the current rule. False, if not.
	 */
	public boolean isDateAllowed(Date date);

}
