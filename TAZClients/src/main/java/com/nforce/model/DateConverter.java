package com.nforce.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class DateConverter extends StringConverter<LocalDate> {
	
	public static final String pattern = "yyyy-MM-dd";
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
	
	@Override
	public LocalDate fromString(String string) {
		if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateFormatter);
        } else {
            return null;
        }
	}

	@Override
	public String toString(LocalDate date) {
		if (date != null) {
            return dateFormatter.format(date);
        } else {
            return "";
        }
	}
	
}
