package com.nforce;

import java.util.Locale;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.airhacks.afterburner.injection.Injector;
import com.nforce.view.RootLayoutView;

public class MainApp extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		Locale.setDefault(Locale.forLanguageTag("lt"));
        primaryStage.setTitle("TAZClients");
        primaryStage.setMaximized(true);
        
    	RootLayoutView rootView = new RootLayoutView();            
        Scene scene = new Scene(rootView.getView());
        primaryStage.setScene(scene);

        primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		Injector.forgetAll();
		super.stop();
	}
	
	static public void main(String[] args) {
		launch(args);
	}
}
