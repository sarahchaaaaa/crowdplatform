package mlab.mcsweb.client.study.survey;

import java.util.Date;

import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.shared.SurveySummary;

public class SurveyConfigView extends Composite {

	@UiField
	InlineRadio radioOnce, radioDaily, radioWeekly, radioMonthly, radioAlways;

	@UiField
	HTMLPanel configPanel;

	@UiField
	TextBox nameBox;

	@UiField
	Label errorLabel, startErrorLabel, endErrorLabel;

	@UiField
	DateTimePicker startDatePicker, endDatePicker;

	private SurveySummary surveySummary;

	private static SurveyConfigViewUiBinder uiBinder = GWT.create(SurveyConfigViewUiBinder.class);

	interface SurveyConfigViewUiBinder extends UiBinder<Widget, SurveyConfigView> {
	}

	public SurveyConfigView(SurveySummary surveySummary) {
		initWidget(uiBinder.createAndBindUi(this));
		this.surveySummary = surveySummary;

		configPanel.clear();
		if (surveySummary.getId() > 0) {
			nameBox.setText(surveySummary.getName());
			String schedule = surveySummary.getSchedule();
			if (schedule.startsWith("once")) {
				radioOnce.setValue(true, true);
			} else if (schedule.startsWith("daily")) {
				radioDaily.setValue(true);
				configPanel.add(new DailyScheduleView(schedule));
			} else if (schedule.startsWith("weekly")) {
				radioWeekly.setValue(true);
				configPanel.add(new WeeklyScheduleView(schedule));
			} else if (schedule.startsWith("monthly")) {
				radioMonthly.setValue(true);
				configPanel.add(new MonthlyScheduleView(schedule));
			} else if (schedule.startsWith("always")) {
				radioAlways.setValue(true);
			}
			
			try {
				Date startDate = new Date();
				startDate.setTime(Long.parseLong(surveySummary.getStartTime()));
				startDatePicker.setValue(startDate);	
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			try {
				Date endDate = new Date();
				endDate.setTime(Long.parseLong(surveySummary.getEndTime()));
				endDatePicker.setValue(endDate);	
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}

	@UiHandler("radioOnce")
	void radioOneClicked(ClickEvent event) {
		configPanel.clear();
	}

	@UiHandler("radioDaily")
	void dailySelected(ClickEvent event) {
		configPanel.clear();
		configPanel.add(new DailyScheduleView());
	}

	@UiHandler("radioWeekly")
	void weeklySelected(ClickEvent event) {
		configPanel.clear();
		configPanel.add(new WeeklyScheduleView());
	}

	@UiHandler("radioMonthly")
	void monthlySelected(ClickEvent event) {
		configPanel.clear();
		configPanel.add(new MonthlyScheduleView());
	}

	@UiHandler("radioAlways")
	void alwaysSelected(ClickEvent event) {
		configPanel.clear();
	}

	private String getSchedule() {
		String schedule = "";
		if (radioOnce.getValue()) {
			schedule = "once";
		} else if (radioDaily.getValue()) {
			schedule = "daily";
			Widget widget = configPanel.getWidget(0);
			if (widget instanceof DailyScheduleView) {
				schedule = schedule + "|" + ((DailyScheduleView) widget).getScheduleString();
			}
		} else if (radioWeekly.getValue()) {
			schedule = "weekly";
			Widget widget = configPanel.getWidget(0);
			if (widget instanceof WeeklyScheduleView) {
				schedule = schedule + "|" + ((WeeklyScheduleView) widget).getScheduleString();
			}
		} else if (radioMonthly.getValue()) {
			schedule = "monthly";
			Widget widget = configPanel.getWidget(0);
			if (widget instanceof MonthlyScheduleView) {
				schedule = schedule + "|" + ((MonthlyScheduleView) widget).getScheduleString();
			}
		} else if (radioAlways.getValue()) {
			schedule = "always";
		}
		return schedule;
	}

	protected boolean isValidForSaving() {
		errorLabel.setText("");
		if (nameBox.getText().trim().length() == 0) {
			errorLabel.setText("Must have a name");
			return false;
		}
		return true;
	}

	protected boolean isValidToPublish() {
		boolean atLeastOneError = false;
		if (configPanel.getWidgetCount() > 0) {
			Widget widget = configPanel.getWidget(0);
			if (widget instanceof DailyScheduleView) {
				if (!((DailyScheduleView) widget).isValid()) {
					atLeastOneError = true;
				}
			} else if (widget instanceof WeeklyScheduleView) {
				if (!((WeeklyScheduleView) widget).isValid()) {
					atLeastOneError = true;
				}
			} else if (widget instanceof MonthlyScheduleView) {
				if (!((MonthlyScheduleView) widget).isValid()) {
					atLeastOneError = true;
				}
			}
		}
		boolean atLeastOneDateInvalid = false;
		try {
			JsDate jsDate = JsDate.create(startDatePicker.getValue().toString());
			startErrorLabel.setText("");
			// Window.alert("jsdate :" + jsDate.getTimezoneOffset() + ", util:"+
			// JSUtil.getTimezoneOffset());
		} catch (Exception e) {
			// TODO: handle exception
			startErrorLabel.setText("Invalid start date");
			atLeastOneError = true;
			atLeastOneDateInvalid = true;
		}

		try {
			JsDate jsDate = JsDate.create(endDatePicker.getValue().toString());
			endErrorLabel.setText("");
			// Window.alert("jsdate :" + jsDate.getTimezoneOffset() + ", util:"+
			// JSUtil.getTimezoneOffset());
		} catch (Exception e) {
			// TODO: handle exception
			endErrorLabel.setText("Invalid end date");
			atLeastOneError = true;
			atLeastOneDateInvalid = true;
		}

		if (!atLeastOneDateInvalid) {
			if (endDatePicker.getValue().before(startDatePicker.getValue())) {
				startErrorLabel.setText("");
				endErrorLabel.setText("End date should be after start date");
			} else {
				startErrorLabel.setText("");
				endErrorLabel.setText("");
			}
			JsDate jsStartDate = JsDate.create(startDatePicker.getValue().toString());
			JsDate jsEndDate = JsDate.create(endDatePicker.getValue().toString());
			Window.alert("start date: " + startDatePicker.getValue().getTime() + ", js: " + jsStartDate.getTime()
					+ ", offset:" + jsStartDate.getTimezoneOffset());
		}

		errorLabel.setText("");
		if (nameBox.getText().trim().length() == 0) {
			errorLabel.setText("Must have a name");
			atLeastOneError = true;
		}

		if (atLeastOneError) {
			return false;
		}
		return true;

	}

	protected SurveySummary getSurveySummary() {
		surveySummary.setName(nameBox.getText());
		if (startDatePicker.getValue() != null) {
			surveySummary.setStartTime(startDatePicker.getValue().getTime() + "");
			surveySummary.setStartTimeZone(JSUtil.getTimezoneOffset());
		}
		if (endDatePicker.getValue() != null) {
			surveySummary.setEndTime(endDatePicker.getValue().getTime() + "");
			surveySummary.setEndTimeZone(JSUtil.getTimezoneOffset());
		}
		surveySummary.setSchedule(getSchedule());
		return surveySummary;

	}

}
