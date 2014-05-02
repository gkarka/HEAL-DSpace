package ntua.lib.dspace.policy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AllowRule extends Rule implements IRule {

	public AllowRule(String ip, Integer duration) {
		setIp(ip);
		setDuration(duration);
	}

	public String getRuleType() {
		return IRule.RULE_ALLOW;
	}

	public boolean isIpAllowed(String ip) {
		String allowRange = parseAllowRange();
		if (ip.startsWith(allowRange)) {
			return true;
		} else {
			return false;
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
			return true;
		}
		else {
			return false;
		}
	}
	

}
