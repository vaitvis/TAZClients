package com.nforce.model;

import java.util.List;

import javafx.scene.control.Button;

public interface ToolBarListener {

	void clear();
	
	void addButtons(List<Button> buttons);
	
}
