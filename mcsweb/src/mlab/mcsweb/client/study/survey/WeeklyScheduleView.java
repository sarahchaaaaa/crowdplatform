package mlab.mcsweb.client.study.survey;

import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.IntegerBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;

public class WeeklyScheduleView extends Composite {
	
	@UiField
	IntegerBox weekBox;
	
	@UiField
	Label errorLabel;
	
	
	@UiField
	CheckBox monCheckbox, tueCheckbox, wedCheckbox, thuCheckbox, friCheckbox, satCheckbox, sunCheckbox;

	private static WeeklyScheduleViewUiBinder uiBinder = GWT.create(WeeklyScheduleViewUiBinder.class);

	interface WeeklyScheduleViewUiBinder extends UiBinder<Widget, WeeklyScheduleView> {
	}

	public WeeklyScheduleView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public WeeklyScheduleView(String schedule) {
		initWidget(uiBinder.createAndBindUi(this));
		String [] tokens = JSUtil.split(schedule, "|");
		int weeks = 0;
		try {
			weeks = Integer.parseInt(tokens[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(weeks==0){
			weekBox.setText("");
		}else {
			weekBox.setText(weeks+"");
		}
		if(tokens.length>2){
			String [] days = JSUtil.split(tokens[2], ",");
			for(int i=0;i<days.length;i++){
				try {
					int day = Integer.parseInt(days[i]);
					if(day==1){
						monCheckbox.setValue(true);
					}else if (day==2) {
						tueCheckbox.setValue(true);
					}else if (day==3) {
						wedCheckbox.setValue(true);
					}else if (day==4) {
						thuCheckbox.setValue(true);
					}else if (day==5) {
						friCheckbox.setValue(true);
					}else if (day==6) {
						satCheckbox.setValue(true);
					}else if (day==7) {
						sunCheckbox.setValue(true);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

	public String getScheduleString(){
		int weeks = 0;
		try {
			weeks = Integer.parseInt(weekBox.getText().trim());
		} catch (Exception e) {
			// TODO: handle exception
		}
		String schedule = weeks + "|";
		if(monCheckbox.getValue()){
			schedule += "1,";
		}
		if(tueCheckbox.getValue()){
			schedule += "2,";
		}
		if(wedCheckbox.getValue()){
			schedule += "3,";
		}
		if(thuCheckbox.getValue()){
			schedule += "4,";
		}
		if(friCheckbox.getValue()){
			schedule += "5,";
		}
		if(satCheckbox.getValue()){
			schedule += "6,";
		}
		if(sunCheckbox.getValue()){
			schedule += "7,";
		}
		if(schedule.endsWith(",")){
			schedule = schedule.substring(0, schedule.length()-1);
		}else if (schedule.endsWith("|")) {
			schedule += "0";
		}

		return schedule;
	}

	boolean isValid(){
		boolean isAnyChecked = false;
		if(monCheckbox.getValue() || tueCheckbox.getValue() || wedCheckbox.getValue() || thuCheckbox.getValue() || 
				friCheckbox.getValue() || satCheckbox.getValue() || sunCheckbox.getValue()){
			isAnyChecked = true;
		}
		
		if(isAnyChecked){
			errorLabel.setText("");
		}else {
			errorLabel.setText("Select at least one day.");
			return false;
		}
		
		int weeks = 0;
		try {
			weeks = Integer.parseInt(weekBox.getText().trim());
			if(weeks>0 && weeks<10){
				errorLabel.setText("");
				return true;
			}else {
				errorLabel.setText("Weeks must be 1-9");
			}
		} catch (Exception e) {
			// TODO: handle exception
			errorLabel.setText("Weeks must be 1-9");
		}
		return false;
	}
}
