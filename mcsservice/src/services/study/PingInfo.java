package services.study;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PingInfo {
	
	private String email = "";
	private String uuid = "";
	private String time = "";
	private String network = "";
	private String osType = "";
	private String osVersion = "";
	private String appVersion = "";
	private String data = "";

	public PingInfo() {
		// TODO Auto-generated constructor stub
	}

	public PingInfo(String email, String uuid, String time, String network, String osType, String osVersion,
			String appVersion, String data) {
		super();
		this.email = email;
		this.uuid = uuid;
		this.time = time;
		this.network = network;
		this.osType = osType;
		this.osVersion = osVersion;
		this.appVersion = appVersion;
		this.data = data;
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

	public String getNetwork() {
		return network;
	}

	public String getOsType() {
		return osType;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public String getData() {
		return data;
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

	public void setNetwork(String network) {
		this.network = network;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public void setData(String data) {
		this.data = data;
	}
}
