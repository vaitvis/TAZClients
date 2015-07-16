package com.nforce.model;

import java.util.Date;

public class LoginData {

	private String ticket;
	private String role;
	private Date expires;
	
	public String getTicket() {
		return ticket;
	}
	
	public String getRole() {
		return role;
	}
	
	public Date getExpires() {
		return expires;
	}
	
	public void setExpires(Date expires) {
		this.expires = expires;
	}
}
