package mlab.mcsweb.client.study.sensor;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.CheckBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.shared.SensorAction;

public class PhoneEventEditor extends Composite{
	
	@UiField
	CheckBox displayEventCheckbox, screenEventCheckbox, batteryLevelCheckbox, batteryStateCheckbox, networkEventCheckbox, callEventCheckbox;

	private static PhoneEventEditorUiBinder uiBinder = GWT.create(PhoneEventEditorUiBinder.class);

	interface PhoneEventEditorUiBinder extends UiBinder<Widget, PhoneEventEditor> {
	}

	public PhoneEventEditor(ArrayList<SensorAction> actionList) {
		initWidget(uiBinder.createAndBindUi(this));
		
		if(actionList != null && actionList.size()>0){
			for(SensorAction action:actionList){
				if(action.getSensorActionCode().equalsIgnoreCase("display")){
					displayEventCheckbox.setValue(true);
				}else if(action.getSensorActionCode().equalsIgnoreCase("screen")){
					screenEventCheckbox.setValue(true);
				}else if(action.getSensorActionCode().equalsIgnoreCase("batterylevel")){
					batteryLevelCheckbox.setValue(true);
				}else if(action.getSensorActionCode().equalsIgnoreCase("batterystate")){
					batteryStateCheckbox.setValue(true);
				}else if (action.getSensorActionCode().equalsIgnoreCase("network")) {
					networkEventCheckbox.setValue(true);
				}else if(action.getSensorActionCode().equalsIgnoreCase("call")){
					callEventCheckbox.setValue(true);
				}
			}			
		}
	}

	
	private boolean isDisplayChecked(){
		return displayEventCheckbox.getValue();
	}
	private boolean isScreenChecked(){
		return screenEventCheckbox.getValue();
	}
	private boolean isBatteryLevelChecked(){
		return batteryLevelCheckbox.getValue();
	}
	private boolean isBatteryStateChecked(){
		return batteryStateCheckbox.getValue();
	}
	private boolean isNetworkChecked(){
		return networkEventCheckbox.getValue();
	}
	private boolean isCallChecked(){
		return callEventCheckbox.getValue();
	}
	
	ArrayList<SensorAction> getEventActionList(){
		ArrayList<SensorAction> list = new ArrayList<>();
		if(isDisplayChecked()){
			SensorAction action = new SensorAction();
			action.setIsEnabled(1);
			action.setSensorActionCode("display");
			list.add(action);
		}
		
		if(isScreenChecked()){
			SensorAction action = new SensorAction();
			action.setIsEnabled(1);
			action.setSensorActionCode("screen");
			list.add(action);			
		}
		
		if(isBatteryLevelChecked()){
			SensorAction action = new SensorAction();
			action.setIsEnabled(1);
			action.setSensorActionCode("batterylevel");
			list.add(action);
		}
		
		if(isBatteryStateChecked()){
			SensorAction action = new SensorAction();
			action.setIsEnabled(1);
			action.setSensorActionCode("batterystate");
			list.add(action);
		}
		
		if(isNetworkChecked()){
			SensorAction action = new SensorAction();
			action.setIsEnabled(1);
			action.setSensorActionCode("network");
			list.add(action);
		}
		
		if(isCallChecked()){
			SensorAction action = new SensorAction();
			action.setIsEnabled(1);
			action.setSensorActionCode("call");
			list.add(action);
		}
		return list;
	}
}
