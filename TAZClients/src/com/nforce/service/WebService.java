package com.nforce.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.nforce.bean.ConfigurationBean;
import com.nforce.bean.SessionBean;
import com.nforce.model.ClientState;
import com.nforce.model.ClientStateSerializer;
import com.nforce.model.EventType;
import com.nforce.model.EventTypeSerializer;

public abstract class WebService {

	protected static final String SERVICE_SUFFIX = "Service"; 
	
	@Inject
	protected ConfigurationBean configurationBean;
	
	@Inject
	protected SessionBean sessionBean;
	
	protected String getWebServiceName() {
		String className = this.getClass().getSimpleName();
		if(className.endsWith(SERVICE_SUFFIX)) {
			className = className.replace(SERVICE_SUFFIX, "");
		}
		return className.toLowerCase();
	}
	
	private URI buildServiceURI(String service, String method) {
		URIBuilder ub = new URIBuilder();
		ub.setScheme(configurationBean.getApiScheme());
		ub.setHost(configurationBean.getApiHost());
		StringBuilder sb = new StringBuilder(configurationBean.getApiAddress());
		sb.append("/").append(service).append("/").append(method);
		ub.setPath(sb.toString());
		try {
			return ub.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected String getRequest(String method, Object arg) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			URIBuilder ub = new URIBuilder(buildServiceURI(getWebServiceName(), method));
			String json = buildDataJson(arg);
			if(StringUtils.isNotBlank(json)) {
				ub.addParameter("data", json);
			}
			if(sessionBean.isLoggedIn()) {
				ub.addParameter("ticket", sessionBean.getTicket());
			}
			HttpGet httpGet = new HttpGet(ub.build());
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				HttpEntity entity = response.getEntity();
				return getDataJson(EntityUtils.toString(entity));
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	protected String postRequest(String method, Object arg) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			URIBuilder ub = new URIBuilder(buildServiceURI(getWebServiceName(), method));
			String json = buildDataJson(arg);
			
			HttpPost httpPost = new HttpPost(ub.build());
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			if(StringUtils.isNotBlank(json)) {
				urlParameters.add(new BasicNameValuePair("data", json));
			}
			if(sessionBean.isLoggedIn()) {
				urlParameters.add(new BasicNameValuePair("ticket", sessionBean.getTicket()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(urlParameters, "utf8"));
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				return getDataJson(EntityUtils.toString(entity));
			} finally {
				response.close();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String buildDataJson(Object arg) {
		if(arg == null) {
			return "";
		}
		Gson gson = buildGson();
		return gson.toJson(arg);
	}
	
	private String getDataJson(String json) {
		if(json == null) {
			return null;
		}
		Gson gson = buildGson();
		Response response = gson.fromJson(json, Response.class);
		if(response != null && response.status == 200) {
			if(response.expires != null) {
				sessionBean.setExpires(response.expires);
			}
			return response.data.toString();
		}
		return null;
	}
	
	private class Response {
		int status;
		JsonElement data;
		Date expires;
	}
	
	protected Gson buildGson() {
		GsonBuilder gson = new GsonBuilder();
		gson.setDateFormat("yyyy-MM-dd HH:mm:ss");
		gson.registerTypeAdapter(ClientState.class, new ClientStateSerializer());
		gson.registerTypeAdapter(EventType.class, new EventTypeSerializer());
		return gson.create();
	}
}
