package mlab.mcsweb.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@XmlRootElement
public class SensorAction implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long studyId;
	private long sensorConfigId;
	private int version;
	private String sensorActionCode = "";
	private String type = "";
	private int isEnabled;
	private float frequency;
	private String timeBound = "";
	private String batteryBound = "";
	private String param1 = "";
	private String param2 = "";
	private String param3 = "";
	
	public SensorAction() {
		// TODO Auto-generated constructor stub
	}

	public SensorAction(long studyId, long sensorConfigId, int version, String sensorActionCode, String type,
			int isEnabled, float frequency, String timeBound, String batteryBound, String param1, String param2,
			String param3) {
		super();
		this.studyId = studyId;
		this.sensorConfigId = sensorConfigId;
		this.version = version;
		this.sensorActionCode = sensorActionCode;
		this.type = type;
		this.isEnabled = isEnabled;
		this.frequency = frequency;
		this.timeBound = timeBound;
		this.batteryBound = batteryBound;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getStudyId() {
		return studyId;
	}

	public long getSensorConfigId() {
		return sensorConfigId;
	}

	public int getVersion() {
		return version;
	}

	public String getSensorActionCode() {
		return sensorActionCode;
	}

	public String getType() {
		return type;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public float getFrequency() {
		return frequency;
	}

	public String getTimeBound() {
		return timeBound;
	}

	public String getBatteryBound() {
		return batteryBound;
	}

	public String getParam1() {
		return param1;
	}

	public String getParam2() {
		return param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}

	public void setSensorConfigId(long sensorConfigId) {
		this.sensorConfigId = sensorConfigId;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setSensorActionCode(String sensorActionCode) {
		this.sensorActionCode = sensorActionCode;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public void setTimeBound(String timeBound) {
		this.timeBound = timeBound;
	}

	public void setBatteryBound(String batteryBound) {
		this.batteryBound = batteryBound;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}
	
	

}
