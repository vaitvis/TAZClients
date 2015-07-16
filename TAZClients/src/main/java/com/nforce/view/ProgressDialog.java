package com.nforce.view;

import javafx.concurrent.Service;

import org.controlsfx.dialog.Dialogs;

@SuppressWarnings("deprecation")
public class ProgressDialog {
	
	public ProgressDialog(String text, Service<?> service) {
		Dialogs dialog = Dialogs.create();
		dialog.title("Vykdoma...");
		dialog.message(text);
		dialog.showWorkerProgress(service);
	}
}
