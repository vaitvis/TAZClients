package com.ernesta.rules;

import org.apache.commons.lang3.StringUtils;

import com.ernesta.model.PersonRule;

public class FirstNameContractionRule implements PersonRule {

	@Override
	public boolean accept(String first, String second) {
		String firstFirstName = getFirstName(first);
		String firstLastName = getLastName(first);
		String secondFirstName = getFirstName(second);
		String secondLastName = getLastName(second);
		if (StringUtils.isNoneBlank(firstFirstName, firstLastName, secondFirstName, secondLastName)) {
			if (firstLastName.equals(secondLastName)) {
				if (firstFirstName.endsWith(".") || secondFirstName.endsWith(".")) {
					String shorterFirstName = (firstFirstName.length() < secondFirstName.length()) ? firstFirstName : secondFirstName;
					String longerFirstName = (firstFirstName.length() < secondFirstName.length()) ? secondFirstName : firstFirstName;
					if (longerFirstName.endsWith(".")) {
						return false;
					}
					if (longerFirstName.contains(shorterFirstName.replace(".", ""))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private String getFirstName(String name) {
		String parts[] = StringUtils.split(name.toLowerCase());
		if (parts.length <= 1) {
			return null;
		}
		return parts[0];
	}

	private String getLastName(String name) {
		String parts[] = StringUtils.split(name.toLowerCase());
		if (parts.length <= 1) {
			return null;
		}
		return parts[parts.length - 1];
	}
}
