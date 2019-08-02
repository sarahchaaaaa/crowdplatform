package mlab.mcsweb.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
	
	//dashboard
	ArrayList<FileIdentifier> getFileIdentifiers(long studyId);
	
	//participant
	ArrayList<Participant> getAllParticipants(long studyId);
	Response addParticipant(Participant participant);
	Response editParticipant(String currentEmail, Participant detailsToUpdate);
	Response deleteParticipants(long studyId, String list);
	ArrayList<PingInfo> getPingHistory(long studyId, String email, int days);
	
	
	//sensor
	Response saveSensorConfiguration(SensorConfiguration sensorConfiguration);
	Response publishSensorConfiguration(SensorConfiguration sensorConfiguration);
	ArrayList<SensorSummary> getSensorConfigList(long studyId);
	ArrayList<SensorAction> getSensorActionList(long studyId, long sensorConfigId);
	
	//survey
	Response saveSurveyConfiguration(SurveyConfiguration surveyConfiguration);
	Response publishSurveyConfiguration(SurveyConfiguration surveyConfiguration);
	ArrayList<SurveySummary> getSurveyList(long studyId);
	ArrayList<SurveyTask> getSurveyTaskList(long studyId, long surveyId);
	
	//Task
	ArrayList<TaskGroupSummary> getTaskGroupList(long studyId);
	
	
}
