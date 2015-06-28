package com.ernesta;

import java.util.Locale;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.airhacks.afterburner.injection.Injector;
import com.ernesta.view.MainView;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Locale.setDefault(Locale.forLanguageTag("lt"));
			primaryStage.setTitle("OntoJoin");
			MainView view = new MainView();
			Scene scene = new Scene(view.getView(), 640, 480);
			scene.getStylesheets().add(getClass().getResource("view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws Exception {
		Injector.forgetAll();
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
