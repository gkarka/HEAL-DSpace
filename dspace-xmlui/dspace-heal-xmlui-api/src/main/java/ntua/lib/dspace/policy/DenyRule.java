package ntua.lib.dspace.policy;

import java.util.Date;

public class DenyRule extends Rule implements IRule {

	public DenyRule(String ip, Integer duration) {
		setIp(ip);
		setDuration(duration);
	}

	public String getRuleType() {
		return IRule.RULE_DENY;
	}

	public boolean isIpAllowed(String ip) {
		String allowRange = parseAllowRange();
		if (ip.startsWith(allowRange)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isDateAllowed(Date date) {
		if (getDuration() == null) {
			return true;
		}
		/* shift the supplied date by the number of years specified by duration */
		Date shiftedDate = shiftDateYear(date, getDuration());
		/*compare the shifted date with today's date*/
		Date today = new Date();
		if (shiftedDate.before(today)) {
			return false;
		}
		else {
			return true;
		}
	}
	}

	
