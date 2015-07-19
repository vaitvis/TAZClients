package com.nforce.model;

/**
 * Created by nforce on 15.7.19.
 */
public class SiaPdfContext extends PdfContext {

    public String getDate() {
        return getContext().get("date");
    }

    public void setDate(String date) {
        getContext().put("date", date);
    }

    public String getCounter() {
        return getContext().get("counter");
    }

    public void setCounter(String counter) {
        getContext().put("counter", counter);
    }

    public String getName() {
        return getContext().get("name");
    }

    public void setName(String name) {
        getContext().put("name", name);
    }

    public String getAddress() {
        return getContext().get("address");
    }

    public void setAddress(String address) {
        getContext().put("address", address);
    }

    public String getCode() {
        return getContext().get("code");
    }

    public void setCode(String code) {
        getContext().put("code", code);
    }

    public String getPhone() {
        return getContext().get("phone");
    }

    public void setPhone(String phone) {
        getContext().put("phone", phone);
    }

    public String getEmail() {
        return getContext().get("email");
    }

    public void setEmail(String email) {
        getContext().put("email", email);
    }
}
