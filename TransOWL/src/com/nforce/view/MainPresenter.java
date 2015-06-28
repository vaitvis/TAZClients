package com.nforce.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.nforce.model.StatisticsBean;
import com.nforce.service.FileVisitor;
import com.nforce.service.JavaCodeParser;

public class MainPresenter implements Initializable {

	@FXML
	private TextField sourceDirField;

	@FXML
	private TextField resultDirField;

	@FXML
	private TextField fileWildcardField;

	@FXML
	private NumberTextField recursionLevelField;

	private final DirectoryChooser directoryChooser = new DirectoryChooser();
	private ValidationSupport validationSupport = new ValidationSupport();
	private File sourceDir;
	private File resultDir;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		validationSupport.registerValidator(sourceDirField, Validator.createEmptyValidator("Privalote pasirinkti java kodo katalogą"));
		validationSupport.registerValidator(resultDirField, Validator.createEmptyValidator("Privalote pasirinkti rezultatų katalogą"));
		validationSupport.registerValidator(fileWildcardField, Validator.createEmptyValidator("Privalote įrašyti kodo failų šabloną"));
		validationSupport.registerValidator(recursionLevelField, Validator.createEmptyValidator("Privalote įrašyti rekursijos gylį"));
	}

	private void configureDirectoryChooser(final DirectoryChooser directoryChooser, final String title, String currentDir) {
		directoryChooser.setTitle(title);
		File initialDirectory = StringUtils.isEmpty(currentDir) ? new File(System.getProperty("user.home")) : new File(currentDir);
		directoryChooser.setInitialDirectory(initialDirectory);
	}

	@FXML
	public void chooseSourceDir() {
		configureDirectoryChooser(directoryChooser, "Pasirinkite programinio kodo katalogą", sourceDirField.getText());
		sourceDir = directoryChooser.showDialog(null);
		if (sourceDir != null) {
			sourceDirField.setText(sourceDir.getAbsolutePath());
		}
	}

	@FXML
	public void chooseResultDir() {
		configureDirectoryChooser(directoryChooser, "Pasirinkite rezultato katalogą", resultDirField.getText());
		resultDir = directoryChooser.showDialog(null);
		if (resultDir != null) {
			resultDirField.setText(resultDir.getAbsolutePath());
		}
	}

	@FXML
	public void startJob() {
		if (validationSupport.isInvalid()) {
			Notifications.create().text("Neužpildyti privalomi laukai").showError();
			return;
		}
		Service<Void> worker = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						StatisticsBean sb = StatisticsBean.getInstance();
						FileVisitor visitor = new FileVisitor(sourceDir, fileWildcardField.getText(), recursionLevelField.getInteger());
						JavaCodeParser parser = new JavaCodeParser(visitor, resultDir, sb);
						parser.parse();
						return null;
					}
				};
			}

			@Override
			protected void succeeded() {
				super.succeeded();
			}

			@Override
			protected void failed() {
				getException().printStackTrace();
				super.failed();
			}
		};
		new ProgressDialog("Kuriama ontologija...", worker);
		worker.start();
	}

}
