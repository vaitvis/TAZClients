package com.nforce.view;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;

import com.nforce.bean.ContentBean;
import com.nforce.bean.SessionBean;
import com.nforce.model.Client;
import com.nforce.model.DateConverter;
import com.nforce.service.ClientsService;
import com.nforce.service.HistoryService;

public class ClientInlinePresenter implements Initializable {

	private Client client;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private Label idLabel;
	
	@FXML
	private Label companyTitleLabel;
	
	@FXML
	private Label companyCodeLabel;
	
	@FXML
	private Label companyVatCodeLabel;
	
	@FXML
	private Label addressLabel;
	
	@FXML
	private Label phoneNumberLabel;
	
	@FXML
	private Label emailLabel;
	
	@FXML
	private Label validFromLabel;
	
	@FXML
	private Label validToLabel;
	
	@FXML
	private Label clientStateLabel;
	
	@Inject
	private SessionBean sessionBean;
	
	@Inject
	private ContentBean contentBean;
	
	@Inject
	private ClientsService clientsService;
	
	@Inject
	private HistoryService historyService;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateConverter.pattern);
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(!sessionBean.isAdmin()) {
			deleteButton.setVisible(false);
		}
	}
	
	public void setClient(Client client) {
		this.client = client;
		idLabel.setText(String.valueOf(client.getId()));
		companyTitleLabel.setText(client.getCompanyTitle());
		companyCodeLabel.setText(client.getCompanyCode());
		companyVatCodeLabel.setText(client.getCompanyVatCode());
		addressLabel.setText(client.getAddress());
		phoneNumberLabel.setText(client.getPhoneNumber());
		emailLabel.setText(client.getEmail());
		if(client.getValidFrom() != null) {
			validFromLabel.setText(simpleDateFormat.format(client.getValidFrom()));
		}
		if(client.getValidTo() != null) {
			validToLabel.setText(simpleDateFormat.format(client.getValidTo()));
		}
		clientStateLabel.setText(client.getClientState().title);
	}

	@FXML
	public void doRead() {
		if(client != null) {
			ClientReadView view = new ClientReadView();
			((ClientReadPresenter) view.getPresenter()).setClient(client);
			contentBean.changeContent(view);
		}
	}
	
	@FXML
	public void doEdit() {
		if(client != null) {
			ClientEditView view = new ClientEditView();
			((ClientEditPresenter) view.getPresenter()).setClient(client);
			contentBean.changeContent(view);
		}
	}
	
	@FXML
	public void doDelete() {
		if(client != null) {
			Service<Void> deleteWorker = new Service<Void>() {
				@Override
				protected Task<Void> createTask() {
					return new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							clientsService.delete(client.getId());
							historyService.deleteClientHistory(client.getId());
							return null;
						}
					};
				}
				
			};
			new ProgressDialog("Trinama...", deleteWorker);
			deleteWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					contentBean.changeContent(new ClientsView());
				}
			});
			deleteWorker.start();
		}
	}
}
