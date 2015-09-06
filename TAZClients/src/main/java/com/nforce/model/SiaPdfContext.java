package com.nforce.model;

/**
 * Created by nforce on 15.7.19.
 */
public class SiaPdfContext extends PdfContext {

    public String getDate() { return get("date"); }

    public void setDate(String date) { put("date", date); }

    public String getCounter() { return get("counter"); }

    public void setCounter(String counter) {
        put("counter", counter);
    }

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getAddress() {
        return get("address");
    }

    public void setAddress(String address) {
        put("address", address);
    }

    public String getCode() {
        return get("code");
    }

    public void setCode(String code) {
        put("code", code);
    }

    public String getPhone() {
        return get("phone");
    }

    public void setPhone(String phone) {
        put("phone", phone);
    }

    public String getEmail() {
        return get("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public String getPrice() { return get("price"); }

    public void setPrice(String price) { put("price", price); }

    public String getPeriod() { return get("period"); }

    public void setPeriod(String period) { put("period", period); }
}
