package com.nforce.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;

import com.airhacks.afterburner.views.FXMLView;
import com.nforce.bean.ContentBean;
import com.nforce.bean.ToolBarBean;
import com.nforce.model.ContentListener;

public class ContentPresenter implements Initializable, ContentListener {

	@FXML
	private AnchorPane contentPane;
	
	@Inject
	private ToolBarBean toolBarBean;
	
	@Inject 
	private ContentBean contentBean;
	
	private FXMLView previousView;
	private FXMLView currentView;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		contentBean.setListener(this);
		changeContent(new InitialView());
	}

	@Override
	public void changeContent(FXMLView view) {
		previousView = currentView;
		currentView = view;
		toolBarBean.clear();
		contentPane.getChildren().clear();
		contentPane.getChildren().add(currentView.getView());
	}

	@Override
	public void goBack() {
		if(previousView != null) {
			changeContent(previousView);
		}
	}
}
