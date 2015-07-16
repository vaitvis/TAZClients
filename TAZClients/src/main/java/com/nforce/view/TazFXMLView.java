package com.nforce.view;

import javafx.scene.Parent;

import com.airhacks.afterburner.views.FXMLView;
import com.nforce.model.ToolBarButtonsAware;

public abstract class TazFXMLView extends FXMLView {

	@Override
	public Parent getView() {
		Parent view = super.getView();
		if(getPresenter() instanceof TazInitializable) {
			((TazInitializable) getPresenter()).realInit();
		}
		if(getPresenter() instanceof ToolBarButtonsAware) {
			((ToolBarButtonsAware) getPresenter()).initToolBarButtons();
		}
		return view;
	}
	
}
