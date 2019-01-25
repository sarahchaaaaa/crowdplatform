package crowdsense.cimoninterface.services;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PushRegistry {

	private String userEmail;// email
	private String deviceUuid;
	private String puskToken;
	private String lastRegisterTime;
	private String lastRegisterTimeZone;
	private int active;

	public PushRegistry() {
		// TODO Auto-generated constructor stub
	}

	public PushRegistry(String userEmail, String deviceUuid, String puskToken, String lastRegisterTime,
			String lastRegisterTimeZone, int active) {
		super();
		this.userEmail = userEmail;
		this.deviceUuid = deviceUuid;
		this.puskToken = puskToken;
		this.lastRegisterTime = lastRegisterTime;
		this.lastRegisterTimeZone = lastRegisterTimeZone;
		this.active = active;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public String getPuskToken() {
		return puskToken;
	}

	public String getLastRegisterTime() {
		return lastRegisterTime;
	}

	public String getLastRegisterTimeZone() {
		return lastRegisterTimeZone;
	}

	public int getActive() {
		return active;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public void setPuskToken(String puskToken) {
		this.puskToken = puskToken;
	}

	public void setLastRegisterTime(String lastRegisterTime) {
		this.lastRegisterTime = lastRegisterTime;
	}

	public void setLastRegisterTimeZone(String lastRegisterTimeZone) {
		this.lastRegisterTimeZone = lastRegisterTimeZone;
	}

	public void setActive(int active) {
		this.active = active;
	}


}
