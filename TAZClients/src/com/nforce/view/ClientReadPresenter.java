package com.nforce.view;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javax.inject.Inject;

import com.nforce.bean.ContentBean;
import com.nforce.bean.ToolBarBean;
import com.nforce.model.Client;
import com.nforce.model.ToolBarButtonsAware;

public class ClientReadPresenter implements Initializable, ToolBarButtonsAware {

	@Inject
	private ContentBean contentBean;
	
	@Inject
	private ToolBarBean toolBarBean;
	
	@FXML
	private Label idLabel;
	
	@FXML
	private Label companyTitleField;
	
	@FXML
	private Label companyCodeField;
	
	@FXML
	private Label companyVatCodeField;
	
	@FXML
	private Label addresseeField;
	
	@FXML
	private Label addressField;
	
	@FXML
	private Label phoneNumberField;
	
	@FXML
	private Label emailField;
	
	@FXML
	private Label validFromField;
	
	@FXML
	private Label validToField;
	
	@FXML
	private Label clientStateField;
	
	@FXML
	private TextArea commentsField;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@Override
	public void initToolBarButtons() {
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
	
	private void fillViewFromClient(Client client) {
		idLabel.setText(String.valueOf(client.getId()));
		companyTitleField.setText(client.getCompanyTitle());
		companyCodeField.setText(client.getCompanyCode());
		companyVatCodeField.setText(client.getCompanyVatCode());
		addresseeField.setText(client.getAddressee());
		addressField.setText(client.getAddress());
		phoneNumberField.setText(client.getPhoneNumber());
		emailField.setText(client.getEmail());
		validFromField.setText(simpleDateFormat.format(client.getValidFrom()));
		validToField.setText(simpleDateFormat.format(client.getValidTo()));
		clientStateField.setText(client.getClientState().title);
		commentsField.setText(client.getComment());
	}
	
	public void setClient(Client client) {
		fillViewFromClient(client);
	}

}
