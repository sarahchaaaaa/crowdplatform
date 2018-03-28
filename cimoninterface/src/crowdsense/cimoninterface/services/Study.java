package crowdsense.cimoninterface.services;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Study {
	
	private long id;
	private String name = "";
	private String description = "";
	private String createdBy = "";
	private String creationTime = "";
	private String creationTimeZone = "";
	private int state;
	private String modificationTime = "";
	private String modificationTimeZone = "";
	
	public Study() {
		// TODO Auto-generated constructor stub
	}

	public Study(long id, String name, String description, String createdBy, String creationTime,
			String creationTimeZone, int state, String modificationTime, String modificationTimeZone) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdBy = createdBy;
		this.creationTime = creationTime;
		this.creationTimeZone = creationTimeZone;
		this.state = state;
		this.modificationTime = modificationTime;
		this.modificationTimeZone = modificationTimeZone;
	}


	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public String getCreationTimeZone() {
		return creationTimeZone;
	}

	public int getState() {
		return state;
	}

	public String getModificationTime() {
		return modificationTime;
	}

	public String getModificationTimeZone() {
		return modificationTimeZone;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public void setCreationTimeZone(String creationTimeZone) {
		this.creationTimeZone = creationTimeZone;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}

	public void setModificationTimeZone(String modificationTimeZone) {
		this.modificationTimeZone = modificationTimeZone;
	}

	
	
}
