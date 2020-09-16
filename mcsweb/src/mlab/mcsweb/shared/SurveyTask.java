package mlab.mcsweb.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

//@XmlRootElement
public class SurveyTask implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long studyId;
	private long surveyId;
	private int version;
	private int taskId;
	private String taskText = "";
	private String type = "";
	private String possibleInput = "";
	private int orderId;
	private int isActive;
	private int isRequired;
	private int hasComment;
	private int hasUrl;
	private int parentTaskId;
	private int hasChild;
	private String childTriggeringInput = "";
	private String defaultInput = "";
	
	public SurveyTask() {
		// TODO Auto-generated constructor stub
	}

	public SurveyTask(long studyId, long surveyId, int version, int taskId, String taskText, String type,
			String possibleInput, int orderId, int isActive, int isRequired, int hasComment, int hasUrl,
			int parentTaskId, int hasChild, String childTriggeringInput, String defaultInput) {
		super();
		this.studyId = studyId;
		this.surveyId = surveyId;
		this.version = version;
		this.taskId = taskId;
		this.taskText = taskText;
		this.type = type;
		this.possibleInput = possibleInput;
		this.orderId = orderId;
		this.isActive = isActive;
		this.isRequired = isRequired;
		this.hasComment = hasComment;
		this.hasUrl = hasUrl;
		this.parentTaskId = parentTaskId;
		this.hasChild = hasChild;
		this.childTriggeringInput = childTriggeringInput;
		this.defaultInput = defaultInput;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getStudyId() {
		return studyId;
	}

	public long getSurveyId() {
		return surveyId;
	}

	public int getVersion() {
		return version;
	}

	public int getTaskId() {
		return taskId;
	}

	public String getTaskText() {
		return taskText;
	}

	public String getType() {
		return type;
	}

	public String getPossibleInput() {
		return possibleInput;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getIsActive() {
		return isActive;
	}

	public int getIsRequired() {
		return isRequired;
	}

	public int getHasComment() {
		return hasComment;
	}

	public int getHasUrl() {
		return hasUrl;
	}

	public int getParentTaskId() {
		return parentTaskId;
	}

	public int getHasChild() {
		return hasChild;
	}

	public String getChildTriggeringInput() {
		return childTriggeringInput;
	}

	public String getDefaultInput() {
		return defaultInput;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}

	public void setSurveyId(long surveyId) {
		this.surveyId = surveyId;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void setTaskText(String taskText) {
		this.taskText = taskText;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPossibleInput(String possibleInput) {
		this.possibleInput = possibleInput;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public void setIsRequired(int isRequired) {
		this.isRequired = isRequired;
	}

	public void setHasComment(int hasComment) {
		this.hasComment = hasComment;
	}

	public void setHasUrl(int hasUrl) {
		this.hasUrl = hasUrl;
	}

	public void setParentTaskId(int parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public void setHasChild(int hasChild) {
		this.hasChild = hasChild;
	}

	public void setChildTriggeringInput(String childTriggeringInput) {
		this.childTriggeringInput = childTriggeringInput;
	}

	public void setDefaultInput(String defaultInput) {
		this.defaultInput = defaultInput;
	}



}
