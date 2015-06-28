package com.nforce.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javax.inject.Inject;

import com.nforce.bean.ConfigurationBean;
import com.nforce.bean.ContentBean;
import com.nforce.bean.SessionBean;
import com.nforce.model.SessionListener;

public class RootLayoutPresenter implements Initializable, SessionListener {

	@FXML
	private Button homeButton;
	
	@FXML
	private Button clientsButton;
	
	@FXML
	private Button emailButton;
	
	@FXML
	private Button settingsButton;
	
	@Inject 
	private ContentBean contentBean;
	
	@Inject
	private SessionBean sessionBean;
	
	@Inject
	private ConfigurationBean configurationBean;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configurationBean.initSettings();
		sessionBean.addSessionListener(this);
		disableButtons(!sessionBean.isLoggedIn());
	}

	@FXML
	public void doHome() {
		contentBean.changeContent(new InitialView());
	}

	@FXML
	public void doClients() {
		contentBean.changeContent(new ClientsView());
	}

	@FXML
	public void doEmail() {
		contentBean.changeContent(new EmailView());
	}
	
	@FXML
	public void doSettings() {
		contentBean.changeContent(new SettingsView());
	}

	private void disableButtons(boolean disable) {
		homeButton.setDisable(disable);
		clientsButton.setDisable(disable);
		emailButton.setDisable(sessionBean.isAdmin() ? disable : true);
		settingsButton.setDisable(sessionBean.isAdmin() ? disable : true);
	}
	
	@Override
	public void sessionStateChanged() {
		disableButtons(!sessionBean.isLoggedIn());
		contentBean.changeContent(new InitialView());
	}
}
