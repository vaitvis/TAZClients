package com.nforce.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;

import javax.inject.Inject;

import org.controlsfx.control.Notifications;

import com.nforce.bean.SessionBean;
import com.nforce.bean.ToolBarBean;
import com.nforce.model.SessionListener;
import com.nforce.model.ToolBarListener;
import com.nforce.service.LoginService;

public class ToolbarPresenter implements Initializable, SessionListener, ToolBarListener {

	@FXML
	private ToolBar loginToolBar;
	
	@FXML
	private ToolBar controlsToolBar;

	private TextField usernameField;
	private PasswordField passwordField;
	
	@Inject
	private SessionBean sessionBean;
	
	@Inject
	private ToolBarBean toolBarBean;
	
	@Inject
	private LoginService loginService;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setLoggedInMode(sessionBean.isLoggedIn());
		sessionBean.addSessionListener(this);
		toolBarBean.setListener(this);
	}
	
	public void doLogin() {
		String userName = usernameField.getText();
		String password = passwordField.getText();
		
		Service<Boolean> loginWorker = new Service<Boolean>() {
			@Override
			protected Task<Boolean> createTask() {
				return new Task<Boolean>() {
					@Override
					protected Boolean call() throws Exception {
						return loginService.login(userName, password);
					}
				};
			}
			
		};
		
		new ProgressDialog("Siunčiama užklausa...", loginWorker);
		loginWorker.start();
		loginWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				Boolean result = (Boolean) event.getSource().getValue();
				if(Boolean.TRUE.equals(result)) {
					sessionBean.sessionStateChanged();
					Notifications.create().title("Sėkmingas prisijungimas").text("Sėkmingai prisijungėte kaip " + sessionBean.getUserName() + ".").showInformation();
				} else {
					sessionBean.sessionStateChanged();
					Notifications.create().title("Autorizacijos klaida").text("Neteisingas vartotojo vardas ir/arba slaptažodis.").showError();
				}
			}
		});
		
	}
	
	public void doLogout() {
		loginService.logout();
		sessionBean.sessionStateChanged();
	}

	@Override
	public void sessionStateChanged() {
		setLoggedInMode(sessionBean.isLoggedIn());
	}
	
	private void setLoggedInMode(boolean loggedIn) {
		loginToolBar.getItems().clear();
		if(loggedIn) {
			loginToolBar.getItems().add(buildLoggedInLabel());
			loginToolBar.getItems().add(buildLogoutButton());
			usernameField = null;
			passwordField = null;
		} else {
			usernameField = buildUserNameField();
			passwordField = buildPasswordField();
			loginToolBar.getItems().add(usernameField);
			loginToolBar.getItems().add(passwordField);
			loginToolBar.getItems().add(buildLoginButton());
		}
	}
	
	private Button buildLoginButton() {
		Button loginButton = new Button("Prisijungti");
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				doLogin();
			}
		});
		loginButton.requestFocus();
		return loginButton;
	}
	
	private Button buildLogoutButton() {
		Button logoutButton = new Button("Atsijungti");
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				doLogout();
			}
		});
		return logoutButton;
	}
	
	private TextField buildUserNameField() {
		TextField field = new TextField();
		field.setPromptText("Vartotojo vardas");
		return field;
	}
	
	private PasswordField buildPasswordField() {
		PasswordField field = new PasswordField();
		field.setPromptText("Slaptažodis");
		return field;
	}
	
	private Label buildLoggedInLabel() {
		Label label = new Label();
		label.setText("Jūs esate prisijungęs kaip " + sessionBean.getUserName() + ".");
		return label;
	}

	@Override
	public void clear() {
		controlsToolBar.getItems().clear();
	}

	@Override
	public void addButtons(List<Button> buttons) {
		controlsToolBar.getItems().addAll(buttons);
	}
}
