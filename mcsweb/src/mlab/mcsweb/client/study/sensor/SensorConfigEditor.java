package mlab.mcsweb.client.study.sensor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class SensorConfigEditor extends Composite {
	
	@UiField
	HTMLPanel locationConfigPanel;
	
	private boolean isLoaded;

	private static SensorConfigEditorUiBinder uiBinder = GWT.create(SensorConfigEditorUiBinder.class);

	interface SensorConfigEditorUiBinder extends UiBinder<Widget, SensorConfigEditor> {
	}

	public SensorConfigEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		if(!isLoaded){
			locationConfigPanel.clear();
			locationConfigPanel.add(new LocationConfiguration());
			isLoaded = true;
		}
	}

}
