package com.nforce.pdf;

import com.nforce.model.PdfContext;
import org.apache.fop.apps.MimeConstants;
import org.docbag.*;
import org.docbag.template.DocumentTemplateStream;
import org.docbag.template.repo.CachingDocumentTemplateRepository;
import org.docbag.template.repo.ClasspathDocumentTemplateRepository;
import org.docbag.template.repo.DocumentTemplateRepository;
import org.docbag.template.transformer.velocity.VelocityTemplateTransformer;

import java.io.InputStream;

/**
 * Created by nforce on 15.7.19.
 */
public class PdfCreator {

    public InputStream getPdf(PdfContext pdfContext, String template) {
        CachingDocumentTemplateRepository templateRepository = new CachingDocumentTemplateRepository();
        templateRepository.registerRepository(new ClasspathDocumentTemplateRepository());

        DocumentCreator<DocumentStream, DocumentTemplateStream> creator = new TazDocumentCreator(MimeConstants.MIME_PDF, new VelocityTemplateTransformer(), templateRepository, PdfCreator.class.getResourceAsStream("fop.xml"));
        Context context = new DefaultContext();
        pdfContext.getContext().forEach(context::put);
        DocumentStream document = creator.createDocument(template, context);
        return document.getStream();
    }
}
