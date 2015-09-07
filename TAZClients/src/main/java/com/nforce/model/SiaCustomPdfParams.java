package com.nforce.model;

/**
 * Created by nforce on 15.9.7.
 */
public class SiaCustomPdfParams {

    private String price;
    private String name; // unused for now
    private String jobTitle; // unused for now

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
