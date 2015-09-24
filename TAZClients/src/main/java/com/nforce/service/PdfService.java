package com.nforce.service;

import com.nforce.Utils;
import com.nforce.model.Client;
import com.nforce.model.SiaCustomPdfParams;
import com.nforce.model.SiaPdfContext;
import com.nforce.pdf.ITextPdfCreator;
import org.apache.commons.lang3.StringUtils;

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

    public InputStream getProformaInvoicePdf(Client client, SiaCustomPdfParams params) {
        return creator.getPdf(fillFromClient(client, params), "sia.xhtml");
    }

    private SiaPdfContext fillFromClient(Client client, SiaCustomPdfParams params) {
        SiaPdfContext context = new SiaPdfContext();
        context.setAddress(client.getAddress());
        context.setCode(client.getCompanyCode());

        context.setCounter(String.valueOf(client.getId()));

        DateFormat format = Utils.getDateFormat("yyyy MM dd");
        context.setDate(format.format(new Date()));
        context.setName(client.getCompanyTitle());

        if(StringUtils.isNotBlank(client.getEmail())) {
            String recipients[] = client.getEmail().split(",");
            if (recipients != null && recipients.length > 0) {
                context.setEmail(recipients[0]);
            }
        }

        context.setPhone(client.getPhoneNumber());
        context.setPrice(params.getPrice());
        context.setJobTitle("Buhalterė");
        context.setResponsiblePersonName("Asta Driskiuvienė");

        Date startDate = null;
        if(client.getValidTo() == null) {
            startDate = new Date();
        } else {
            startDate = Utils.addMonths(client.getValidTo(), 1);
        }
        startDate = Utils.resetDate(startDate, Utils.TimeOfMonth.START);
        Date endDate = Utils.addMonths(startDate, 11);
        endDate = Utils.resetDate(endDate, Utils.TimeOfMonth.END);
        context.setPeriod("nuo " + format.format(startDate) + " iki " + format.format(endDate));

        return context;
    }
}
