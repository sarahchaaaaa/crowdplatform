package mlab.mcsweb.client.events;

import mlab.mcsweb.shared.SurveySummary;

public class SurveyState {
	public enum SurveySpecificState {
		NEW, EDITED, SAVED, PUBLISHED, EXIT;
	}

	// private Study study;
	// private String surveyId;
	private SurveySummary surveySummary;
	private SurveySpecificState surveySpecificState;

	public SurveyState(SurveySummary surveySummary, SurveySpecificState surveySpecificState) {
		super();
		// this.study = study;
		// this.surveyId = surveyId;
		this.surveySummary = surveySummary;
		this.surveySpecificState = surveySpecificState;
	}

	public SurveySummary getSurveySummary() {
		return surveySummary;
	}

	public SurveySpecificState getSurveySpecificState() {
		return surveySpecificState;
	}

}
