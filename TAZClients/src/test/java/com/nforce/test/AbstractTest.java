package com.nforce.test;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(TazTestRunner.class)
public abstract class AbstractTest {

	protected PropertiesConfiguration config;
	
	@Before
	public void init() throws ConfigurationException {
		config = new PropertiesConfiguration("test.prop");
		config.load();
	}
	
	protected String getUserName() {
		return config.getString("userName");
	}
	
	protected String getPassword() {
		return config.getString("password");
	}
}
