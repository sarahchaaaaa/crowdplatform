package crowdsense.cimoninterface.services;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SurveyResponse {

	private String userEmail;// email
	private String deviceUuid;
	private long studyId;
	private long surveyId;
	private int taskId;
	private int version;
	private String submissionTime;
	private String submissionTimeZone;
	private String answerType;
	private String answer;
	private String comment;
	private String objectUrl;

	public SurveyResponse() {
		// TODO Auto-generated constructor stub
	}

	public SurveyResponse(String userEmail, String deviceUuid, long studyId, long surveyId, int taskId, int version,
			String submissionTime, String submissionTimeZone, String answerType, String answer, String comment,
			String objectUrl) {
		super();
		this.userEmail = userEmail;
		this.deviceUuid = deviceUuid;
		this.studyId = studyId;
		this.surveyId = surveyId;
		this.taskId = taskId;
		this.version = version;
		this.submissionTime = submissionTime;
		this.submissionTimeZone = submissionTimeZone;
		this.answerType = answerType;
		this.answer = answer;
		this.comment = comment;
		this.objectUrl = objectUrl;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public long getStudyId() {
		return studyId;
	}

	public long getSurveyId() {
		return surveyId;
	}

	public int getTaskId() {
		return taskId;
	}

	public int getVersion() {
		return version;
	}

	public String getSubmissionTime() {
		return submissionTime;
	}

	public String getSubmissionTimeZone() {
		return submissionTimeZone;
	}

	public String getAnswerType() {
		return answerType;
	}

	public String getAnswer() {
		return answer;
	}

	public String getComment() {
		return comment;
	}

	public String getObjectUrl() {
		return objectUrl;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}

	public void setSurveyId(long surveyId) {
		this.surveyId = surveyId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setSubmissionTime(String submissionTime) {
		this.submissionTime = submissionTime;
	}

	public void setSubmissionTimeZone(String submissionTimeZone) {
		this.submissionTimeZone = submissionTimeZone;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setObjectUrl(String objectUrl) {
		this.objectUrl = objectUrl;
	}



}
