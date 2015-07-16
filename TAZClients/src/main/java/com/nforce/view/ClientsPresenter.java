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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.inject.Inject;

import com.nforce.bean.ContentBean;
import com.nforce.bean.SessionBean;
import com.nforce.bean.ToolBarBean;
import com.nforce.model.Client;
import com.nforce.model.ClientState;
import com.nforce.model.DateConverter;
import com.nforce.model.Page;
import com.nforce.model.PagedResult;
import com.nforce.model.ToolBarButtonsAware;
import com.nforce.service.ClientsService;

public class ClientsPresenter implements TazInitializable, ToolBarButtonsAware {

	private static final int PAGE_SIZE = 10;
	
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
	
	@Inject 
	private ClientsService clientsService;
	
	@Inject
	private ToolBarBean toolBarBean;
	
	@Inject
	private ContentBean contentBean;
	
	@Inject 
	private SessionBean sessionBean;
	
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
		
		clientsInit();
	}

	@Override
	public void initToolBarButtons() {
		Button createButton = new Button();
		createButton.setText("Kurti naują");
		createButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				contentBean.changeContent(new ClientEditView());
			}
		});
		toolBarBean.addButton(createButton);
		toolBarBean.commitButtons();
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
					int pageCount = result.getTotalRows() / PAGE_SIZE;
					pageCount += (result.getTotalRows() % PAGE_SIZE != 0 ) ? 1 : 0;
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
	
	@FXML
	public void doFilter() {
		clientsInit();
	}
}
