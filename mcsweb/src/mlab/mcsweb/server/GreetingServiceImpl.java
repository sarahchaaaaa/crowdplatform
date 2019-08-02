package mlab.mcsweb.server;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import mlab.mcsweb.client.GreetingService;
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
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	static String serverRoot = "";

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		serverRoot = config.getServletContext().getInitParameter("serverRoot");
		System.out.println("Server root :" + serverRoot);
	}

	@Override
	public Response signup(User user) {
		String url = serverRoot + "/auth/signup";
		System.out.println("url:" + url);
		return genericPostMethod(url, user);
	}

	@Override
	public Response login(User user) {
		String url = serverRoot + "/auth/login";
		System.out.println("url:" + url);
		return genericPostMethod(url, user);
	}
	
	@Override
	public Response createStudy(Study study) {
		String url = serverRoot + "/study/create";
		return genericPostMethod(url, study);
	}
	
	//sensor
	@Override
	public Response saveSensorConfiguration(SensorConfiguration sensorConfiguration) {
		String url = serverRoot + "/study/0/sensorconfig/save";
		System.out.println("save url:"+ url + ", " + sensorConfiguration.getSensorSummary().getId());
		return genericPostMethod(url, sensorConfiguration);
	}
	
	@Override
	public Response publishSensorConfiguration(SensorConfiguration sensorConfiguration) {
		String url = serverRoot + "/study/0/sensorconfig/publish";
		System.out.println("publish url:"+ url + ", " + sensorConfiguration.getSensorSummary().getId());
		return genericPostMethod(url, sensorConfiguration);
	}
	
	//survey
	@Override
	public Response saveSurveyConfiguration(SurveyConfiguration surveyConfig) {
		String url = serverRoot + "/survey/save/config";
		System.out.println("save url:"+ url + ", " + surveyConfig.getSurveySummary().getId());
		return genericPostMethod(url, surveyConfig);
	}
	
	@Override
	public Response publishSurveyConfiguration(SurveyConfiguration surveyConfiguration) {
		String url = serverRoot + "/survey/publish/config";
		System.out.println("publish url:"+ url + ", " + surveyConfiguration.getSurveySummary().getId());
		return genericPostMethod(url, surveyConfiguration);
	}
	
	//participation
	@Override
	public Response addParticipant(Participant participant) {
		String url = serverRoot + "/study/"+ participant.getStudyId() +"/participation/add";
		System.out.println("publish url:"+ url + ", " + participant.getStudyId() + ", " + participant.getUserEmail());
		return genericPostMethod(url, participant);
	}
	
	@Override
	public Response editParticipant(String currentEmail, Participant detailsToUpdate) {
		String url = serverRoot + "/study/"+ detailsToUpdate.getStudyId() +"/participation/update/" + currentEmail;
		System.out.println("publish url:"+ url + ", " + detailsToUpdate.getStudyId() + ", " + detailsToUpdate.getUserEmail());
		return genericPostMethod(url, detailsToUpdate);
	}
	
	
	private Response genericPostMethod(String url, Object genericObject) {
		long start = Calendar.getInstance().getTimeInMillis();
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(url);
		String result = service.accept(MediaType.APPLICATION_JSON).post(String.class, genericObject);
		Response response = new Gson().fromJson(result, Response.class);

		System.out.println("response code generic post method: " + response.getCode());
		long end = Calendar.getInstance().getTimeInMillis();
		System.out.println("time diff generic post call: "+ url + ", time to complete: " + (end - start) + "\n");

		return response;

	}
	
	@Override
	public ArrayList<Study> getStudyList(String email) {
		String url = serverRoot + "/study/mylist/"+email;
		String response = genericGetMethod(url);
		ArrayList<Study> studyList = new Gson().fromJson(response, new TypeToken<ArrayList<Study>>(){}.getType());
		return studyList;
	}
	
	//dashboard
	@Override
	public ArrayList<FileIdentifier> getFileIdentifiers(long studyId) {
		String url = serverRoot + "/study/"+ studyId +"/dashboard/filemapping";
		String response = genericGetMethod(url);
		ArrayList<FileIdentifier> list = new Gson().fromJson(response, new TypeToken<ArrayList<FileIdentifier>>(){}.getType());
		return list;
	}
	
	
	//sensor
	@Override
	public ArrayList<SensorSummary> getSensorConfigList(long studyId) {
		String url = serverRoot + "/study/"+ studyId +"/sensorconfig/list";
		System.out.println("going to call get , url:"+ url);
		String response = genericGetMethod(url);
		ArrayList<SensorSummary> sensorConfigList = new Gson().fromJson(response, new TypeToken<ArrayList<SensorSummary>>(){}.getType());
		return sensorConfigList;
	}
	
	@Override
	public ArrayList<SensorAction> getSensorActionList(long studyId, long sensorConfigId) {
		String url = serverRoot + "/study/"+ studyId +"/sensorconfig/"+ sensorConfigId + "/actionlist";
		System.out.println("going to call get , url:"+ url);
		String response = genericGetMethod(url);
		ArrayList<SensorAction> actionList = new Gson().fromJson(response, new TypeToken<ArrayList<SensorAction>>(){}.getType());
		return actionList;
	}
	
	
	//survey
	
	@Override
	public ArrayList<SurveySummary> getSurveyList(long studyId) {
		String url = serverRoot + "/survey/list/" + studyId;
		String response = genericGetMethod(url);
		ArrayList<SurveySummary> surveyList = new Gson().fromJson(response, new TypeToken<ArrayList<SurveySummary>>(){}.getType());
		return surveyList;
	}
	
	@Override
	public ArrayList<SurveyTask> getSurveyTaskList(long studyId, long surveyId) {
		String url = serverRoot + "/survey/tasklist/" + studyId + "/" + surveyId;
		System.out.println("request url: "+ url);
		String response = genericGetMethod(url);
		ArrayList<SurveyTask> taskList = new Gson().fromJson(response, new TypeToken<ArrayList<SurveyTask>>(){}.getType());
		return taskList;
	}
	
	@Override
	public ArrayList<TaskGroupSummary> getTaskGroupList(long studyId) {
		String url = serverRoot + "/taskgroup/list/" + studyId;
		String response = genericGetMethod(url);
		ArrayList<TaskGroupSummary> taskGroupList = new Gson().fromJson(response, new TypeToken<ArrayList<TaskGroupSummary>>(){}.getType());
		return taskGroupList;
	}
	
	
	@Override
	public ArrayList<Participant> getAllParticipants(long studyId) {
		String url = serverRoot + "/study/"+ studyId +"/participation/list";
		String response = genericGetMethod(url);
		ArrayList<Participant> list = new Gson().fromJson(response, new TypeToken<ArrayList<Participant>>(){}.getType());
		return list;
	}
	
	@Override
	public Response deleteParticipants(long studyId, String list) {
		list = Base64.encodeBase64String(list.getBytes());
		String url = serverRoot + "/study/"+ studyId +"/participation/delete/" + list;
		String result = genericGetMethod(url);
		Response response = new Gson().fromJson(result, Response.class);
		return response;
	}
	
	@Override
	public ArrayList<PingInfo> getPingHistory(long studyId, String email, int days) {
		String url = serverRoot + "/study/"+ studyId +"/participation/pinghistory?email=" + email + "&days=" + days;
		String response = genericGetMethod(url);
		ArrayList<PingInfo> list = new Gson().fromJson(response, new TypeToken<ArrayList<PingInfo>>(){}.getType());
		return list;
	}
	
	private String genericGetMethod(String url){
		long start = Calendar.getInstance().getTimeInMillis();

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(url);

		String response = service.accept(MediaType.APPLICATION_JSON).get(String.class);
		System.out.println(url+ ",  response :"+response);
		long end = Calendar.getInstance().getTimeInMillis();
		System.out.println("time diff generic get call: "+ url + ", time to execute:" + (end - start) + "\n");
		return response;

	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
