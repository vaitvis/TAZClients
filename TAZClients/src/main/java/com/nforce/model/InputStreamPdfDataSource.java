package com.nforce.model;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by nforce on 15.9.7.
 */
public class InputStreamPdfDataSource implements DataSource {

    private InputStream inputStream;

    public InputStreamPdfDataSource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String getContentType() {
        return "application/pdf";
    }

    @Override
    public String getName() {
        return "InputStreamPdfDataSource";
    }
}
