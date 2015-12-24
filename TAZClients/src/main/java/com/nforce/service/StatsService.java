package com.nforce.service;

import com.google.gson.Gson;
import com.nforce.bean.SessionBean;
import com.nforce.model.ClientState;
import com.nforce.model.EventType;

import javax.inject.Inject;
import java.util.Date;

public class StatsService extends WebService {

	class CountByDateStateParam {
		private ClientState state;
		private Date dateFrom;
		private Date dateTo;
	}
	
	public Integer countClientsByState(final ClientState state) {
		CountByDateStateParam param = new CountByDateStateParam();
		param.state = state;
		String json = getRequest("countClientsByDateState", param);
		if(json == null) {
            return null;
        }
        Gson gson = buildGson();
		Integer count = gson.fromJson(json, Integer.class);
        return count;
	}
	
	public Integer countClientsByDateState(final ClientState state, final Date dateFrom, final Date dateTo) {
		CountByDateStateParam param = new CountByDateStateParam();
		param.state = state;
		param.dateFrom = dateFrom;
		param.dateTo = dateTo;
		String json = getRequest("countClientsByDateState", param);
		if(json == null) {
            return null;
        }
        Gson gson = buildGson();
		Integer count = gson.fromJson(json, Integer.class);
        return count;
	}
}
