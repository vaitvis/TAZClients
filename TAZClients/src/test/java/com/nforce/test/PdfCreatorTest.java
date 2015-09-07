package com.nforce.test;

import com.nforce.model.SiaPdfContext;
import com.nforce.pdf.ITextPdfCreator;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by nforce on 15.7.19.
 */
public class PdfCreatorTest extends AbstractTest {

    @Inject
    private ITextPdfCreator creator;

    private SiaPdfContext context;

    @Before
    public void setUpPdfContext() {
        context = new SiaPdfContext();
        context.setAddress("Vasaros g. 5, Kaunas");
        context.setCode("31234567890");
        context.setCounter("1");
        context.setDate("2007 05 11");
        context.setName("Vardenis Pavardenis");
        context.setEmail("vardenis.pavardenis@test.lt");
        context.setPhone("+37061234567");
        context.setPrice("55,90");
        context.setPeriod("nuo 2007 05 11 iki 2008 05 10");
        context.setJobTitle("Buhalterė");
        context.setResponsiblePersonName("Vaida Vaidaitė");
    }

    @Test
    public void testGetPdf() throws Exception {
        InputStream inputStream = creator.getPdf(context, "sia.xhtml");
        Files.copy(inputStream, new File("test.pdf").toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}