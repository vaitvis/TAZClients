package com.nforce.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;

import org.controlsfx.control.Notifications;

import com.nforce.bean.ConfigurationBean;
import com.nforce.bean.ContentBean;
import com.nforce.bean.ToolBarBean;
import com.nforce.model.ToolBarButtonsAware;

public class SettingsPresenter implements TazInitializable, ToolBarButtonsAware {

	@FXML
	private TextField smtpHostField;
	
	@FXML
	private TextField smtpUserField;
	
	@FXML
	private TextField smtpPasswordField;
	
	@FXML
	private TextField smtpFromField;
	
	@FXML
	private TextField smtpFromNameField;
	
	@Inject
	private ConfigurationBean configurationBean;
	
	@Inject
	private ToolBarBean toolBarBean;
	
	@Inject
	private ContentBean contentBean;
	
	@Override
	public void initToolBarButtons() {
		Button saveButton = new Button();
		saveButton.setText("Saugoti");
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				configurationBean.setSmtpHost(smtpHostField.getText());
				configurationBean.setSmtpUser(smtpUserField.getText());
				configurationBean.setSmtpPassword(smtpPasswordField.getText());
				configurationBean.setSmtpFrom(smtpFromField.getText());
				configurationBean.setSmtpFromName(smtpFromNameField.getText());
				if(configurationBean.saveSettings()) {
					configurationBean.initSettings();
					contentBean.goBack();
				} else {
					Notifications.create().title("Saugojimo klaida").text("Nepavyko išsaugoti nustatymų.").showError();
				}
			}
		});
		toolBarBean.addButton(saveButton);
		Button closeButton = new Button();
		closeButton.setText("Grįžti");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				contentBean.goBack();
			}
		});
		toolBarBean.addButton(closeButton);
		toolBarBean.commitButtons();
	}

	@Override
	public void realInit() {
		configurationBean.initSettings();
		smtpHostField.setText(configurationBean.getSmtpHost());
		smtpUserField.setText(configurationBean.getSmtpUser());
		smtpPasswordField.setText(configurationBean.getSmtpPassword());
		smtpFromField.setText(configurationBean.getSmtpFrom());
		smtpFromNameField.setText(configurationBean.getSmtpFromName());
	}

}
