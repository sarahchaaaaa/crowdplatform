package mlab.mcsweb.client.study.sensor;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.CheckBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.shared.SensorAction;

public class PhoneSensorEditor extends Composite {
	
	@UiField
	HTMLPanel locationConfigPanel, accelConfigPanel, gyroConfigPanel, magnetConfigPanel, deviceMotionConfigPanel, pressureConfigPanel;
	
	@UiField
	CheckBox locationCheckbox, accelCheckbox, gyroCheckbox, magnetCheckbox, devicemotionCheckbox, pressureCheckbox;
	
	private LocationConfiguration locationConifg;
	private AccelerometerConfiguration accelConfig;
	private GyroscopeConfiguration gyroConfig;
	private MagnetometerConfiguration magnetConfig;
	private DeviceMotionConfiguration devicemotionConfig;
	private PressureConfiguration pressureConfig;

	private static PhoneSensorEditorUiBinder uiBinder = GWT.create(PhoneSensorEditorUiBinder.class);

	interface PhoneSensorEditorUiBinder extends UiBinder<Widget, PhoneSensorEditor> {
	}

	public PhoneSensorEditor(ArrayList<SensorAction> actionList) {
		initWidget(uiBinder.createAndBindUi(this));
		
		locationConifg = new LocationConfiguration(null);
		accelConfig = new AccelerometerConfiguration(null);
		gyroConfig = new GyroscopeConfiguration(null);
		magnetConfig = new MagnetometerConfiguration(null);
		devicemotionConfig = new DeviceMotionConfiguration(null);
		pressureConfig = new PressureConfiguration(null);

		if(actionList != null && actionList.size()>0){
			for(SensorAction action:actionList){
				if(action.getSensorActionCode().equalsIgnoreCase("location")){
					locationCheckbox.setValue(true);
					locationConifg = new LocationConfiguration(action);
				}else if(action.getSensorActionCode().equalsIgnoreCase("accel")){
					accelCheckbox.setValue(true);
					accelConfig = new AccelerometerConfiguration(action);
				}else if(action.getSensorActionCode().equalsIgnoreCase("gyro")){
					gyroCheckbox.setValue(true);
					gyroConfig = new GyroscopeConfiguration(action);
				}else if(action.getSensorActionCode().equalsIgnoreCase("magnet")){
					magnetCheckbox.setValue(true);
					magnetConfig = new MagnetometerConfiguration(action);
				}else if (action.getSensorActionCode().equalsIgnoreCase("devicemotion")) {
					devicemotionCheckbox.setValue(true);
					devicemotionConfig = new DeviceMotionConfiguration(action);
				}else if(action.getSensorActionCode().equalsIgnoreCase("pressure")){
					pressureCheckbox.setValue(true);
					pressureConfig = new PressureConfiguration(action);
				}
			}			
		}
		
		//locationConfigPanel.clear();
		locationConfigPanel.add(locationConifg);
		
		//accelConfigPanel.clear();
		accelConfigPanel.add(accelConfig);
		
		//gyroConfigPanel.clear();
		gyroConfigPanel.add(gyroConfig);
		
		//magnetConfigPanel.clear();
		magnetConfigPanel.add(magnetConfig);
		
		//deviceMotionConfigPanel.clear();
		deviceMotionConfigPanel.add(devicemotionConfig);
		
		//pressureConfigPanel.clear();
		pressureConfigPanel.add(pressureConfig);
		
	}

	
	private boolean isLocationChecked(){
		return locationCheckbox.getValue();
	}
	
	
	private boolean isAccelChecked(){
		return accelCheckbox.getValue();
	}
	
	private boolean isGyroChecked(){
		return gyroCheckbox.getValue();
	}
	
	private boolean isMagnetChecked(){
		return magnetCheckbox.getValue();
	}
	
	private boolean isDevicemotionChecked(){
		return devicemotionCheckbox.getValue();
	}
	private boolean isPressureChecked(){
		return pressureCheckbox.getValue();
	}
	
	boolean isValid(){
		return (locationConifg.isValid(isLocationChecked()) && accelConfig.isValid(isAccelChecked()) 
				&& gyroConfig.isValid(isGyroChecked()) && magnetConfig.isValid(isMagnetChecked())
				&& devicemotionConfig.isValid(isDevicemotionChecked()) && pressureConfig.isValid(isPressureChecked()));
	}
	
	ArrayList<SensorAction> getSensorActionList(){
		ArrayList<SensorAction> list = new ArrayList<>();
		if(isLocationChecked()){
			list.add(locationConifg.getSensorAction());
		}
		if(isAccelChecked()){
			list.add(accelConfig.getSensorAction());
		}
		if(isGyroChecked()){
			list.add(gyroConfig.getSensorAction());
		}
		if(isMagnetChecked()){
			list.add(magnetConfig.getSensorAction());
		}
		if(isDevicemotionChecked()){
			list.add(devicemotionConfig.getSensorAction());
		}
		if(isPressureChecked()){
			list.add(pressureConfig.getSensorAction());
		}
		return list;
		
	}
}
