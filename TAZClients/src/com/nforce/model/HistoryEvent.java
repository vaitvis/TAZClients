package com.nforce.model;

import java.util.Date;

public class HistoryEvent {

	private Integer id;
	private String user;
	private Client client;
	private Date date;
	private EventType eventType;
	private String comment;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
}
