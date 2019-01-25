package mlab.mcsweb.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.SurveyConfiguration;
import mlab.mcsweb.shared.SurveySummary;
import mlab.mcsweb.shared.SurveyTask;
import mlab.mcsweb.shared.TaskGroupSummary;
import mlab.mcsweb.shared.User;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("study")
public interface StudyConfigurationService extends RemoteService {
	
	
	/*
	//survey
	Response saveSurveyConfiguration(SurveyConfiguration surveyConfiguration);
	Response publishSurveyConfiguration(SurveyConfiguration surveyConfiguration);
	ArrayList<SurveySummary> getSurveyList(long studyId);
	ArrayList<SurveyTask> getSurveyTaskList(long studyId, long surveyId);

	
	//Task
	ArrayList<TaskGroupSummary> getTaskGroupList(long studyId);
	*/
	
}
