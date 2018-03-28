package mlab.mcsweb.client.study.sensor;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class SensorEditor extends Composite {
	
	@UiField
	Button saveButton, publishButton;
	
	@UiField
	HTMLPanel sensorConfigPanel, sensorEventPanel;
	
	private boolean isLoaded;

	private static SensorEditorUiBinder uiBinder = GWT.create(SensorEditorUiBinder.class);

	interface SensorEditorUiBinder extends UiBinder<Widget, SensorEditor> {
	}

	public SensorEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		
		saveButton.setSize(ButtonSize.LARGE);
		saveButton.getElement().getStyle().setBorderStyle(BorderStyle.HIDDEN);
		saveButton.setIcon(IconType.SAVE);
		saveButton.setIconPosition(IconPosition.RIGHT);
		
		publishButton.setSize(ButtonSize.LARGE);
		publishButton.getElement().getStyle().setBorderStyle(BorderStyle.HIDDEN);
		publishButton.setIcon(IconType.CLOUD_UPLOAD);
		publishButton.setIconPosition(IconPosition.RIGHT);

	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		if(!isLoaded){
			sensorConfigPanel.clear();
			sensorConfigPanel.add(new SensorConfigEditor());
			
			sensorEventPanel.clear();
			sensorEventPanel.add(new SensorEventEditor());
			isLoaded = true;
		}
	}

}
