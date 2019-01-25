package mlab.mcsweb.client.study.sensor;

import org.gwtbootstrap3.client.ui.CheckBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class SensorConfigEditor extends Composite {
	
	@UiField
	HTMLPanel locationConfigPanel, accelConfigPanel, gyroConfigPanel, magnetConfigPanel, deviceMotionConfigPanel;
	
	@UiField
	CheckBox locationCheckBox;
	
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
			
			accelConfigPanel.clear();
			accelConfigPanel.add(new AccelerometerConfiguration());
			
			gyroConfigPanel.clear();
			gyroConfigPanel.add(new GyroscopeConfiguration());
			
			magnetConfigPanel.clear();
			magnetConfigPanel.add(new MagnetometerConfiguration());
			
			deviceMotionConfigPanel.clear();
			deviceMotionConfigPanel.add(new DeviceMotionConfiguration());
			isLoaded = true;
		}
	}
	
	@UiHandler("locationCheckBox")
	void locationClicked(ClickEvent event){
		Window.alert("location :" + locationCheckBox.getValue());
		if(locationCheckBox.getValue()){
			try {
				Widget widget = locationConfigPanel.getWidget(0);
				((LocationConfiguration)widget).expand();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else {
			try {
				Widget widget = locationConfigPanel.getWidget(0);
				((LocationConfiguration)widget).collapse();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}

}
