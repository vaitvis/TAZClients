package com.nforce.service;

import com.nforce.Utils;
import com.nforce.model.Client;
import com.nforce.model.SiaPdfContext;
import com.nforce.pdf.ITextPdfCreator;

import javax.inject.Inject;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nforce on 15.9.7.
 */
public class PdfService {

    @Inject
    private ITextPdfCreator creator;

    @Inject
    private TazIntegrationService tazIntegrationService;

    public InputStream getProformaInvoicePdf(Client client) {
        return creator.getPdf(fillFromClient(client), "sia.xhtml");
    }

    private SiaPdfContext fillFromClient(Client client) {
        SiaPdfContext context = new SiaPdfContext();
        context.setAddress(client.getAddress());
        context.setCode(client.getCompanyCode());

        context.setCounter(String.valueOf(tazIntegrationService.getProformaInvoiceId()));

        DateFormat format = Utils.getDateFormat("yyyy MM dd");
        context.setDate(format.format(new Date()));
        context.setName(client.getCompanyTitle());
        context.setEmail(client.getEmail());
        context.setPhone(client.getPhoneNumber());
        context.setPrice("55,90");

        return context;
    }
}
