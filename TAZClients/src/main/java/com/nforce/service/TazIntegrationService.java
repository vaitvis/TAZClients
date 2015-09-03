package com.nforce.service;

import com.google.gson.Gson;
import com.nforce.bean.SessionBean;

import javax.inject.Inject;

/**
 * Created by nforce on 15.9.4.
 */
public class TazIntegrationService extends WebService {

	@Inject
	private SessionBean sessionBean;

    public Integer getProformaInvoiceId() {
        String json = getRequest("getProformaInvoiceId", null);
        if(json == null) {
            return null;
        }
        Gson gson = buildGson();
		Integer nextId = gson.fromJson(json, Integer.class);
        return  nextId;
    }
}
