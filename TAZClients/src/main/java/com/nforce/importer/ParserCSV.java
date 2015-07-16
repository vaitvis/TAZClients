package com.nforce.importer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ParserCSV extends Parser {
	protected String delimeter = "\t";

	private HashMap<String, Integer> FORMAT = new HashMap<String, Integer>();

	BufferedReader reader = null;

	String[] fields = null;

	public ParserCSV(String filePath, String delimeter) {
		try {
			this.delimeter = delimeter;
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ParserCSV(InputStream stream) {
		reader = new BufferedReader(new InputStreamReader(stream));
		init();
	}

	public void init() {
		try {
			FORMAT.clear();
			String format_line = readLine();
			String[] format_array = Utils.explode(format_line, delimeter);
			System.out.println(format_line);
			int i = 0;
			for (String field : format_array) {
				FORMAT.put(field, i++);
			}
			System.out.println(FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ParserCSV setDelimeter(String delimeter) {
		this.delimeter = delimeter;
		init();
		return this;
	}

	public String readLine() {
		try {
			return reader.readLine();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean next() {
		String line = readLine();
		fields = Utils.explode(line, delimeter);
		return fields != null && fields.length > 0;
	}

	@Override
	public String getField(String fieldName) {
		return fields[FORMAT.get(fieldName)].trim().replaceAll("\"\"", "\"");
	}

	@Override
	public Map<String, Object> getFields() {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Entry<String, Integer> entry : FORMAT.entrySet()) {
			map.put(entry.getKey(), fields[entry.getValue()]);
		}
		return map.size() > 0 ? map : null;
	}

	@Override
	public boolean close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println("-PARSER TEST-");
		ParserCSV test = new ParserCSV("./sarasas-vyteniui.csv", ";");
		test.close();
	}
}
