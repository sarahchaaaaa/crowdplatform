package mlab.mcsweb.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@XmlRootElement
public class LabelingInfo implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long studyId;
	private String email = "";
	private String uuid = "";
	private String time = "";
	private String label = "";
	private String type = "";
	
	public LabelingInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public LabelingInfo(long studyId, String email, String uuid, String time, String label, String type) {
		super();
		this.studyId = studyId;
		this.email = email;
		this.uuid = uuid;
		this.time = time;
		this.label = label;
		this.type = type;
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

	public String getTime() {
		return time;
	}

	public String getLabel() {
		return label;
	}

	public String getType() {
		return type;
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

	public void setTime(String time) {
		this.time = time;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}