package com.nforce;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
		List<String> list = new ArrayList<String>();
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
}
