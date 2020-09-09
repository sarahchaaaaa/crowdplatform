package mlab.mcsweb.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

//@XmlRootElement
public class FileObjectInfo implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long studyId;
	private String email = "";
	private String uuid = "";
	private String name = "";
	private String readyToUploadTime = "";
	private String uploadTime = "";
	private String deleteTime = "";
	
	public FileObjectInfo() {
		// TODO Auto-generated constructor stub
	}


	public FileObjectInfo(long studyId, String email, String uuid, String name, String readyToUploadTime,
			String uploadTime, String deleteTime) {
		super();
		this.studyId = studyId;
		this.email = email;
		this.uuid = uuid;
		this.name = name;
		this.readyToUploadTime = readyToUploadTime;
		this.uploadTime = uploadTime;
		this.deleteTime = deleteTime;
	}






	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public long getStudyId() {
		return studyId;
	}


	public String getEmail() {
		return email;
	}


	public String getUuid() {
		return uuid;
	}


	public String getName() {
		return name;
	}


	public String getReadyToUploadTime() {
		return readyToUploadTime;
	}


	public String getUploadTime() {
		return uploadTime;
	}


	public String getDeleteTime() {
		return deleteTime;
	}


	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setReadyToUploadTime(String readyToUploadTime) {
		this.readyToUploadTime = readyToUploadTime;
	}


	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}


	public void setDeleteTime(String deleteTime) {
		this.deleteTime = deleteTime;
	}


	
}