package com.nforce.service;

import javax.inject.Inject;

import com.nforce.bean.SessionBean;
import com.nforce.model.EventType;

public class HistoryService extends WebService {

	@Inject
	private SessionBean sessionBean;

	@SuppressWarnings("unused")
	class InsertParam {
		private String user;
		private int id;
		private EventType eventType;
		private String comment;
	}
	
	public void insert(final int clientId, final EventType type, final String comment) {
		InsertParam insertParam = new InsertParam();
		insertParam.user = sessionBean.getUserName();
		insertParam.id = clientId;
		insertParam.eventType = type;
		insertParam.comment = comment;
		postRequest("insert", insertParam);
	}
	
	@SuppressWarnings("unused")
	class GetParam {
		private int id;
	}
	
	public void deleteClientHistory(final int clientId) {
		GetParam getParam = new GetParam();
		getParam.id = clientId;
		getRequest("deleteClientHistory", getParam);
	}
}
