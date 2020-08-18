package mlab.mcsweb.client.events;

import mlab.mcsweb.shared.LabelingSummary;

public class LabelState {
	public enum LabelSpecificState {
		NEW, EDITED, SAVED, PUBLISHED, EXIT;
	}

	// private Study study;
	// private String surveyId;
	private LabelingSummary labelingSummary;
	private LabelSpecificState labelSpecificState;

	public LabelState(LabelingSummary labelingSummary, LabelSpecificState labelSpecificState) {
		super();
		// this.study = study;
		// this.surveyId = surveyId;
		this.labelingSummary = labelingSummary;
		this.labelSpecificState = labelSpecificState;
	}

	public LabelSpecificState getLabelSpecificState() {
		return labelSpecificState;
	}

	public LabelingSummary getLabelSummary() {
		return labelingSummary;
	}

}
