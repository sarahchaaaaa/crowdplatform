package mlab.mcsweb.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.FileIdentifier;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.PingInfo;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.SensorAction;
import mlab.mcsweb.shared.SensorConfiguration;
import mlab.mcsweb.shared.SensorSummary;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.SurveyConfiguration;
import mlab.mcsweb.shared.SurveySummary;
import mlab.mcsweb.shared.SurveyTask;
import mlab.mcsweb.shared.TaskGroupSummary;
import mlab.mcsweb.shared.User;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	//auth
	void signup(User user, AsyncCallback<Response> callback);
	void login(User user, AsyncCallback<Response> callback);
	
	//study
	void createStudy(Study study, AsyncCallback<Response> callback);
	void getStudyList(String email, AsyncCallback<ArrayList<Study>> callback);
	
	//dashboard
	void getFileIdentifiers(long studyId, AsyncCallback<ArrayList<FileIdentifier>> callback);
	
	//participant
	void getAllParticipants(long studyId, AsyncCallback<ArrayList<Participant>> callback);
	void addParticipant(Participant participant, AsyncCallback<Response> callback);
	void editParticipant(String currentEmail, Participant detailsToUpdate, AsyncCallback<Response> callback);
	void deleteParticipants(long studyId, String list, AsyncCallback<Response> callback);
	void getPingHistory(long studyId, String email, int days, AsyncCallback<ArrayList<PingInfo>> callback);

	//sensor
	void saveSensorConfiguration(SensorConfiguration sensorConfiguration, AsyncCallback<Response> callback);
	void publishSensorConfiguration(SensorConfiguration sensorConfiguration, AsyncCallback<Response> callback);
	void getSensorConfigList(long studyId, AsyncCallback<ArrayList<SensorSummary>> callback);
	void getSensorActionList(long studyId, long sensorConfigId, AsyncCallback<ArrayList<SensorAction>> callback);
	
	
	//survey
	void saveSurveyConfiguration(SurveyConfiguration surveyConfiguration, AsyncCallback<Response> callback);
	void publishSurveyConfiguration(SurveyConfiguration surveyConfiguration, AsyncCallback<Response> callback);
	void getSurveyList(long studyId, AsyncCallback<ArrayList<SurveySummary>> callback);
	void getSurveyTaskList(long studyId, long surveyId, AsyncCallback<ArrayList<SurveyTask>> callback);
	
	//Task
	void getTaskGroupList(long studyId, AsyncCallback<ArrayList<TaskGroupSummary>> callback);
	
}
