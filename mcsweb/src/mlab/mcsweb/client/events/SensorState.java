package mlab.mcsweb.client.events;

import mlab.mcsweb.shared.SensorSummary;

public class SensorState {
	public enum SensorSpecificState {
		NEW, EDITED, SAVED, PUBLISHED, EXIT;
	}

	// private Study study;
	// private String surveyId;
	private SensorSummary sensorSummary;
	private SensorSpecificState sensorSpecificState;

	public SensorState(SensorSummary sensorSummary, SensorSpecificState sensorSpecificState) {
		super();
		// this.study = study;
		// this.surveyId = surveyId;
		this.sensorSummary = sensorSummary;
		this.sensorSpecificState = sensorSpecificState;
	}

	public SensorSummary getSensorSummary() {
		return sensorSummary;
	}

	public SensorSpecificState getSensorSpecificState() {
		return sensorSpecificState;
	}

}
