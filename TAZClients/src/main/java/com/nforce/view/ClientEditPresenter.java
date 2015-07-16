package com.nforce.view;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import javax.inject.Inject;

import org.controlsfx.control.Notifications;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.nforce.bean.ContentBean;
import com.nforce.bean.SessionBean;
import com.nforce.bean.ToolBarBean;
import com.nforce.model.Client;
import com.nforce.model.ClientState;
import com.nforce.model.DateConverter;
import com.nforce.model.EventType;
import com.nforce.model.ToolBarButtonsAware;
import com.nforce.service.ClientsService;
import com.nforce.service.HistoryService;

public class ClientEditPresenter implements Initializable, ToolBarButtonsAware {

	private Client client;
	
	@Inject
	private ToolBarBean toolBarBean;
	
	@Inject
	private ContentBean contentBean;
	
	@Inject
	private SessionBean sessionBean;
	
	@Inject
	private ClientsService clientsService;
	
	@Inject
	private HistoryService historyService;
	
	@FXML
	private Label idLabel;
	
	@FXML
	private TextField companyTitleField;
	
	@FXML
	private TextField companyCodeField;
	
	@FXML
	private TextField companyVatCodeField;
	
	@FXML
	private TextField addresseeField;
	
	@FXML
	private TextArea addressField;
	
	@FXML
	private TextField phoneNumberField;
	
	@FXML
	private TextField emailField;
	
	@FXML
	private DatePicker validFromField;
	
	@FXML
	private DatePicker validToField;
	
	@FXML
	private ComboBox<ClientState> clientStateField;
	
	@FXML
	private TextArea commentsField;
	
	private ValidationSupport validationSupport;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		client = new Client();
		ObservableList<ClientState> states = FXCollections.observableArrayList(ClientState.values(sessionBean.isAdmin()));
		clientStateField.setItems(states);
		clientStateField.setValue(ClientState.ALIVE);
		clientStateField.setCellFactory(new Callback<ListView<ClientState>, ListCell<ClientState>>() {
			
			@Override
			public ListCell<ClientState> call(ListView<ClientState> param) {
				final ListCell<ClientState> cell = new ListCell<ClientState>() {
					@Override
					protected void updateItem(ClientState item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null && !empty) {
							setText(item.title);
						}
					}
				};
				return cell;
			}
		});
		clientStateField.setButtonCell(new ListCell<ClientState>() {
			@Override
			protected void updateItem(ClientState item, boolean empty) {
				super.updateItem(item, empty);
				if(item != null && !empty) {
					setText(item.title);
				}
			}
		});
		validFromField.setConverter(new DateConverter());
		validToField.setConverter(new DateConverter());
		
		validationSupport = new ValidationSupport();
        validationSupport.registerValidator(companyTitleField, Validator.createEmptyValidator("Šis laukas privalomas"));
        validationSupport.registerValidator(emailField, Validator.createEmptyValidator("Šis laukas privalomas"));
        validationSupport.registerValidator(clientStateField, Validator.createEmptyValidator("Šis laukas privalomas"));
        validationSupport.registerValidator(validFromField, Validator.createEmptyValidator("Šis laukas privalomas"));
        validationSupport.registerValidator(validToField, Validator.createEmptyValidator("Šis laukas privalomas"));
	}

	@Override
	public void initToolBarButtons() {
		Button saveButton = new Button();
		saveButton.setText("Saugoti");
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!validationSupport.isInvalid()) {
					Client client = fillClientFromView();
					Service<Void> saveWorker = new Service<Void>() {
						@Override
						protected Task<Void> createTask() {
							return new Task<Void>() {
								@Override
								protected Void call() throws Exception {
									int id = clientsService.update(client);
									historyService.insert(id, client.getId() == null ? EventType.CLIENT_ADDED : EventType.CLIENT_UPDATED, "");
									return null;
								}
							};
						}
					};
					new ProgressDialog("Saugoma...", saveWorker);
					saveWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent event) {
							contentBean.goBack();
						}
					});
					saveWorker.start();
				} else {
					Notifications.create().title("Validavimo klaida").text("Reikia užpildyti visus privalomus laukus.").showError();
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
	
	private void fillViewFromClient() {
		idLabel.setText(String.valueOf(client.getId()));
		companyTitleField.setText(client.getCompanyTitle());
		companyCodeField.setText(client.getCompanyCode());
		companyVatCodeField.setText(client.getCompanyVatCode());
		addresseeField.setText(client.getAddressee());
		addressField.setText(client.getAddress());
		phoneNumberField.setText(client.getPhoneNumber());
		emailField.setText(client.getEmail());
		
		Instant validFromInstant = client.getValidFrom().toInstant();
		validFromField.setValue(validFromInstant.atZone(ZoneId.systemDefault()).toLocalDate());
		Instant validToInstant = client.getValidTo().toInstant();
		validToField.setValue(validToInstant.atZone(ZoneId.systemDefault()).toLocalDate());
		
		clientStateField.setValue(client.getClientState());
		
		commentsField.setText(client.getComment());
	}
	
	private Client fillClientFromView() {
		Client client = new Client();
		client.setId(this.client.getId());
		client.setCompanyTitle(companyTitleField.getText());
		client.setCompanyCode(companyCodeField.getText());
		client.setCompanyVatCode(companyVatCodeField.getText());
		client.setAddressee(addresseeField.getText());
		client.setAddress(addressField.getText());
		client.setPhoneNumber(phoneNumberField.getText());
		client.setEmail(emailField.getText());
		
		LocalDate localDateFrom = validFromField.getValue();
		Instant instantFrom = Instant.from(localDateFrom.atStartOfDay(ZoneId.systemDefault()));
		client.setValidFrom(Date.from(instantFrom));
		LocalDate localDateTo = validToField.getValue();
		Instant instantTo = Instant.from(localDateTo.atStartOfDay(ZoneId.systemDefault()));
		client.setValidTo(Date.from(instantTo));
		
		client.setClientState(clientStateField.getValue());
		client.setComment(commentsField.getText());
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
		fillViewFromClient();
	}
}
