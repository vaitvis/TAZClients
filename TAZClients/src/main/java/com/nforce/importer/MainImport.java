package com.nforce.importer;

import java.util.function.Function;

import javafx.beans.property.ObjectProperty;

import com.airhacks.afterburner.injection.Injector;

public class MainImport {
	
	protected ObjectProperty<Object> presenterProperty;
	protected final Function<String, Object> injectionContext;
	
	public MainImport() {
        this(f -> null);
    }
	
	public MainImport(Function<String, Object> injectionContext) {
        this.injectionContext = injectionContext;
    }
	
	public void launch() {
		ImportClients importClients = (ImportClients) Injector.instantiatePresenter(ImportClients.class);
		importClients.doJob();
	}
	
	static public void main(String[] args) {
		MainImport mainImport = new MainImport();
		mainImport.launch();
		mainImport.cleanup();
	}
	
	public void cleanup() {
        Injector.forgetAll();
    }
}
