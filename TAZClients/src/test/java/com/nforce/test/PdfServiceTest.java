package com.nforce.test;

import com.nforce.model.Client;
import com.nforce.model.SiaCustomPdfParams;
import com.nforce.service.ClientsService;
import com.nforce.service.LoginService;
import com.nforce.service.PdfService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by nforce on 15.9.7.
 */
public class PdfServiceTest extends AbstractTest {

    @Inject
    private PdfService pdfService;

    @Inject
    private ClientsService clientsService;

    @Inject
    private LoginService loginService;

    private Client client;

    @Before
    public void setUp() {
        boolean loggedIn = loginService.login(getUserName(), getPassword());
        Assert.assertTrue(loggedIn);
        client = clientsService.get(1);
        Assert.assertNotNull(client);
        System.out.println(client);
    }

    @Test
    public void test() throws IOException {
        SiaCustomPdfParams params = new SiaCustomPdfParams();
        params.setPrice("55,90");
        InputStream inputStream = pdfService.getProformaInvoicePdf(client, params);
        Files.copy(inputStream, new File("testPdfService.pdf").toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @After
    public void tearDown() {
        loginService.logout();
    }
}
