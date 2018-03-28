package crowdsense.cimoninterface.services;

import java.util.ArrayList;

//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class SurveyResponseList {
	
	ArrayList<SurveyResponse> responseList;
	
	public SurveyResponseList() {
		// TODO Auto-generated constructor stub
	}

	public SurveyResponseList(ArrayList<SurveyResponse> responseList) {
		super();
		this.responseList = responseList;
	}

	public ArrayList<SurveyResponse> getResponseList() {
		return responseList;
	}

	public void setResponseList(ArrayList<SurveyResponse> responseList) {
		this.responseList = responseList;
	}

	
}
