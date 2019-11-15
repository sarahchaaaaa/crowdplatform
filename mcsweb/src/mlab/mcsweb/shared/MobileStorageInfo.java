package mlab.mcsweb.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@XmlRootElement
public class MobileStorageInfo implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long studyId;
	private String modifiedBy = "";
	private String modificationTime = "";
	private String modificationTimeZone = "";
	private int fileObjectSize;
	private int maxFiles;
	private int maxCapacity;
	
	public MobileStorageInfo() {
		// TODO Auto-generated constructor stub
	}

	public MobileStorageInfo(long studyId, String modifiedBy, String modificationTime, String modificationTimeZone,
			int fileObjectSize, int maxFiles, int maxCapacity) {
		super();
		this.studyId = studyId;
		this.modifiedBy = modifiedBy;
		this.modificationTime = modificationTime;
		this.modificationTimeZone = modificationTimeZone;
		this.fileObjectSize = fileObjectSize;
		this.maxFiles = maxFiles;
		this.maxCapacity = maxCapacity;
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

	public int getFileObjectSize() {
		return fileObjectSize;
	}

	public int getMaxFiles() {
		return maxFiles;
	}

	public int getMaxCapacity() {
		return maxCapacity;
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

	public void setFileObjectSize(int fileObjectSize) {
		this.fileObjectSize = fileObjectSize;
	}

	public void setMaxFiles(int maxFiles) {
		this.maxFiles = maxFiles;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	
	

}