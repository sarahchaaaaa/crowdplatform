package mlab.mcsweb.shared;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

//@XmlRootElement
public class SensorConfiguration implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SensorSummary sensorSummary;
	private ArrayList<SensorAction> actionList;
	
	public SensorConfiguration() {
		// TODO Auto-generated constructor stub
	}

	public SensorConfiguration(SensorSummary sensorSummary, ArrayList<SensorAction> actionList) {
		super();
		this.sensorSummary = sensorSummary;
		this.actionList = actionList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SensorSummary getSensorSummary() {
		return sensorSummary;
	}

	public ArrayList<SensorAction> getActionList() {
		return actionList;
	}

	public void setSensorSummary(SensorSummary sensorSummary) {
		this.sensorSummary = sensorSummary;
	}

	public void setActionList(ArrayList<SensorAction> actionList) {
		this.actionList = actionList;
	}



}
