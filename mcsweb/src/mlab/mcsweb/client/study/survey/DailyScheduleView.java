package mlab.mcsweb.client.study.survey;

import java.util.Date;

import org.gwtbootstrap3.client.ui.IntegerBox;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;

public class DailyScheduleView extends Composite {
	
	@UiField
	Label errorLabel;
	
	
	@UiField
	IntegerBox daysBox, durationBox;
	
	@UiField
	DateTimePicker timePicker;

	private static DailyScheduleViewUiBinder uiBinder = GWT.create(DailyScheduleViewUiBinder.class);

	interface DailyScheduleViewUiBinder extends UiBinder<Widget, DailyScheduleView> {
	}

	public DailyScheduleView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public DailyScheduleView(String schedule) {
		initWidget(uiBinder.createAndBindUi(this));
		String [] tokens = JSUtil.split(schedule, "|");
		if(tokens.length==5){
			int days = 0;
			try {
				days = Integer.parseInt(tokens[1]);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(days==0){
				daysBox.setText("");
			}else {
				daysBox.setText(days + "");
			}
			
			try {
				JsDate jsDate = JsDate.create();
				jsDate.setHours(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
				Date date = new Date();
				date.setTime((long)jsDate.getTime());
				timePicker.setValue(date);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			int duration = 0;
			try {
				duration = Integer.parseInt(tokens[4]);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(duration==0){
				durationBox.setText("");
			}else {
				durationBox.setText(duration+"");
			}
		}
	}

	boolean isValid(){
		errorLabel.setText("");
		int days = 0;
		try {
			days = Integer.parseInt(daysBox.getText().trim());
			if(days>0 && days<10){
				errorLabel.setText("");
			}else {
				errorLabel.setText("Days must be 1-9");
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			errorLabel.setText("Days must be 1-9");
			return false;
		}
		try {
			JsDate.create(timePicker.getValue().toString());
		} catch (Exception e) {
			// TODO: handle exception
			errorLabel.setText("Invalid time");
			return false;
		}
		
		int duration = 0;
		try {
			duration = Integer.parseInt(durationBox.getText().trim());
			if(duration>0 && duration<17){
				//
			}else{
				errorLabel.setText("Duration must be 1-16");
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			errorLabel.setText("Duration must be 1-16");
			return false;
		}
		return true;
	}
	
	public String getScheduleString(){
		String schedule = "";
		int days = 0;
		try {
			days = Integer.parseInt(daysBox.getText().trim());
		} catch (Exception e) {
			// TODO: handle exception
		}
		int hour = 0, minute = 0;
		try {
			JsDate jsDate = JsDate.create(timePicker.getValue().toString());
			hour = jsDate.getHours();
			minute = jsDate.getMinutes();
		} catch (Exception e) {
			// TODO: handle exception
		}
		int duration = 0;
		try {
			duration = Integer.parseInt(durationBox.getText().trim());
		} catch (Exception e) {
			// TODO: handle exception
		}
		schedule = days + "|" + hour + "|" + minute + "|" + duration;
		return schedule;
	}
}
