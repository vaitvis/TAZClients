package com.nforce.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nforce on 15.7.19.
 */
public abstract class PdfContext {

    private Map<String, String> context;

    public PdfContext() {
        context = new HashMap<>();
    }

    public Map<String, String> getContext() {
        return context;
    }
}
