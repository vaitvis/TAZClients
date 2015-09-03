package com.nforce.test;

import com.nforce.bean.ConfigurationBean;
import com.nforce.service.EmailService;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Created by nforce on 15.8.19.
 */
public class EmailServiceTest extends AbstractTest {

    @Inject
    private EmailService emailService;

    @Inject
    private ConfigurationBean configurationBean;

    @Before
    public void setUp() {
        configurationBean.initSettings();
        configurationBean.setSmtpFrom("test@test.com");
    }

    @Test
    public void testMultipleSend() {
        emailService.sendMail("test@test.com", "Test subject", "Test message");
    }
}
