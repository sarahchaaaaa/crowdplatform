package mlab.mcsweb.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@XmlRootElement
public class Participant implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long studyId;
	private String userEmail = "";
	private String firstName = "";
	private String lastName = "";
	private String organization = "";
	private String status = "";
	
	public Participant() {
		// TODO Auto-generated constructor stub
	}


	public Participant(long studyId, String userEmail, String firstName, String lastName, String organization,
			String status) {
		super();
		this.studyId = studyId;
		this.userEmail = userEmail;
		this.firstName = firstName;
		this.lastName = lastName;
		this.organization = organization;
		this.status = status;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public long getStudyId() {
		return studyId;
	}


	public String getUserEmail() {
		return userEmail;
	}


	public String getFirstName() {
		return firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public String getOrganization() {
		return organization;
	}


	public String getStatus() {
		return status;
	}


	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public void setOrganization(String organization) {
		this.organization = organization;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
}