package services.study;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SensorConfiguration {
	
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
