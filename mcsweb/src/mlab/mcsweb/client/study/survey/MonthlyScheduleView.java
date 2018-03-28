package mlab.mcsweb.client.study.survey;

import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.IntegerBox;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;

public class MonthlyScheduleView extends Composite {
	
	@UiField
	Label errorLabel;

	@UiField
	Select daySelect, weekSelect;
	
	@UiField
	InlineRadio radioDay, radioSelect;
	
	@UiField
	IntegerBox dayBox, monthBox, altMonthBox;

	private static MonthlyScheduleViewUiBinder uiBinder = GWT.create(MonthlyScheduleViewUiBinder.class);

	interface MonthlyScheduleViewUiBinder extends UiBinder<Widget, MonthlyScheduleView> {
	}

	public MonthlyScheduleView() {
		initWidget(uiBinder.createAndBindUi(this));
		radioDay.setValue(true);
	}
	
	public MonthlyScheduleView(String schedule) {
		initWidget(uiBinder.createAndBindUi(this));
		
		String [] tokens = JSUtil.split(schedule, "|");
		if(tokens.length == 3){
			int day = 0;
			try {
				day = Integer.parseInt(tokens[1]);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(day==0){
				dayBox.setText("");
			}else {
				dayBox.setText(day+"");
			}
			int month = 0;
			try {
				month = Integer.parseInt(tokens[2]);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(month==0){
				monthBox.setText("");
			}else {
				monthBox.setText(month+"");
			}
			radioDay.setValue(true);
		}else if(tokens.length == 4) {
			try {
				weekSelect.setValue(tokens[1]);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			try {
				daySelect.setValue(tokens[2]);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			int month = 0;
			try {
				month = Integer.parseInt(tokens[3]);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(month==0){
				altMonthBox.setText("");
			}else {
				altMonthBox.setText(month+"");
			}
			radioSelect.setValue(true);
		}
	}
	
	public String getScheduleString(){
		String schedule = "";
		if(radioDay.getValue()){
			int day = 0;
			try {
				day = Integer.parseInt(dayBox.getText().trim());
			} catch (Exception e) {
				// TODO: handle exception
			}
			int month = 0;
			try {
				month = Integer.parseInt(monthBox.getText().trim());
			} catch (Exception e) {
				// TODO: handle exception
			}
			schedule = day + "|" + month;
		}else {
			schedule = weekSelect.getValue() + "|" + daySelect.getValue() + "|";
			int month = 0;
			try {
				month = Integer.parseInt(altMonthBox.getText().trim());
			} catch (Exception e) {
				// TODO: handle exception
			}
			schedule += month;
		}
		
		return schedule;
	}
	
	boolean isValid(){
		errorLabel.setText("");
		
		if(radioDay.getValue()){
			int day = 0;
			try {
				day = Integer.parseInt(dayBox.getText().trim());
				if(day<1 || day>31){
					errorLabel.setText("Day must be 1-31");
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				errorLabel.setText("Day must be 1-31");
				return false;
			}
			int month = 0;
			try {
				month = Integer.parseInt(monthBox.getText().trim());
				if(month<1 || month>12){
					errorLabel.setText("Month must be 1-12");
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				errorLabel.setText("Day must be 1-12");
				return false;
			}
			return true;
		}else {
			int month = 0;
			try {
				month = Integer.parseInt(altMonthBox.getText().trim());
				if(month<1 || month>12){
					errorLabel.setText("Month must be 1-12");
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				errorLabel.setText("Month must be 1-12");
				return false;
			}
			return true;
			
		}
	}

}
