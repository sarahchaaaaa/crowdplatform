package crowdsense.cimoninterface.services;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SurveySummary {
	
	private long id;
	private long studyId;
	private String name = "";
	private String description = "";
	private String createdBy = "";
	private String creationTime = "";
	private String creationTimeZone = "";
	private String modificationTime = "";
	private String modificationTimeZone = "";
	private String publishTime = "";
	private String publishTimeZone = "";
	private int publishedVersion;
	private int state;
	private int responseCount;
	private String startTime = "";
	private String startTimeZone = "";
	private String endTime = "";
	private String endTimeZone = "";
	private String schedule = "";
	
	public SurveySummary() {
		// TODO Auto-generated constructor stub
	}

	public SurveySummary(long id, long studyId, String name, String description, String createdBy, String creationTime,
			String creationTimeZone, String modificationTime, String modificationTimeZone, String publishTime,
			String publishTimeZone, int publishedVersion, int state, int responseCount, String startTime,
			String startTimeZone, String endTime, String endTimeZone, String schedule) {
		super();
		this.id = id;
		this.studyId = studyId;
		this.name = name;
		this.description = description;
		this.createdBy = createdBy;
		this.creationTime = creationTime;
		this.creationTimeZone = creationTimeZone;
		this.modificationTime = modificationTime;
		this.modificationTimeZone = modificationTimeZone;
		this.publishTime = publishTime;
		this.publishTimeZone = publishTimeZone;
		this.publishedVersion = publishedVersion;
		this.state = state;
		this.responseCount = responseCount;
		this.startTime = startTime;
		this.startTimeZone = startTimeZone;
		this.endTime = endTime;
		this.endTimeZone = endTimeZone;
		this.schedule = schedule;
	}

	public long getId() {
		return id;
	}

	public long getStudyId() {
		return studyId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public String getCreationTimeZone() {
		return creationTimeZone;
	}

	public String getModificationTime() {
		return modificationTime;
	}

	public String getModificationTimeZone() {
		return modificationTimeZone;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public String getPublishTimeZone() {
		return publishTimeZone;
	}

	public int getPublishedVersion() {
		return publishedVersion;
	}

	public int getState() {
		return state;
	}

	public int getResponseCount() {
		return responseCount;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getStartTimeZone() {
		return startTimeZone;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getEndTimeZone() {
		return endTimeZone;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public void setCreationTimeZone(String creationTimeZone) {
		this.creationTimeZone = creationTimeZone;
	}

	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}

	public void setModificationTimeZone(String modificationTimeZone) {
		this.modificationTimeZone = modificationTimeZone;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public void setPublishTimeZone(String publishTimeZone) {
		this.publishTimeZone = publishTimeZone;
	}

	public void setPublishedVersion(int publishedVersion) {
		this.publishedVersion = publishedVersion;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setResponseCount(int responseCount) {
		this.responseCount = responseCount;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setStartTimeZone(String startTimeZone) {
		this.startTimeZone = startTimeZone;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setEndTimeZone(String endTimeZone) {
		this.endTimeZone = endTimeZone;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	
}