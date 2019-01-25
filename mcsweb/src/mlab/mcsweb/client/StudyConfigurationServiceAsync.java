package mlab.mcsweb.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.SurveyConfiguration;
import mlab.mcsweb.shared.SurveySummary;
import mlab.mcsweb.shared.SurveyTask;
import mlab.mcsweb.shared.TaskGroupSummary;
import mlab.mcsweb.shared.User;

/**
 * The async counterpart of <code>StudyConfigurationService</code>.
 */
public interface StudyConfigurationServiceAsync {
	
	/*
	//survey
	void saveSurveyConfiguration(SurveyConfiguration surveyConfiguration, AsyncCallback<Response> callback);
	void publishSurveyConfiguration(SurveyConfiguration surveyConfiguration, AsyncCallback<Response> callback);
	void getSurveyList(long studyId, AsyncCallback<ArrayList<SurveySummary>> callback);
	void getSurveyTaskList(long studyId, long surveyId, AsyncCallback<ArrayList<SurveyTask>> callback);
	
	//Task
	void getTaskGroupList(long studyId, AsyncCallback<ArrayList<TaskGroupSummary>> callback);
	*/
}
