package com.ernesta.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.ernesta.model.RulesManager;
import com.ernesta.rules.FirstNameContractionRule;
import com.ernesta.service.OntologyMerger;

public class MainPresenter implements Initializable {

	@FXML
	private TextField sourceFileField;

	@Inject
	private RulesManager rulesManager;

	private final FileChooser fileChooser = new FileChooser();
	private File sourceFile;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rulesManager.addPersonRule(new FirstNameContractionRule());
	}

	private void configureFileChooser(final FileChooser fileChooser, final String title, String currentDir) {
		fileChooser.setTitle(title);
		File initialDirectory = StringUtils.isEmpty(currentDir) ? new File(System.getProperty("user.home")) : new File(currentDir).getParentFile();
		fileChooser.setInitialDirectory(initialDirectory);
	}

	@FXML
	public void chooseSourceFile() {
		configureFileChooser(fileChooser, "Pasirinkite failą", sourceFileField.getText());
		sourceFile = fileChooser.showOpenDialog(null);
		if (sourceFile != null) {
			sourceFileField.setText(sourceFile.getAbsolutePath());
		}
	}

	@FXML
	public void doMerge() {
		Service<Void> worker = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						if (sourceFile != null) {
							OntologyMerger merger = new OntologyMerger(sourceFile);
							merger.merge();
						}
						return null;
					}
				};
			}

			@Override
			protected void failed() {
				getException().printStackTrace();
				super.failed();
			}
		};
		new ProgressDialog("Skaitoma ontologija", worker);
		worker.start();
	}
}
