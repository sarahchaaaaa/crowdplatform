package mlab.mcsweb.client.events;

import mlab.mcsweb.shared.LabelSummary;

public class LabelState {
	public enum LabelSpecificState {
		NEW, EDITED, SAVED, PUBLISHED, EXIT;
	}

	// private Study study;
	// private String surveyId;
	private LabelSummary labelSummary;
	private LabelSpecificState labelSpecificState;

	public LabelState(LabelSummary labelSummary, LabelSpecificState labelSpecificState) {
		super();
		// this.study = study;
		// this.surveyId = surveyId;
		this.labelSummary = labelSummary;
		this.labelSpecificState = labelSpecificState;
	}

	public LabelSpecificState getLabelSpecificState() {
		return labelSpecificState;
	}

	public LabelSummary getLabelSummary() {
		return labelSummary;
	}

}
