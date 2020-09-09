package mlab.mcsweb.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

//@XmlRootElement
public class AutoNotificationInfo implements Serializable, IsSerializable{
	private static final long serialVersionUID = 1L;
	private long studyId;
	private String modifiedBy = "";
	private String modificationTime = "";
	private String modificationTimeZone = "";
	private double baseInterval;
	private double increaseFactor;
	private int numberOfPushAtAStage;
	private int attemptsBeforeNotify;
	private int daysBeforeNotify;
	private String notifyBooleanOperator;
	private int attemptsBeforeTerminate;
	private int daysBeforeTerminate;
	private String terminateBooleanOperator;
	private String notificationTitle;
	private String notificationMessage;
	private int active;
	
	public AutoNotificationInfo() {
		// TODO Auto-generated constructor stub
	}

	public AutoNotificationInfo(long studyId, String modifiedBy, String modificationTime, String modificationTimeZone,
			double baseInterval, double increaseFactor, int numberOfPushAtAStage, int attemptsBeforeNotify,
			int daysBeforeNotify, String notifyBooleanOperator, int attemptsBeforeTerminate, int daysBeforeTerminate,
			String terminateBooleanOperator, String notificationTitle, String notificationMessage, int active) {
		super();
		this.studyId = studyId;
		this.modifiedBy = modifiedBy;
		this.modificationTime = modificationTime;
		this.modificationTimeZone = modificationTimeZone;
		this.baseInterval = baseInterval;
		this.increaseFactor = increaseFactor;
		this.numberOfPushAtAStage = numberOfPushAtAStage;
		this.attemptsBeforeNotify = attemptsBeforeNotify;
		this.daysBeforeNotify = daysBeforeNotify;
		this.notifyBooleanOperator = notifyBooleanOperator;
		this.attemptsBeforeTerminate = attemptsBeforeTerminate;
		this.daysBeforeTerminate = daysBeforeTerminate;
		this.terminateBooleanOperator = terminateBooleanOperator;
		this.notificationTitle = notificationTitle;
		this.notificationMessage = notificationMessage;
		this.active = active;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getStudyId() {
		return studyId;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public String getModificationTime() {
		return modificationTime;
	}

	public String getModificationTimeZone() {
		return modificationTimeZone;
	}

	public double getBaseInterval() {
		return baseInterval;
	}

	public double getIncreaseFactor() {
		return increaseFactor;
	}

	public int getNumberOfPushAtAStage() {
		return numberOfPushAtAStage;
	}

	public int getAttemptsBeforeNotify() {
		return attemptsBeforeNotify;
	}

	public int getDaysBeforeNotify() {
		return daysBeforeNotify;
	}

	public String getNotifyBooleanOperator() {
		return notifyBooleanOperator;
	}

	public int getAttemptsBeforeTerminate() {
		return attemptsBeforeTerminate;
	}

	public int getDaysBeforeTerminate() {
		return daysBeforeTerminate;
	}

	public String getTerminateBooleanOperator() {
		return terminateBooleanOperator;
	}

	public String getNotificationTitle() {
		return notificationTitle;
	}

	public String getNotificationMessage() {
		return notificationMessage;
	}

	public int getActive() {
		return active;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}

	public void setModificationTimeZone(String modificationTimeZone) {
		this.modificationTimeZone = modificationTimeZone;
	}

	public void setBaseInterval(double baseInterval) {
		this.baseInterval = baseInterval;
	}

	public void setIncreaseFactor(double increaseFactor) {
		this.increaseFactor = increaseFactor;
	}

	public void setNumberOfPushAtAStage(int numberOfPushAtAStage) {
		this.numberOfPushAtAStage = numberOfPushAtAStage;
	}

	public void setAttemptsBeforeNotify(int attemptsBeforeNotify) {
		this.attemptsBeforeNotify = attemptsBeforeNotify;
	}

	public void setDaysBeforeNotify(int daysBeforeNotify) {
		this.daysBeforeNotify = daysBeforeNotify;
	}

	public void setNotifyBooleanOperator(String notifyBooleanOperator) {
		this.notifyBooleanOperator = notifyBooleanOperator;
	}

	public void setAttemptsBeforeTerminate(int attemptsBeforeTerminate) {
		this.attemptsBeforeTerminate = attemptsBeforeTerminate;
	}

	public void setDaysBeforeTerminate(int daysBeforeTerminate) {
		this.daysBeforeTerminate = daysBeforeTerminate;
	}

	public void setTerminateBooleanOperator(String terminateBooleanOperator) {
		this.terminateBooleanOperator = terminateBooleanOperator;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public void setActive(int active) {
		this.active = active;
	}
	
	
}
