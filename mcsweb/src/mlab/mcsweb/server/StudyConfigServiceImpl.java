package mlab.mcsweb.server;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

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

import mlab.mcsweb.client.StudyConfigurationService;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.SurveyConfiguration;
import mlab.mcsweb.shared.SurveySummary;
import mlab.mcsweb.shared.SurveyTask;
import mlab.mcsweb.shared.TaskGroupSummary;

/**
 * The server-side implementation of the RPC service.
 */	
@SuppressWarnings("serial")
public class StudyConfigServiceImpl extends RemoteServiceServlet implements StudyConfigurationService {

	static String serverRoot = "";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
//	private static MyConnectionPool connectionPool = null;

	private static String dbUrl, username, password;

	
	Connection connection;

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		serverRoot = config.getServletContext().getInitParameter("serverRoot");
		System.out.println("Server root :" + serverRoot);
		
        Properties properties = new Properties();
        InputStream propertiesInputStream = getServletContext().getResourceAsStream("/WEB-INF/system.properties"); 
        		//DatabaseUtil.class.getClassLoader().getResourceAsStream("system.properties");
        if(propertiesInputStream == null){
        	System.out.println("properties is null");
        }else{
        	System.out.println("found properties stream");
        }
        try {
            properties.load(propertiesInputStream);
    		dbUrl=properties.getProperty("db_host")+"/"+properties.getProperty("db_schema");
    		username =properties.getProperty("db_username");
    		password = properties.getProperty("db_password");
    		System.out.println("db prop, dburl:"+ dbUrl + ", user:"+ username + ", pass:" + password);
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	private Connection connect(){
		try {
	       return DriverManager.getConnection(dbUrl,username,password);			
		} catch (Exception e) {
			// TODO: handle exception
		}
        return null;
	}

	/*
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
	*/
	
	/*
	@Override
	public ArrayList<SurveySummary> getSurveyList(long studyId) {
		
//		String url = serverRoot + "/survey/list/" + studyId;
//		String response = genericGetMethod(url);
//		ArrayList<SurveySummary> surveyList = new Gson().fromJson(response, new TypeToken<ArrayList<SurveySummary>>(){}.getType());
//		return surveyList;
		
		ArrayList<SurveySummary> surveyList = new ArrayList<>();

		
		try {
			System.out.println("study id:" + studyId);
			connection = connect();
					//DatabaseUtil.getInstance().connectToDatabase();
			String query = "select * from mcs.survey_summary where study_id=" + studyId
					+ " order by modification_time desc";
			if(connection == null){
				System.out.println("connection is null");
			}else{
				System.out.println("not null connection");
			}
			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("query:" + query);
			while (resultSet.next()) {
				try {

					SurveySummary surveySummary = new SurveySummary();
					int id = resultSet.getInt("id");
					String name = resultSet.getString("name");
					String description = resultSet.getString("description");
					String modificationTime = resultSet.getString("modification_time");
					String modificationTimeZone = resultSet.getString("modification_time_zone");
					String publishTime = resultSet.getString("publish_time");
					String publishTimeZone = resultSet.getString("publish_time_zone");
					int publishedVersion = resultSet.getInt("published_version");
					int state = resultSet.getInt("state");
					int responseCount = resultSet.getInt("response_count");
					String startTime = resultSet.getString("start_time");
					String startTimeZone = resultSet.getString("start_time_zone");
					String endTime = resultSet.getString("end_time");
					String endTimeZone = resultSet.getString("end_time_zone");
					String schedule = resultSet.getString("schedule");

					surveySummary.setId(id);
					surveySummary.setStudyId(studyId);
					surveySummary.setName(name);
					surveySummary.setDescription(description);
					surveySummary.setModificationTime(modificationTime);
					surveySummary.setModificationTimeZone(modificationTimeZone);
					surveySummary.setPublishedVersion(publishedVersion);
					surveySummary.setPublishTime(publishTime);
					surveySummary.setPublishTimeZone(publishTimeZone);
					surveySummary.setState(state);
					surveySummary.setResponseCount(responseCount);
					surveySummary.setStartTime(startTime);
					surveySummary.setStartTimeZone(startTimeZone);
					surveySummary.setEndTime(endTime);
					surveySummary.setEndTimeZone(endTimeZone);
					surveySummary.setSchedule(schedule);

					surveyList.add(surveySummary);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("Closing the connection.");
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}
		}

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
	*/

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
