package com.nforce.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.nforce.model.InputStreamPdfDataSource;
import org.apache.commons.lang3.StringUtils;

import com.nforce.bean.ConfigurationBean;

public class EmailService {

	@Inject 
	private ConfigurationBean configurationBean;

	public boolean sendMail(String recipient, String subject, String text) {
		return sendMail(recipient, subject, text, null);
	}

	public boolean sendMail(String recipient, String subject, String text, InputStream attachment) {
		Properties properties = System.getProperties();
		if(StringUtils.isAnyBlank(configurationBean.getSmtpHost(), configurationBean.getSmtpUser(), configurationBean.getSmtpPassword(), configurationBean.getSmtpFrom())) {
			return false;
		}

		Transport t = null;
	    try{
		    Session session = Session.getInstance(properties, null);
		    MimeMessage message = new MimeMessage(session);

	    	message.setFrom(new InternetAddress(configurationBean.getSmtpFrom(), configurationBean.getSmtpFromName()));
			
	    	String recipients[] = recipient.split(",");
	    	if(recipients == null) {
	    		return false;
	    	}
	    	for(String r : recipients) {
	    		message.addRecipient(Message.RecipientType.TO, new InternetAddress(r));
	    	}

			message.setSubject(subject, "utf-8");

			Multipart multipart = new MimeMultipart(); 
			
			BodyPart messageBodyPart = new MimeBodyPart(); 
			StringBuilder sb = new StringBuilder("<img src=\"cid:logo\">");
			sb.append(text);
			messageBodyPart.setContent(sb.toString(), "text/html; charset=utf-8" );
			multipart.addBodyPart(messageBodyPart); 
			
			messageBodyPart = new MimeBodyPart();
	        DataSource fds = new URLDataSource(ClassLoader.getSystemResource("logo.png"));

	        messageBodyPart.setDataHandler(new DataHandler(fds));
	        messageBodyPart.setHeader("Content-ID", "<logo>");
	        multipart.addBodyPart(messageBodyPart);

			if(attachment != null) {
				messageBodyPart = new MimeBodyPart();
				DataSource ads = new InputStreamPdfDataSource(attachment);
				messageBodyPart.setDataHandler(new DataHandler(ads));
				messageBodyPart.setFileName("isankstine_saskaita.pdf");
	        	multipart.addBodyPart(messageBodyPart);
			}

			message.setContent(multipart);

			t = session.getTransport("smtp");
			t.connect(configurationBean.getSmtpHost(), configurationBean.getSmtpUser(), configurationBean.getSmtpPassword());
			t.sendMessage(message, message.getAllRecipients());
			return true;
		}catch (Exception ex) {
	         return false;
		} finally {
			if (attachment != null) {
				try {
					attachment.close();
				} catch (IOException e) {
					return false;
				}
			}
			if(t != null) {
				try {
					t.close();
				} catch (MessagingException e) {
					return false;
				}
			}
	     }
	}
	
}
