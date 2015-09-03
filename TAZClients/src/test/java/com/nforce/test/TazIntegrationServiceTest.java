package com.nforce.test;

import com.nforce.bean.SessionBean;
import com.nforce.service.LoginService;
import com.nforce.service.TazIntegrationService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

public class TazIntegrationServiceTest extends AbstractTest {

	@Inject LoginService loginService;

	@Inject
	TazIntegrationService tazIntegrationService;
	
	@Inject SessionBean sessionBean;
	
	@Before
	public void login() {
		boolean loggedIn = loginService.login(getUserName(), getPassword());
		Assert.assertTrue(loggedIn);
	}

	@Test
	public void test() {
		Integer id = tazIntegrationService.getProformaInvoiceId();
		Assert.assertNotNull(id);
		Assert.assertNotEquals(Integer.valueOf(0), id);
		System.out.println(id);
	}
	
	@After
	public void logout() {
		boolean loggedIn = sessionBean.isLoggedIn();
		Assert.assertTrue(loggedIn);
		loginService.logout();
	}
}
