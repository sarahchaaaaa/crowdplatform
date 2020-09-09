package mlab.mcsweb.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

//@XmlRootElement
public class FileIdentifier implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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


	public static long getSerialversionuid() {
		return serialVersionUID;
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