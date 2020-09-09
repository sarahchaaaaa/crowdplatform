package mlab.mcsweb.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

//@XmlRootElement
public class DataUploadInfo implements Serializable, IsSerializable{
	private static final long serialVersionUID = 1L;
	private long studyId;
	private String modifiedBy = "";
	private String modificationTime = "";
	private String modificationTimeZone = "";
	private String network;
	private String battery;
	private int frequency;
	
	public DataUploadInfo() {
		// TODO Auto-generated constructor stub
	}
	
	
	public DataUploadInfo(long studyId, String modifiedBy, String modificationTime, String modificationTimeZone,
			String network, String battery, int frequency) {
		super();
		this.studyId = studyId;
		this.modifiedBy = modifiedBy;
		this.modificationTime = modificationTime;
		this.modificationTimeZone = modificationTimeZone;
		this.network = network;
		this.battery = battery;
		this.frequency = frequency;
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
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getBattery() {
		return battery;
	}
	public void setBattery(String battery) {
		this.battery = battery;
	}
	
	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	

}
