package mlab.mcsweb.server;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import mlab.mcsweb.client.GreetingService;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.Response;
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
	
	
	//sensor
	@Override
	public ArrayList<SensorSummary> getSensorConfigList(long studyId) {
		String url = serverRoot + "/study/"+ studyId +"/sensorconfig/list";
		String response = genericGetMethod(url);
		ArrayList<SensorSummary> sensorConfigList = new Gson().fromJson(response, new TypeToken<ArrayList<SensorSummary>>(){}.getType());
		return sensorConfigList;
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
		String url = serverRoot + "/study/"+ studyId +"/participant/all/list";
		String response = genericGetMethod(url);
		ArrayList<Participant> list = new Gson().fromJson(response, new TypeToken<ArrayList<Participant>>(){}.getType());
		return list;
	}
	
	@Override
	public Response deleteParticipants(long studyId, String list) {
		String url = serverRoot + "/study/"+ studyId +"/participant/delete/" + list;
		String result = genericGetMethod(url);
		Response response = new Gson().fromJson(result, Response.class);
		return response;
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
