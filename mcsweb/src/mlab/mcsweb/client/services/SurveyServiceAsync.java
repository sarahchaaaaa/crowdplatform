package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.SurveyConfiguration;
import mlab.mcsweb.shared.SurveySummary;
import mlab.mcsweb.shared.SurveyTask;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SurveyServiceAsync {

	//survey
	void saveSurveyConfiguration(SurveyConfiguration surveyConfiguration, AsyncCallback<Response> callback);
	void publishSurveyConfiguration(SurveyConfiguration surveyConfiguration, AsyncCallback<Response> callback);
	void getSurveyList(long studyId, AsyncCallback<ArrayList<SurveySummary>> callback);
	void getSurveyTaskList(long studyId, long surveyId, AsyncCallback<ArrayList<SurveyTask>> callback);
	
	//Task
	//void getTaskGroupList(long studyId, AsyncCallback<ArrayList<TaskGroupSummary>> callback);
	
}
