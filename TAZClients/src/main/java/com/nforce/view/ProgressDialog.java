package com.nforce.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ProgressDialog {

	private Dialog dialog;

	public ProgressDialog(String text, Service<?> service) {
		if (service != null
                && (service.getState() == Worker.State.CANCELLED
                || service.getState() == Worker.State.FAILED
                || service.getState() == Worker.State.SUCCEEDED)) {
            return;
        }
		dialog = new Dialog();
		dialog.setTitle("Vykdoma...");
		dialog.setHeaderText(text);

		VBox vBox = new VBox(10);

		Label statusLabel = new Label();
		statusLabel.textProperty().bind(service.messageProperty());
		statusLabel.setMinWidth(400);

		ProgressBar progressBar = new ProgressBar(ProgressIndicator.INDETERMINATE_PROGRESS);
		progressBar.setMinWidth(400);

		vBox.getChildren().addAll(statusLabel, progressBar);

		dialog.getDialogPane().setContent(vBox);
		ButtonType buttonType = new ButtonType("Atšaukti", ButtonBar.ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().add(buttonType);
		Node cancelButton = dialog.getDialogPane().lookupButton(buttonType);
		cancelButton.setVisible(false);

		service.stateProperty().addListener(new ChangeListener<Worker.State>() {
			@Override
			public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State oldState, Worker.State newState) {
				switch(newState) {
                    case CANCELLED:
                    case FAILED:
                    case SUCCEEDED:
                        dialog.close();
                        break;
					case SCHEDULED:
						dialog.show();
						break;
                }
			}
		});
	}
}
