package mlab.mcsweb.shared;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SurveyConfiguration implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SurveySummary surveySummary;
	private ArrayList<SurveyTask> taskList;
	
	public SurveyConfiguration() {
		// TODO Auto-generated constructor stub
	}

	public SurveyConfiguration(SurveySummary surveySummary, ArrayList<SurveyTask> taskList) {
		super();
		this.surveySummary = surveySummary;
		this.taskList = taskList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SurveySummary getSurveySummary() {
		return surveySummary;
	}

	public ArrayList<SurveyTask> getTaskList() {
		return taskList;
	}

	public void setSurveySummary(SurveySummary surveySummary) {
		this.surveySummary = surveySummary;
	}

	public void setTaskList(ArrayList<SurveyTask> taskList) {
		this.taskList = taskList;
	}
	
	

}
