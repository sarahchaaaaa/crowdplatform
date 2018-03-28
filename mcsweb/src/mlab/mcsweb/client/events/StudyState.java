package mlab.mcsweb.client.events;

import mlab.mcsweb.shared.Study;

public class StudyState {
	public enum StudySpecificState {
		CREATED, GETDETAILS, HOME;
	}

	private Study study;
	private StudySpecificState studySpecificState;
	public StudyState(Study study, StudySpecificState studySpecificState) {
		super();
		this.study = study;
		this.studySpecificState = studySpecificState;
	}
	public Study getStudy() {
		return study;
	}
	public StudySpecificState getStudySpecificState() {
		return studySpecificState;
	}
	
	

}
