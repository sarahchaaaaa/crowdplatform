package mlab.mcsweb.client.study.sensor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SensorEventEditor extends Composite {

	private static SensorEventEditorUiBinder uiBinder = GWT.create(SensorEventEditorUiBinder.class);

	interface SensorEventEditorUiBinder extends UiBinder<Widget, SensorEventEditor> {
	}

	public SensorEventEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
