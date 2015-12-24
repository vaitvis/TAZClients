package com.nforce.test;

import com.nforce.bean.SessionBean;
import com.nforce.model.ClientState;
import com.nforce.service.LoginService;
import com.nforce.service.StatsService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by nforce on 15.12.24.
 */
public class StatsServiceTest extends AbstractTest {

	@Inject
    LoginService loginService;

    @Inject
    StatsService statsService;

	@Inject
    SessionBean sessionBean;

    @Before
    public void setUp() {
        boolean loggedIn = loginService.login(getUserName(), getPassword());
		Assert.assertTrue(loggedIn);
    }

    @Test
    public void testCountClientsByState() {
        Integer count = statsService.countClientsByState(null);
        Assert.assertNotNull(count);
        Assert.assertTrue(count > 0);
        System.out.println(count);
        count = statsService.countClientsByState(ClientState.ALIVE);
        Assert.assertNotNull(count);
        Assert.assertTrue(count > 0);
        System.out.println(count);
    }

    @Test
    public void testCountClientsByDateState() {
        Integer count = statsService.countClientsByDateState(ClientState.ALIVE, new Date(), null);
        Assert.assertNotNull(count);
        Assert.assertTrue(count > 0);
        System.out.println(count);
        count = statsService.countClientsByDateState(ClientState.ALIVE, null, new Date());
        Assert.assertNotNull(count);
        Assert.assertTrue(count > 0);
        System.out.println(count);
    }

    @After
    public void tearDown() {
        boolean loggedIn = sessionBean.isLoggedIn();
		Assert.assertTrue(loggedIn);
		loginService.logout();
    }
}
