package com.nforce.bean;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;

import javax.inject.Singleton;

import com.nforce.model.ToolBarListener;

@Singleton
public class ToolBarBean {

	private ToolBarListener listener;	
	private List<Button> buttons = new ArrayList<Button>();
	
	public void clear() {
		buttons.clear();
		if(listener != null) {
			listener.clear();
		}
	}
	
	public void addButton(Button button) {
		buttons.add(button);
	}
	
	public void commitButtons() {
		if(listener != null) {
			listener.addButtons(buttons);
		}
	}
	
	public void setListener(ToolBarListener listener) {
		this.listener = listener;
	}
}
