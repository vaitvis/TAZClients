package com.nforce.importer;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public abstract class Parser {
	protected String dateFormat = "yyyy.MM.dd";

	public abstract boolean next();

	public abstract Object getField(String name);

	public abstract Map<String, Object> getFields();

	public abstract boolean close();

	public Parser setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	public String getString(String name) {
		Object value = getField(name);
		if (value != null) {
			return value.toString();
		}
		return null;
	}

	public Date getDate(String fieldName) {
		try {
			Object field = getField(fieldName);
			if (field instanceof Date) {
				return (Date) field;
			} else if (field instanceof String && StringUtils.isNotBlank(field.toString())) {
				return Utils.getDateFormat(dateFormat).parse((String) field);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer getInteger(String fieldName) {
		return Integer.parseInt(getField(fieldName).toString());
	}

	public String getNumber(String fieldName) {
		return getField(fieldName).toString().replaceAll("[^0-9]*", "");
	}

	public boolean isEmpty(String fieldName) {
		String field = getField(fieldName).toString();
		return field == null || field.isEmpty();
	}

	public boolean hasValue(String fieldName) {
		return getField(fieldName) != null;
	}
}
