package mlab.mcsweb.server;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import mlab.mcsweb.client.services.SurveyService;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.SurveyConfiguration;
import mlab.mcsweb.shared.SurveySummary;
import mlab.mcsweb.shared.SurveyTask;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SurveyServiceImpl extends RemoteServiceServlet implements SurveyService {

	static String serverRoot = "";
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static String dbUrl, username, password;

	static {
		try {
			Class.forName(DRIVER).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		serverRoot = config.getServletContext().getInitParameter("serverRoot");
		System.out.println("Server root :" + serverRoot);

		Properties properties = new Properties();
		InputStream inputStream = getServletContext().getResourceAsStream("/WEB-INF/system.properties");

		try {
			properties.load(inputStream);
			dbUrl = properties.getProperty("db_host") + "/" + properties.getProperty("db_schema");
			username = properties.getProperty("db_username");
			password = properties.getProperty("db_password");
			System.out.println("db prop, dburl:" + dbUrl + ", user:" + username + ", pass:" + password);

		} catch (Exception e) {
		}

	}

	private Connection connect() {
		try {
			return DriverManager.getConnection(dbUrl, username, password);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	

	// survey
	private Response updateSurvey(SurveyConfiguration surveyConfig, int workingVersion) {
		Response response = new Response();
		Connection connection = null;
		try {
			SurveySummary survey = surveyConfig.getSurveySummary();
			ArrayList<SurveyTask> taskList = surveyConfig.getTaskList();

			connection = connect();
			PreparedStatement preparedStatement = null;

			String query = "";
			if (survey.getId() > 0) {
				query = "update mcs.survey_summary set name=?, description=?, modification_time=?, modification_time_zone=?, "
						+ " publish_time=?, publish_time_zone=?, published_version=?, state=?, start_time=?, start_time_zone=?, end_time=?, end_time_zone=?, "
						+ " schedule=? where id=? and study_id=?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, survey.getName());
				preparedStatement.setString(2, survey.getDescription());
				preparedStatement.setString(3, survey.getModificationTime());
				preparedStatement.setString(4, survey.getModificationTimeZone());
				preparedStatement.setString(5, survey.getPublishTime());
				preparedStatement.setString(6, survey.getPublishTimeZone());
				preparedStatement.setInt(7, survey.getPublishedVersion());
				preparedStatement.setInt(8, survey.getState());
				preparedStatement.setString(9, survey.getStartTime());
				preparedStatement.setString(10, survey.getStartTimeZone());
				preparedStatement.setString(11, survey.getEndTime());
				preparedStatement.setString(12, survey.getEndTimeZone());
				preparedStatement.setString(13, survey.getSchedule());

				preparedStatement.setLong(14, survey.getId());
				preparedStatement.setLong(15, survey.getStudyId());

				preparedStatement.execute();

			} else {
				query = "insert into mcs.survey_summary (study_id, name, description, created_by, creation_time, creation_time_zone,"
						+ " modification_time, modification_time_zone, publish_time, publish_time_zone, published_version, state, start_time, start_time_zone,"
						+ " end_time, end_time_zone, schedule) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setLong(1, survey.getStudyId());
				preparedStatement.setString(2, survey.getName());
				preparedStatement.setString(3, survey.getDescription());
				preparedStatement.setString(4, survey.getCreatedBy());
				preparedStatement.setString(5, survey.getCreationTime());
				preparedStatement.setString(6, survey.getCreationTimeZone());
				preparedStatement.setString(7, survey.getModificationTime());
				preparedStatement.setString(8, survey.getModificationTimeZone());
				preparedStatement.setString(9, survey.getPublishTime());
				preparedStatement.setString(10, survey.getPublishTimeZone());
				preparedStatement.setInt(11, survey.getPublishedVersion());
				preparedStatement.setInt(12, survey.getState());
				preparedStatement.setString(13, survey.getStartTime());
				preparedStatement.setString(14, survey.getStartTimeZone());
				preparedStatement.setString(15, survey.getEndTime());
				preparedStatement.setString(16, survey.getEndTimeZone());
				preparedStatement.setString(17, survey.getSchedule());

				preparedStatement.execute();

				query = "select last_insert_id() as id";
				preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					String id = resultSet.getString("id");
					survey.setId(Integer.parseInt(id));
				}

			}

			if (survey.getId() > 0) {
				query = "delete from mcs.survey_task where study_id=? and survey_id=? and version=?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setLong(1, survey.getStudyId());
				preparedStatement.setLong(2, survey.getId());
				preparedStatement.setInt(3, workingVersion);
				preparedStatement.executeUpdate();
			}

			if (taskList.size() > 0) {
				query = "insert into mcs.survey_task (study_id, survey_id, version, task_id, task_text, type, possible_input, order_id)"
						+ " values(?,?,?,?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(query);
				for (int i = 0; i < taskList.size(); i++) {
					System.out.println("insert a task " + i + ", task id:" + taskList.get(i).getTaskId() + ", type:"
							+ taskList.get(i).getType());
					preparedStatement.setLong(1, survey.getStudyId());
					preparedStatement.setLong(2, survey.getId());
					preparedStatement.setInt(3, workingVersion);
					preparedStatement.setInt(4, taskList.get(i).getTaskId());
					preparedStatement.setString(5, taskList.get(i).getTaskText());
					preparedStatement.setString(6, taskList.get(i).getType());
					preparedStatement.setString(7, taskList.get(i).getPossibleInput());
					preparedStatement.setInt(8, taskList.get(i).getOrderId());

					preparedStatement.execute();
				}
			}
			
			try {
				query = "update mcs.study set modification_time = ? where id=?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, survey.getModificationTime());
				preparedStatement.setString(2, survey.getStudyId()+"");
				
				preparedStatement.execute();
			} catch (Exception e) {
				// TODO: handle exception
			}

			response.setCode(0);
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

		return response;

	}

	
	@Override
	public Response saveSurveyConfiguration(SurveyConfiguration surveyConfig) {
		int workingVersion = surveyConfig.getSurveySummary().getPublishedVersion() + 1;
		System.out.println("working version :" + workingVersion);
		return updateSurvey(surveyConfig, workingVersion);
	}

	@Override
	public Response publishSurveyConfiguration(SurveyConfiguration surveyConfig) {
		int workingVersion = surveyConfig.getSurveySummary().getPublishedVersion();
		System.out.println("working version :" + workingVersion);
		return updateSurvey(surveyConfig, workingVersion);
	}



	// survey

	@Override
	public ArrayList<SurveySummary> getSurveyList(long studyId) {
		ArrayList<SurveySummary> surveyList = new ArrayList<>();
		Connection connection = null;
		try {
			connection = connect();
			String query = "select * from mcs.survey_summary where study_id=" + studyId
					+ " order by modification_time desc";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
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
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
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
		ArrayList<SurveyTask> taskList = new ArrayList<>();
		Connection connection = null;
		try {
			System.out.println("study id:" + studyId + ", survey id:" + surveyId);
			connection = connect();
			
			String query = "select max(version) as max_version from mcs.survey_task where study_id=" + studyId + " and survey_id=" + surveyId;
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				System.out.println("current max version "+ resultSet.getString("max_version"));
				int version  = resultSet.getInt("max_version");
				query = "select * from mcs.survey_task where study_id=" + studyId + " and survey_id=" + surveyId +" and version="+ version +" order by task_id";
				preparedStatement = connection.prepareStatement(query);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {

					try {
						SurveyTask task = new SurveyTask();
						task.setStudyId(studyId);
						task.setSurveyId(surveyId);

						int taskId = resultSet.getInt("task_id");
						task.setTaskId(taskId);

						String taskText = resultSet.getString("task_text");
						task.setTaskText(taskText);

						String type = resultSet.getString("type");
						task.setType(type);

						String possibleInput = resultSet.getString("possible_input");
						task.setPossibleInput(possibleInput);

						int orderId = resultSet.getInt("order_id");
						task.setOrderId(orderId);

						taskList.add(task);

					} catch (Exception e) {
						// TODO: handle exception
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			System.out.println("Closing the connection.");
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}

		}
		return taskList;
	}

//	@Override
//	public ArrayList<TaskGroupSummary> getTaskGroupList(long studyId) {
//		String url = serverRoot + "/taskgroup/list/" + studyId;
//		String response = genericGetMethod(url);
//		ArrayList<TaskGroupSummary> taskGroupList = new Gson().fromJson(response,
//				new TypeToken<ArrayList<TaskGroupSummary>>() {
//				}.getType());
//		return taskGroupList;
//	}

}
