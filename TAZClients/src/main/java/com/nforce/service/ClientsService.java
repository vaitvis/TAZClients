package com.nforce.service;

import java.util.Date;

import com.nforce.model.Client;
import com.nforce.model.ClientState;
import com.nforce.model.Page;
import com.nforce.model.PagedResult;

public class ClientsService extends WebService {

	public int update(final Client client) {
		String json = postRequest("update", client);
		if(json != null) {
			GetParam result = buildGson().fromJson(json, GetParam.class);
			return result.id;
		}
		return 0;
	}

	class GetParam {
		private int id;
	}
	
	public Client get(final int clientId) {
		GetParam getParam = new GetParam();
		getParam.id = clientId;
		String json = getRequest("get", getParam);
		if(json != null) {
			return buildGson().fromJson(json, Client.class);
		}
		return null;
	}
	
	public void delete(final int clientId) {
		GetParam getParam = new GetParam();
		getParam.id = clientId;
		getRequest("delete", getParam);
	}

	@SuppressWarnings("unused")
	class FilterParam {
		private Integer id;
		private String filter;
		private Date validFrom;
		private Date validTo;
		private ClientState state;
		private Page page;
	}
	
	public PagedResult filter(Integer id, String filter, Date validFrom, Date validTo, ClientState state, Page page) {
		FilterParam filterParam = new FilterParam();
		filterParam.id = id;
		filterParam.filter = filter;
		filterParam.validFrom = validFrom;
		filterParam.validTo = validTo;
		filterParam.state = state;
		filterParam.page = page;
		String json = getRequest("filter", filterParam);
		if(json != null) {
			return buildGson().fromJson(json, PagedResult.class);
		}
		return null;
	}
	
}
