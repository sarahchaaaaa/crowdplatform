package mlab.mcsweb.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@XmlRootElement
public class CloudStorageInfo implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long studyId;
	private String createdBy = "";
	private String modificationTime = "";
	private String modificationTimeZone = "";
	private String type = "";
	private String parameter = "";
	
	public CloudStorageInfo() {
		// TODO Auto-generated constructor stub
	}

	public CloudStorageInfo(long studyId, String createdBy, String modificationTime, String modificationTimeZone,
			String type, String parameter) {
		super();
		this.studyId = studyId;
		this.createdBy = createdBy;
		this.modificationTime = modificationTime;
		this.modificationTimeZone = modificationTimeZone;
		this.type = type;
		this.parameter = parameter;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getStudyId() {
		return studyId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getModificationTime() {
		return modificationTime;
	}

	public String getModificationTimeZone() {
		return modificationTimeZone;
	}

	public String getType() {
		return type;
	}

	public String getParameter() {
		return parameter;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}

	public void setModificationTimeZone(String modificationTimeZone) {
		this.modificationTimeZone = modificationTimeZone;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

}