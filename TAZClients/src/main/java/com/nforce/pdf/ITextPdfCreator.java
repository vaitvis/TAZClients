package com.nforce.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.*;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.nforce.model.PdfContext;

import javax.inject.Singleton;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by nforce on 15.7.19.
 */
@Singleton
public class ITextPdfCreator {

    public ITextPdfCreator() {
        FontFactory.registerDirectories();
    }

    public InputStream getPdf(PdfContext pdfContext, String template) {
        Document document = new Document();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, bout);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, parseTemplate(pdfContext, template), Charset.forName("UTF-8"));
            document.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        return copyOutputStream(bout);
    }

    private InputStream parseTemplate(PdfContext pdfContext, String template) {
        String html = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ITextPdfCreator.class.getResourceAsStream(template), StandardCharsets.UTF_8))) {
            html = br.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> expressions = findExpressions(html);

        for(String e : expressions) {
            String value = pdfContext.getOrDefault(e.substring(1), "");
            String expression = e.replace("$", "\\$");
            html = html.replaceFirst(expression, value);
        }

        return new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
    }

    private List<String> findExpressions(String str) {
        List<String> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\$[A-z]*").matcher(str);
        while (matcher.find()) {
            result.add(str.substring(matcher.start(), matcher.end()));
        }
        return result;
    }

    private InputStream copyOutputStream(ByteArrayOutputStream out) {
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return in;
    }
}
