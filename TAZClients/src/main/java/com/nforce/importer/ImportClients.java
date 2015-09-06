package com.nforce.importer;

import java.text.ParseException;

import javax.inject.Inject;

import com.nforce.Utils;
import org.apache.commons.lang3.StringUtils;

import com.nforce.model.Client;
import com.nforce.model.ClientState;
import com.nforce.model.Page;
import com.nforce.model.PagedResult;
import com.nforce.service.ClientsService;
import com.nforce.service.LoginService;

public class ImportClients {

    @Inject
    private LoginService loginService;
    
    @Inject
    private ClientsService clientsService;
    
    public void doJob() {
    	loginService.login("", "");
    	ParserCSV parser = new ParserCSV("./file.csv", "\t");
    	while (parser.next()) {
    		String email = parser.getField("EMAIL");
    		email = getProperEmailString(email);
    		String companyCode = parser.getField("COMPANYCODE");
    		String companyTitle = parser.getField("COMPANYTITLE");
    		String phoneNumber = parser.getField("PHONE");
    		String comment = parser.getField("COMMENT");
    		String addressee = parser.getField("ADDRESSEE");
    		String address = parser.getField("ADDRESS");
    		Client client = formClient(email, companyCode, companyTitle, phoneNumber, comment, addressee, address);
    		/*PagedResult similarClients = getSimilarClients(client);
    		if(similarClients != null) {
    			System.out.println("----------------- Pradzia ---------------------");
    			System.out.println(client);
    			System.out.println("----------------- Panasus ---------------------");
    			System.out.println(getClientsShortString(similarClients.getRows()));
    			System.out.println("----------------- Pabaiga ---------------------");
    		}*/
    		System.out.println(clientsService.update(client));
    	}
    	parser.close();
    }
    
    private String getProperEmailString(String unproper) {
    	String emails[] = unproper.split(",");
    	for(int i = 0; i < emails.length; i++) {
    		emails[i] = emails[i].trim();
    	}
    	return String.join(",", emails);
    }
    
    private Client formClient(String email, String companyCode, String companyTitle, String phoneNumber, String comment, String addressee, String address) {
    	Client client = new Client();
    	client.setAddress(address);
    	client.setAddressee(addressee);
    	client.setClientState(ClientState.HIDDEN);
    	client.setComment(comment);
    	client.setCompanyCode(companyCode);
    	client.setCompanyVatCode("");
    	client.setCompanyTitle(companyTitle);
    	client.setEmail(email);
    	client.setPhoneNumber(phoneNumber);
    	try {
			client.setValidFrom(Utils.getDateFormat("yyyy-MM-dd").parse("2014-12-01"));
	    	client.setValidTo(Utils.getDateFormat("yyyy-MM-dd").parse("2014-12-01"));
		} catch (ParseException e) {
			// shouldn't happen
		}
    	return client;
    }
    
    @SuppressWarnings("unused")
	private PagedResult getSimilarClients(Client client) {
    	PagedResult result = null;
    	if(StringUtils.isNoneBlank(client.getCompanyCode()) && !client.getCompanyCode().equalsIgnoreCase("nėra")) {
	    	result = clientsService.filter(null, client.getCompanyCode(), null, null, null, new Page(0, 5));
	    	if(result.getTotalRows() > 0) {
	    		return result;
	    	}
	    }
    	if(StringUtils.isNoneBlank(client.getEmail()) && !client.getEmail().equalsIgnoreCase("nėra")) {
    		result = clientsService.filter(null, client.getEmail(), null, null, null, new Page(0, 5));
	    	if(result.getTotalRows() > 0) {
	        	return result;
	        }
    	}
    	if(StringUtils.isNoneBlank(client.getPhoneNumber()) && !client.getPhoneNumber().equalsIgnoreCase("nėra")) {
	        result = clientsService.filter(null, client.getPhoneNumber(), null, null, null, new Page(0, 5));
			if(result.getTotalRows() > 0) {
	    		return result;
			}
    	}
    	return null;
    }

    @SuppressWarnings("unused")
    private String getClientsShortString(Client[] clients) {
    	StringBuilder sb = new StringBuilder();
    	int i = 0;
    	for(Client c : clients) {
    		sb.append(++i).append(" ").append(c.getId()).append(" ").append(c.getCompanyCode()).append(" ").append(" ").append(c.getPhoneNumber()).append(" ").append(c.getEmail()).append("\n");
    	}
    	return sb.toString();
    }
}
