package com.nforce.model;

public class Page {

	private int startAt;
	private int size;
	
	public Page(int startAt, int size) {
		this.startAt = startAt;
		this.size = size;
	}
	
	public int getStartAt() {
		return startAt;
	}
	
	public int getSize() {
		return size;
	}
}
