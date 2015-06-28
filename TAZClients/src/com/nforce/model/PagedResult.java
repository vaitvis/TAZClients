package com.nforce.model;

public class PagedResult {

	private int totalRows;
	private Client[] rows;
	
	public int getTotalRows() {
		return totalRows;
	}
	
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public Client[] getRows() {
		return rows;
	}

	public void setRows(Client[] rows) {
		this.rows = rows;
	}
}
