package com.nforce.test;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.nforce.bean.SessionBean;
import com.nforce.service.LoginService;

public class LoginServiceTest extends AbstractTest {

	@Inject LoginService loginService;
	
	@Inject SessionBean sessionBean;
	
	@Test
	public void testLoginProcedure() {
		boolean loggedIn = loginService.login(getUserName(), getPassword());
		Assert.assertTrue(loggedIn);
	}
	
	@Test
	public void testLogoutProcedure() {
		boolean loggedIn = sessionBean.isLoggedIn();
		Assert.assertTrue(loggedIn);
		loginService.logout();
	}
}
