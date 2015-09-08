package com.nforce.model;

public enum EventType {

	CLIENT_ADDED(1),
	CLIENT_UPDATED(2),
	MAIL_SENT(3),
	MAIL_FAILED(4),
	PROFORMA_INVOICE_SENT(5);
	
	int value;
	
	private EventType(int value) {
		this.value = value;
	}
}
