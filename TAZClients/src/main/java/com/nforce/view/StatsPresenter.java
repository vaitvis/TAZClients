package com.nforce.view;

import com.nforce.Utils;
import com.nforce.model.ClientState;
import com.nforce.service.StatsService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by nforce on 15.12.24.
 */
public class StatsPresenter implements TazInitializable {

    private static final ClientState ALIVE = ClientState.ALIVE;

    @FXML
    private TableView<HashMap<ClientState, String>> subscribersTable;

    @FXML
    private GridPane activeSubscribers;

    @Inject
    private StatsService statsService;

    private List<Map<String, String>> activeSubscribersData = new ArrayList<>();

    @Override
    public void realInit() {
        initSubscribersTable();
        Service<Void> initWorker = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        HashMap<ClientState, String> s = retrieveSubscribersData();
                        subscribersTable.setItems(FXCollections.observableList(Collections.singletonList(s)));
                        retrieveActiveSubscribersData();
                        return null;
                    }
                };
            }
        };
        initWorker.setOnSucceeded(workerStateEvent -> initActiveSubscribersGrid());
        new ProgressDialog("Gaunami duomenys...", initWorker);
        initWorker.start();
    }

    private void initSubscribersTable() {
        List<TableColumn<HashMap<ClientState, String>, String>> subscribersData = new ArrayList<>();
        for(ClientState state : ClientState.values()) {
            TableColumn<HashMap<ClientState, String>, String> column = new TableColumn<>(state.title);
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<HashMap<ClientState, String>, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<HashMap<ClientState, String>, String> p) {
                     return new ReadOnlyObjectWrapper(p.getValue().get(state));
                }
            });
            column.setMinWidth(150);
            subscribersData.add(column);
        }
        TableColumn<HashMap<ClientState, String>, String> column = new TableColumn<>("Iš viso");
        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<HashMap<ClientState, String>, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<HashMap<ClientState, String>, String> p) {
                 return new ReadOnlyObjectWrapper(p.getValue().get(null));
            }
        });
        column.setMinWidth(150);
        subscribersData.add(column);
        subscribersTable.getColumns().addAll(subscribersData);
    }

    private HashMap<ClientState, String> retrieveSubscribersData() {
        HashMap<ClientState, String> s = new HashMap<ClientState, String>();
        for (ClientState state : ClientState.values()) {
            s.put(state, String.valueOf(statsService.countClientsByState(state)));
        }
        s.put(null, String.valueOf(statsService.countClientsByState(null)));
        return s;
    }

    private void initActiveSubscribersGrid() {
        int arrayIndex = 0;
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 7; j++) {
                Map<String, String> data = activeSubscribersData.get(arrayIndex++);
                VBox cell = initGridCell(data.get("title"), data.get("value"));
                activeSubscribers.add(cell, j, i);
            }
        }
    }

    private VBox initGridCell(String title, String value) {
        Label titleLabel = new Label();
        titleLabel.setFont(Font.font(null, FontWeight.BOLD, 15));
        titleLabel.setText(title);
        Label valueLabel = new Label();
        valueLabel.setText(value);
        VBox vbox = new VBox(titleLabel, valueLabel);
        vbox.setSpacing(15);
        return vbox;
    }

    private void retrieveActiveSubscribersData() {
        Date startDate = new Date();
        activeSubscribersData.add(getFirstCell(startDate));
        for(int i = 0; i < 12; i++) {
            activeSubscribersData.add(getCell(Utils.addMonths(startDate, i)));
        }
        activeSubscribersData.add(getLastCell(Utils.addMonths(startDate, 12)));
    }

    private Map<String, String> getFirstCell(Date startDate) {
        Map<String, String> map = new HashMap<>();
        Date valueDate = Utils.resetDate(startDate, Utils.TimeOfMonth.START);
        String value = String.valueOf(statsService.countClientsByDateState(ALIVE, null, valueDate));
        map.put("value", value);
        Date titleDate = Utils.addMonths(valueDate, -1);
        titleDate = Utils.resetDate(titleDate, Utils.TimeOfMonth.END);
        map.put("title", "Iki " + Utils.getDateFormat("yyyy-MM-dd").format(titleDate));
        return map;
    }

    private Map<String, String> getCell(Date startDate) {
        Map<String, String> map = new HashMap<>();
        Date start = Utils.resetDate(startDate, Utils.TimeOfMonth.START);
        Date end = Utils.addMonths(startDate, 1);
        end = Utils.resetDate(end, Utils.TimeOfMonth.START);
        String value = String.valueOf(statsService.countClientsByDateState(ALIVE, start, end));
        map.put("value", value);
        Date titleDate = Utils.resetDate(startDate, Utils.TimeOfMonth.END);
        map.put("title", Utils.getDateFormat("yyyy-MM-dd").format(titleDate));
        return map;
    }

    private Map<String, String> getLastCell(Date startDate) {
        Map<String, String> map = new HashMap<>();
        Date start = Utils.resetDate(startDate, Utils.TimeOfMonth.START);
        String value = String.valueOf(statsService.countClientsByDateState(ALIVE, start, null));
        map.put("value", value);
        Date titleDate = Utils.addMonths(startDate, -1);
        titleDate = Utils.resetDate(titleDate, Utils.TimeOfMonth.END);
        map.put("title", "Po " + Utils.getDateFormat("yyyy-MM-dd").format(titleDate));
        return map;
    }
}
