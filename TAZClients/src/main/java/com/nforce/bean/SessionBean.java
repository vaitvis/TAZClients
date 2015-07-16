package com.nforce.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import com.nforce.model.LoginData;
import com.nforce.model.SessionListener;

@Singleton
public class SessionBean {
	
	private LoginData loginData;
	private String userName;
	private List<SessionListener> listeners = new ArrayList<SessionListener>();	
	
	public boolean isLoggedIn() {
		return loginData != null && StringUtils.isNotBlank(loginData.getTicket());
	}
	
	public void init(LoginData loginData, String userName) {
		this.loginData = loginData;
		this.userName = userName;
	}
	
	public void clear() {
		loginData = null;
	}
	
	public String getTicket() {
		if(!isLoggedIn()) {
			return null;
		}
		return loginData.getTicket();
	}
	
	public Date getExpires() {
		if(!isLoggedIn()) {
			return null;
		}
		return loginData.getExpires();
	}
	
	public void setExpires(Date expires) {
		if(isLoggedIn()) {
			loginData.setExpires(expires);
		}
	}

	public String getUserName() {
		return userName;
	}
	
	public boolean isAdmin() {
		if(!isLoggedIn()) {
			return false;
		}
		return "admin".equals(loginData.getRole());
	}
	
	public void sessionStateChanged() {
		for(SessionListener l : listeners) {
			l.sessionStateChanged();
		}
	}
	
	public void addSessionListener(SessionListener listener) {
		listeners.add(listener);
	}
}
