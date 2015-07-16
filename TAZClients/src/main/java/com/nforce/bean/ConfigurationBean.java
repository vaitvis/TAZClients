package com.nforce.bean;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

@Singleton
public class ConfigurationBean {

	private static final String CONFIG_SMTP_HOST = "smtp.host";
	private static final String CONFIG_SMTP_USER = "smtp.user";
	private static final String CONFIG_SMTP_PASSWORD = "smtp.password";
	private static final String CONFIG_SMTP_FROM = "smtp.from";
	private static final String CONFIG_SMTP_FROM_NAME = "smtp.from.name";
	
	private String smtpHost;
	private String smtpUser;
	private String smtpPassword;
	private String smtpFrom;
	private String smtpFromName;
	
	public String getApiScheme() {
		return "http";
	}
	
	public String getApiHost() {
		return "taz.lt";
	}
	
	public String getApiAddress() {
		return "/api";
	}
	
	public String getSmtpHost() {
		return smtpHost;
	}
	
	public void setSmtpHost(String host) {
		this.smtpHost = host;
	}
	
	public String getSmtpUser() {
		return smtpUser;
	}
	
	public void setSmtpUser(String user) {
		this.smtpUser = user;
	}
	
	public String getSmtpPassword() {
		return smtpPassword;
	}
	
	public void setSmtpPassword(String password) {
		this.smtpPassword = password;
	}
	
	public String getSmtpFrom() {
		return smtpFrom;
	}
	
	public void setSmtpFrom(String from) {
		this.smtpFrom = from;
	}
	
	public String getSmtpFromName() {
		return smtpFromName;
	}

	public void setSmtpFromName(String smtpFromName) {
		this.smtpFromName = smtpFromName;
	}

	public void initSettings() {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration("tazclients.prop");
			config.load();
			smtpHost = config.getString(CONFIG_SMTP_HOST);
			smtpUser = config.getString(CONFIG_SMTP_USER);
			smtpPassword = config.getString(CONFIG_SMTP_PASSWORD);
			smtpFrom = config.getString(CONFIG_SMTP_FROM);
			smtpFromName = config.getString(CONFIG_SMTP_FROM_NAME);
		} catch (ConfigurationException e) {
		}
	}
	
	public boolean saveSettings() {
		File configFile = new File("tazclients.prop");
		try {
			configFile.createNewFile();
		} catch (IOException e1) {
			return false;
		}
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(configFile);
			config.setProperty(CONFIG_SMTP_HOST, smtpHost);
			config.setProperty(CONFIG_SMTP_USER, smtpUser);
			config.setProperty(CONFIG_SMTP_PASSWORD, smtpPassword);
			config.setProperty(CONFIG_SMTP_FROM, smtpFrom);
			config.setProperty(CONFIG_SMTP_FROM_NAME, smtpFromName);
			config.save();
		} catch (ConfigurationException e) {
			return false;
		}
		return true;
	}
}
