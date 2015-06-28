package com.nforce.model;

import java.util.HashMap;
import java.util.Map;

public class StatisticsBean {

	private static StatisticsBean instance;

	private Map<String, Integer> files = new HashMap<>();

	private StatisticsBean() {
		// let's be singleton
	}

	static public StatisticsBean getInstance() {
		if (instance == null) {
			instance = new StatisticsBean();
		}
		return instance;
	}

	public void addFile(String fileName, String content) {
		files.put(fileName, getLinesCount(content));
	}

	private Integer getLinesCount(String content) {
		if (content == null) {
			return null;
		}
		String lines[] = content.split("\n");
		return lines.length;
	}

	public void printSummary() {
		int totalFiles = files.size();
		int totalLines = 0;
		for (String file : files.keySet()) {
			if (files.get(file) != null) {
				totalLines += files.get(file);
			}
		}
		System.out.println(String.format("Total %d files were analysed", totalFiles));
		System.out.println(String.format("Total %d lines were found", totalLines));
		System.out.println("------------------------------------------------------------------------");
	}

	public void clear() {
		files.clear();
	}
}
