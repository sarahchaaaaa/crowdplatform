package mlab.mcsweb.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@XmlRootElement
public class AutoNotInfo implements Serializable, IsSerializable{
	private static final long serialVersionUID = 1L;
	private long studyId;
	private String modifiedBy = "";
	private String modificationTime = "";
	private String modificationTimeZone = "";
	private int days;
	private boolean repeat;
	private String message;
	
	public AutoNotInfo() {
		// TODO Auto-generated constructor stub
	}
	
	
	public AutoNotInfo(long studyId, String modifiedBy, String modificationTime, String modificationTimeZone, int days,
			boolean repeat, String message) {
		super();
		this.studyId = studyId;
		this.modifiedBy = modifiedBy;
		this.modificationTime = modificationTime;
		this.modificationTimeZone = modificationTimeZone;
		this.days = days;
		this.repeat = repeat;
		this.message = message;
	}
	public long getStudyId() {
		return studyId;
	}
	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModificationTime() {
		return modificationTime;
	}
	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}
	public String getModificationTimeZone() {
		return modificationTimeZone;
	}
	public void setModificationTimeZone(String modificationTimeZone) {
		this.modificationTimeZone = modificationTimeZone;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public boolean isRepeat() {
		return repeat;
	}
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}
