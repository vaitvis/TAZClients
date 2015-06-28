package com.nforce.view;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.web.HTMLEditor;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.nforce.bean.SessionBean;
import com.nforce.bean.ToolBarBean;
import com.nforce.model.Client;
import com.nforce.model.ClientState;
import com.nforce.model.DateConverter;
import com.nforce.model.EventType;
import com.nforce.model.Page;
import com.nforce.model.PagedResult;
import com.nforce.model.ToolBarButtonsAware;
import com.nforce.service.ClientsService;
import com.nforce.service.EmailService;
import com.nforce.service.HistoryService;

@SuppressWarnings("deprecation")
public class EmailPresenter implements TazInitializable, ToolBarButtonsAware {
	
	private static final int PAGE_SIZE = 5;
	
	@FXML 
	private Pagination pagination;
	
	@FXML
	private TextField idFilter;
	
	@FXML
	private TextField filter;
	
	@FXML
	private ChoiceBox<ClientState> clientStateFilter;
	
	@FXML
	private DatePicker validFromFilter;
	
	@FXML
	private DatePicker validToFilter;
	
	@FXML
	private Button selectButton;
	
	@FXML
	private Button filterButton;
	
	@FXML
	private TitledPane recipients;
	
	@FXML
	private Label recipientsCount;
	
	@FXML
	private TextField subject;
	
	@FXML
	private HTMLEditor emailText;
	
	@Inject 
	private ClientsService clientsService;
	
	@Inject
	private EmailService emailService;
	
	@Inject
	private HistoryService historyService;
	
	@Inject
	private ToolBarBean toolBarBean;
	
	@Inject
	private SessionBean sessionBean;
	
	private ValidationSupport validationSupport;
	
	private Integer totalCount;
	
	private Client[] selectedClients;
	
