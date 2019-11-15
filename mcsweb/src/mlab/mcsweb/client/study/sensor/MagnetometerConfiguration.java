package mlab.mcsweb.client.study.sensor;

import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.shared.SensorAction;

public class MagnetometerConfiguration extends Composite {
	@UiField
	TextBox frequencyText;

	@UiField
	Label errorLabel;
	// @UiField
	// Button collapseButton, timeButton, batteryButton, infoButton;

	@UiField
	Row headerRow;

	@UiField
	HTMLPanel configDetailsPanel, constraintPanel;

	private boolean isCollapsed = true;

	private static MagnetometerConfigurationUiBinder uiBinder = GWT.create(MagnetometerConfigurationUiBinder.class);

	interface MagnetometerConfigurationUiBinder extends UiBinder<Widget, MagnetometerConfiguration> {
	}

	public MagnetometerConfiguration(SensorAction sensorAction) {
		initWidget(uiBinder.createAndBindUi(this));
		if (sensorAction != null) {
			frequencyText.setText(String.valueOf(sensorAction.getFrequency()));
		}

		/*
		 * timeButton.setIcon(IconType.CLOCK_O);
		 * batteryButton.setIcon(IconType.BATTERY_3);
		 * infoButton.setIcon(IconType.INFO);
		 * 
		 * expand();
		 */
	}

	boolean isValid(boolean checked) {
		if (checked) {
			try {
				float frequency = Float.parseFloat(frequencyText.getText().trim());
				if (frequency <= 0) {
					errorLabel.setText("Must be greater than 0");
				} else if (frequency > 100) {
					errorLabel.setText("Must be less than 100");
				} else {
					errorLabel.setText("");
				}
			} catch (Exception e) {
				// TODO: handle exception
				errorLabel.setText("Invalid value");
			}
			if (errorLabel.getText().isEmpty()) {
				return true;
			}
			return false;

		} else {
			return true;
		}
	}

	SensorAction getSensorAction() {
		SensorAction action = new SensorAction();
		action.setSensorActionCode("magnet");
		action.setIsEnabled(1);
		action.setFrequency(Float.parseFloat(frequencyText.getText().trim()));
		return action;

	}

	/*
	 * @UiHandler("timeButton") void addTimeConstraint(ClickEvent event){
	 * constraintPanel.add(new TimeConstraintView()); }
	 * 
	 * @UiHandler("batteryButton") void addEnergyConstraint(ClickEvent event){
	 * constraintPanel.add(new EnergyConstraintView()); }
	 * 
	 * void collapse(){ timeButton.setVisible(false);
	 * batteryButton.setVisible(false); infoButton.setVisible(false);
	 * configDetailsPanel.setVisible(false);
	 * collapseButton.setIcon(IconType.ANGLE_DOWN); isCollapsed = true; } void
	 * expand(){ timeButton.setVisible(true); batteryButton.setVisible(true);
	 * infoButton.setVisible(true); configDetailsPanel.setVisible(true);
	 * collapseButton.setIcon(IconType.ANGLE_UP); isCollapsed = false; }
	 * 
	 * protected void collapseAction(){ if(isCollapsed){ expand(); }else {
	 * collapse(); } }
	 * 
	 * @UiHandler("collapseButton") void collapseButtonClicked(ClickEvent
	 * event){ collapseAction(); }
	 */

}
