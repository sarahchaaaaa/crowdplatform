package services.study;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileIdentifier {
	
	private String email = "";
	private String uuid = "";

	public FileIdentifier() {
		// TODO Auto-generated constructor stub
	}

	public FileIdentifier(String email, String uuid) {
		super();
		this.email = email;
		this.uuid = uuid;
	}

	public String getEmail() {
		return email;
	}

	public String getUuid() {
		return uuid;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
}
