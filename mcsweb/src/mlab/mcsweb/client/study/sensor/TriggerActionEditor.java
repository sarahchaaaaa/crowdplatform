package mlab.mcsweb.client.study.sensor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TriggerActionEditor extends Composite {

	private static TriggerActionEditorUiBinder uiBinder = GWT.create(TriggerActionEditorUiBinder.class);

	interface TriggerActionEditorUiBinder extends UiBinder<Widget, TriggerActionEditor> {
	}

	public TriggerActionEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
