package com.nforce.service;

import com.google.gson.Gson;
import com.nforce.model.LoginData;


public class LoginService extends WebService {

	@SuppressWarnings("unused")
	private class LoginPackage {
		private String userName;
		private String password;
	}
	
	public boolean login(String userName, String password) {
		LoginPackage lp = new LoginPackage();
		lp.userName = userName;
		lp.password = password;
		String json = getRequest("login", lp);
		if(json == null) {
			sessionBean.clear();
			return false;
		}
		Gson gson = buildGson();
		System.out.println(json);
		LoginData loginData = gson.fromJson(json, LoginData.class);
		
		if(loginData != null) {
			sessionBean.init(loginData, userName);
			return true;
		}
		return false;
	}
	
	public void logout() {
		sessionBean.clear();
	}	
}
