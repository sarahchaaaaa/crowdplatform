package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.SurveyConfiguration;
import mlab.mcsweb.shared.SurveySummary;
import mlab.mcsweb.shared.SurveyTask;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("survey")
public interface SurveyService extends RemoteService {
	
	//survey
	Response saveSurveyConfiguration(SurveyConfiguration surveyConfiguration);
	Response publishSurveyConfiguration(SurveyConfiguration surveyConfiguration);
	ArrayList<SurveySummary> getSurveyList(long studyId);
	ArrayList<SurveyTask> getSurveyTaskList(long studyId, long surveyId);
	
	//Task
//	ArrayList<TaskGroupSummary> getTaskGroupList(long studyId);
		
}
