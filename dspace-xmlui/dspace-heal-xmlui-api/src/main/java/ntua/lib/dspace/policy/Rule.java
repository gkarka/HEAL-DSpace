package ntua.lib.dspace.policy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class Rule {

	private String ip;

	private Integer duration;

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	protected String parseAllowRange() {
		int asteriskIndex = getIp().indexOf("*");
		if (asteriskIndex == -1) {
			asteriskIndex = getIp().length() - 1;
		}
		String allowRange = null;
		try {
			allowRange = ip.substring(0, asteriskIndex);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			allowRange = "";
		}
		return allowRange;

	}

	protected Date shiftDateYear(Date date, Integer years) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		System.out.println(calendar.getTime());
		calendar.roll(GregorianCalendar.YEAR, years.intValue());
		System.out.println(calendar.getTime());
		return calendar.getTime();
	}

	
	
}