	@Override
	public void initToolBarButtons() {
		Button saveButton = new Button();
		saveButton.setText("Siųsti");
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(selectedClients == null || selectedClients.length == 0) {
					Notifications.create().title("Nėra gavėjų").text("Turite pasirinkti bent vieną gavėją.").showError();
				} else if(validationSupport.isInvalid()) {
					Notifications.create().title("Validavimo klaida").text("Reikia įrašyti laiško temą.").showError();
				} else {
					sendMail();
				}
			}
		});
		toolBarBean.addButton(saveButton);
		toolBarBean.commitButtons();
	}

	@Override
	public void realInit() {		
		List<ClientState> realStates = new ArrayList<ClientState>();
		realStates.add(null);
		realStates.addAll(Arrays.asList(ClientState.values(sessionBean.isAdmin())));
		ObservableList<ClientState> states = FXCollections.observableList(realStates);
		clientStateFilter.setItems(states);
		clientStateFilter.getSelectionModel().selectFirst();
		clientStateFilter.setConverter(new StringConverter<ClientState>() {
			
			@Override
			public String toString(ClientState object) {
				if(object == null) {
					return "Bet kokia";
				}
				return object.title;
			}
			
			@Override
			public ClientState fromString(String string) {
				if("Bet kokia".equals(string)) {
					return null;
				} else {
					for(ClientState s : ClientState.values()) {
						if(s.title.equals(string)) {
							return s;
						}
					}
				}
				return null;
			}
		});
		
		validFromFilter.setConverter(new DateConverter());
		validToFilter.setConverter(new DateConverter());
		
		selectedClients = null;
		
		selectButton.setDisable(true);
		
		validationSupport = new ValidationSupport();
        validationSupport.registerValidator(subject, Validator.createEmptyValidator("Šis laukas privalomas"));
	}

	@FXML
	public void doFilter() {
		clientsInit();
	}
	
	@FXML
	public void selectClients() {
		Integer id = null;
		try {
			id = Integer.parseInt(idFilter.getText());
		} catch (NumberFormatException e) {
		}
		Integer finalId = id;
		String term = filter.getText();
		LocalDate localDateFrom = validFromFilter.getValue();
		Instant instantFrom = localDateFrom == null ? null : Instant.from(localDateFrom.atStartOfDay(ZoneId.systemDefault()));
		Date validFrom = instantFrom == null ? null : Date.from(instantFrom);
		LocalDate localDateTo = validToFilter.getValue();
		Instant instantTo = localDateTo == null ? null : Instant.from(localDateTo.atStartOfDay(ZoneId.systemDefault()));
		Date validTo = instantTo == null ? null : Date.from(instantTo);
		ClientState clientState = clientStateFilter.getValue();
		
		Service<PagedResult> filterWorker = new Service<PagedResult>() {
			@Override
			protected Task<PagedResult> createTask() {
				return new Task<PagedResult>() {
					@Override
					protected PagedResult call() throws Exception {
						return clientsService.filter(finalId, term, validFrom, validTo, clientState, new Page(0, totalCount));
					}
				};
			}			
		};
		new ProgressDialog("Kraunama...", filterWorker);
		filterWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				PagedResult result = (PagedResult) event.getSource().getValue();
				if(result != null) {
					totalCount = result.getTotalRows();
					selectedClients = result.getRows();
					recipientsSelected();
				}
			}
		});
		filterWorker.start();
	}
	
	private void recipientsSelected() {
		recipients.setExpanded(false);
		recipientsCount.setText(selectedClients == null ? "0" : String.valueOf(selectedClients.length));
	}
	
	private void clientsInit() {
		Integer id = null;
		try {
			id = Integer.parseInt(idFilter.getText());
		} catch (NumberFormatException e) {
		}
		Integer finalId = id;
		String term = filter.getText();
		LocalDate localDateFrom = validFromFilter.getValue();
		Instant instantFrom = localDateFrom == null ? null : Instant.from(localDateFrom.atStartOfDay(ZoneId.systemDefault()));
		Date validFrom = instantFrom == null ? null : Date.from(instantFrom);
		LocalDate localDateTo = validToFilter.getValue();
		Instant instantTo = localDateTo == null ? null : Instant.from(localDateTo.atStartOfDay(ZoneId.systemDefault()));
		Date validTo = instantTo == null ? null : Date.from(instantTo);
		ClientState clientState = clientStateFilter.getValue();
		
		Service<PagedResult> filterWorker = new Service<PagedResult>() {
			@Override
			protected Task<PagedResult> createTask() {
				return new Task<PagedResult>() {
					@Override
					protected PagedResult call() throws Exception {
						return clientsService.filter(finalId, term, validFrom, validTo, clientState, null);
					}
				};
			}
			
		};
		new ProgressDialog("Kraunama...", filterWorker);
		filterWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				PagedResult result = (PagedResult) event.getSource().getValue();
				if(result != null) {
					totalCount = result.getTotalRows();
					int pageCount = totalCount / PAGE_SIZE;
					pageCount += (totalCount % PAGE_SIZE != 0 ) ? 1 : 0;
					pagination.setPageCount(pageCount);
					pagination.setCurrentPageIndex(0);
					pagination.setPageFactory(new Callback<Integer, Node>() {
						@Override
						public Node call(Integer index) {
							PagedResult result = clientsService.filter(finalId, term, validFrom, validTo, clientState, new Page(PAGE_SIZE * index, PAGE_SIZE));
							ObservableList<Client> list = FXCollections.observableArrayList(result.getRows());
							ListView<Client> listView = new ListView<Client>(list);
							listView.setCellFactory(new Callback<ListView<Client>, ListCell<Client>>() {
								@Override
								public ListCell<Client> call(ListView<Client> param) {
									return new ClientInlineCell();
								}
							});
							return listView;
						}
					});
					selectButton.setDisable(false);
				}
			}
			
		});
		filterWorker.start();
	}
	
	static class ClientInlineCell extends ListCell<Client> {
		@Override
		protected void updateItem(Client item, boolean empty) {
			super.updateItem(item, empty);
			if(!empty && item != null) {
				ClientInlineView view = new ClientInlineView();
				ClientInlinePresenter presenter = (ClientInlinePresenter) view.getPresenter();
				presenter.setClient(item);
				setGraphic(view.getView());
			}
		}
	}
	
	private void sendMail() {
		Action response = Dialogs.create().title("Ar siųsti laišką?").message("Ar siųsti laišką " + selectedClients.length + " gavėjui (-ams)?")
			      .showConfirm();
		if(response == Dialog.ACTION_YES) {
			Service<Integer> emailWorker = new Service<Integer>() {
				@Override
				protected Task<Integer> createTask() {
					return new Task<Integer>() {
						@Override
						protected Integer call() throws Exception {
							Integer failCount = 0;
							Integer index = 1;
							for(Client client : selectedClients) {
								updateMessage("Siunčiamas laiškas " + index++ + "/" + selectedClients.length);
								if(StringUtils.isNotEmpty(client.getEmail())) {
									if(!emailService.sendMail(client.getEmail(), subject.getText(), emailText.getHtmlText())) {
										failCount++;
										historyService.insert(client.getId(), EventType.MAIL_FAILED, subject.getText());
									} else {
										historyService.insert(client.getId(), EventType.MAIL_SENT, subject.getText());
									}
								} else {
									failCount++;
									historyService.insert(client.getId(), EventType.MAIL_FAILED, subject.getText() + ": Neįvestas el. pašto adresas");
								}
							}
							return failCount;
						}
					};
				}
			};
			new ProgressDialog("Siunčiama...", emailWorker);
			emailWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					Integer failCount = (Integer) event.getSource().getValue();
					if(failCount != null && failCount > 0) {
						Notifications.create().title("Ne visi laiškai išsiųsti").text("Nepavyko išsiųsti " + failCount + " laiško (-ų).").showError();
					} else {
						Notifications.create().title("Siuntimas baigtas").text("Visi laiškai išsiųsti.").showInformation();
					}
				}
			});
			emailWorker.setOnFailed(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					event.getSource().getException().printStackTrace();
					Notifications.create().title("Nepavyko išsiųsti laiškų").text("Visai nepavyko.").showError();
				}
			});
			emailWorker.start();
		}
	}
}
