package com.nforce;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
	
	/**
	 * The epic PHP exploder port to JAVA
	 *
	 * @param string
	 * @param delimeter
	 * @return the array of exploded string as it should be
	 */
	public static String[] explode(String string, String delimeter) {
		if (string == null) {
			return null;
		}
		List<String> list = new ArrayList<>();
		String target = string;
		int tokenIndex = target.indexOf(delimeter);
		if (tokenIndex == -1) {
			return Arrays.asList(string).toArray(new String[0]);
		}
		while (tokenIndex != -1) {
			list.add(target.substring(0, tokenIndex));
			target = target.substring(tokenIndex + 1);
			tokenIndex = target.indexOf(delimeter);
		}
		list.add(target);
		return list.toArray(new String[list.size()]);
	}
	
	public static DateFormat getDateFormat(String format) {
		return new SimpleDateFormat(format, new Locale("lt", "LT"));
	}

	public enum TimeOfMonth {
		START,
		END
	}

	public static Date resetDate(Date startDate, TimeOfMonth time) {
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		switch (time) {
			case START:
				c.set(Calendar.DAY_OF_MONTH, 1);
				break;
			case END:
				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
				break;
		}
		return c.getTime();
	}

	public static Date addMonths(Date startDate, int toAdd) {
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.MONTH, toAdd);
		return c.getTime();
	}
}
