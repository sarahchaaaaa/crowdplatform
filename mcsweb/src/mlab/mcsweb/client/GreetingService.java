package mlab.mcsweb.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.SurveyConfiguration;
import mlab.mcsweb.shared.SurveySummary;
import mlab.mcsweb.shared.SurveyTask;
import mlab.mcsweb.shared.User;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	
	//auth
	Response signup(User user);
	Response login(User user);
	
	//study
	Response createStudy(Study study);
	ArrayList<Study> getStudyList(String email);
	
	//survey
	Response saveSurveyConfiguration(SurveyConfiguration surveyConfiguration);
	Response publishSurveyConfiguration(SurveyConfiguration surveyConfiguration);
	ArrayList<SurveySummary> getSurveyList(long studyId);
	ArrayList<SurveyTask> getSurveyTaskList(long studyId, long surveyId);
	
}
