package com.nforce.model;

import java.util.ArrayList;
import java.util.List;

public enum ClientState {
	
	ALIVE("Aktyvus", 1, false),
	DEAD("Neaktyvus", 2, false),
	COMATOSE("Tarpinis", 3, false),
	HIDDEN("Paslėptas", 4, true),
	SPECIAL("Ypatingas", 5, false);
	
	public String title;
	public int value;
	public boolean adminOnly;
	
	private ClientState(String title, int value, boolean adminOnly) {
		this.title = title;
		this.value = value;
		this.adminOnly = adminOnly;
	}
	
	public static ClientState[] values(boolean isAdmin) {
		List<ClientState> list = new ArrayList<ClientState>();
		for(ClientState value : values()) {
			if(value.adminOnly) {
				if(isAdmin) {
					list.add(value);
				}
			} else {
				list.add(value);
			}
		}
		return list.toArray(new ClientState[list.size()]);
	}
}
