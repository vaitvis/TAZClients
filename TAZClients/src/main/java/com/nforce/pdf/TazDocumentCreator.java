package com.nforce.pdf;

import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.docbag.*;
import org.docbag.stream.MemoryInputStream;
import org.docbag.stream.MemoryOutputStream;
import org.docbag.template.DocumentTemplateStream;
import org.docbag.template.repo.DocumentTemplateRepository;
import org.docbag.template.transformer.TemplateTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.Date;

public class TazDocumentCreator implements DocumentCreator<DocumentStream, DocumentTemplateStream> {
    private static final Logger log = LoggerFactory.getLogger(TazDocumentCreator.class);
    private final String mimeType;
    private final FopFactory fopFactory = FopFactory.newInstance();
    private final TransformerFactory tFactory = TransformerFactory.newInstance();
    private final TemplateTransformer<DocumentTemplateStream> templateTransformer;
    private final DocumentTemplateRepository<DocumentTemplateStream> templateRepository;
    private final FOUserAgent userAgent = fopFactory.newFOUserAgent();

    public TazDocumentCreator(String mimeType, TemplateTransformer<DocumentTemplateStream> templateTransformer,
                              DocumentTemplateRepository<DocumentTemplateStream> templateRepository) {
        this(mimeType, templateTransformer, templateRepository, null);
    }

    public TazDocumentCreator(String mimeType, TemplateTransformer<DocumentTemplateStream> templateTransformer,
                              DocumentTemplateRepository<DocumentTemplateStream> templateRepository, InputStream config) {
        this.mimeType = mimeType;
        this.templateTransformer = templateTransformer;
        this.templateRepository = templateRepository;
        if (config != null) {
            configure(config);
        }
    }

    public DocumentStream createDocument(DocumentTemplateStream templateStream) {
        return createDocument(templateStream, new DefaultContext());
    }

    public DocumentStream createDocument(DocumentTemplateStream templateStream, Context context) {
        if (templateStream == null) {
            throw new NullPointerException("DocumentTemplate can't be null!");
        }
        MemoryOutputStream pdf = new MemoryOutputStream();
        try {
            // Prepare DocumentTemplate
            DocumentTemplateStream transformed = transformTemplate(templateStream, context);
            // Generate PDF
            tFactory.newTransformer().transform(new StreamSource(transformed.getStream()),
                    new SAXResult(fopFactory.newFop(mimeType, userAgent, pdf).getDefaultHandler()));
        } catch (Exception e) {
            log.error("Error creating document: " + e.getLocalizedMessage(), e);
            throw new DocumentCreatorException("Error creating document: " + e.getLocalizedMessage(), e);
        }
        return new DocumentStream(new Date(), new MemoryInputStream(pdf));
    }

    /**
     * To be able to use this version of createDocument the templateRepository attribute needs to be set
     */
    public DocumentStream createDocument(String templateName) {
        return createDocument(templateName, new DefaultContext());
    }

    /**
     * To be able to use this version of createDocument the templateRepository attribute needs to be set
     */
    public DocumentStream createDocument(String templateName, Context context) {
        if (templateRepository == null) {
            throw new NullPointerException("Default template repository not set! If you want to use 'templateName' version of"
                    + " createDocument() then you need to set valid instance of DocumentTemplateRepository");
        }
        return createDocument(templateRepository.findTemplate(templateName), context);
    }

    private DocumentTemplateStream transformTemplate(DocumentTemplateStream templateStream, Context context) {
        if (templateTransformer != null) {
            return templateTransformer.transform(templateStream, context);
        }
        return templateStream;
    }

    private void configure(InputStream config) {
        DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
        try {
            fopFactory.setUserConfig(cfgBuilder.build(config));
        } catch (Exception e) {
            log.error("Error configuring Apache FOP!", e.getLocalizedMessage(), e);
        }
    }

    public String toString() {
        return "FOPDocumentCreator{" +
                "mimeType='" + mimeType + "\'}";
    }
}
