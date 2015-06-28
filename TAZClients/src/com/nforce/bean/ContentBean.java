package com.nforce.bean;

import javax.inject.Singleton;

import com.airhacks.afterburner.views.FXMLView;
import com.nforce.model.ContentListener;

@Singleton
public class ContentBean {

	private ContentListener listener;
	
	public void setListener(ContentListener listener) {
		this.listener = listener;
	}
	
	public void changeContent(FXMLView view) {
		if(listener != null) {
			listener.changeContent(view);
		}
	}
	
	public void goBack() {
		if(listener != null) {
			listener.goBack();
		}
	}
}
