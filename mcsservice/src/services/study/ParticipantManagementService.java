package services.study;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import org.apache.commons.codec.binary.Base64;

import commons.DatabaseUtil;
import commons.Response;

@Path("study/{studyId}/participant")
public class ParticipantManagementService {

	private Connection connection;
	private PreparedStatement preparedStatement;

	/*
	 * private Response updateSurvey(SurveyConfiguration surveyConfig, int
	 * workingVersion) { Response response = new Response(); try { SurveySummary
	 * survey = surveyConfig.getSurveySummary(); ArrayList<SurveyTask> taskList
	 * = surveyConfig.getTaskList();
	 * 
	 * connection = DatabaseUtil.connectToDatabase();
	 * 
	 * String query = ""; if (survey.getId() > 0) { query =
	 * "update mcs.survey_summary set name=?, description=?, modification_time=?, modification_time_zone=?, "
	 * +
	 * " publish_time=?, publish_time_zone=?, published_version=?, state=?, start_time=?, start_time_zone=?, end_time=?, end_time_zone=?, "
	 * + " schedule=? where id=? and study_id=?"; preparedStatement =
	 * connection.prepareStatement(query); preparedStatement.setString(1,
	 * survey.getName()); preparedStatement.setString(2,
	 * survey.getDescription()); preparedStatement.setString(3,
	 * survey.getModificationTime()); preparedStatement.setString(4,
	 * survey.getModificationTimeZone()); preparedStatement.setString(5,
	 * survey.getPublishTime()); preparedStatement.setString(6,
	 * survey.getPublishTimeZone()); preparedStatement.setInt(7,
	 * survey.getPublishedVersion()); preparedStatement.setInt(8,
	 * survey.getState()); preparedStatement.setString(9,
	 * survey.getStartTime()); preparedStatement.setString(10,
	 * survey.getStartTimeZone()); preparedStatement.setString(11,
	 * survey.getEndTime()); preparedStatement.setString(12,
	 * survey.getEndTimeZone()); preparedStatement.setString(13,
	 * survey.getSchedule());
	 * 
	 * preparedStatement.setLong(14, survey.getId());
	 * preparedStatement.setLong(15, survey.getStudyId());
	 * 
	 * preparedStatement.execute();
	 * 
	 * } else { query =
	 * "insert into mcs.survey_summary (study_id, name, description, created_by, creation_time, creation_time_zone,"
	 * +
	 * " modification_time, modification_time_zone, publish_time, publish_time_zone, published_version, state, start_time, start_time_zone,"
	 * +
	 * " end_time, end_time_zone, schedule) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	 * ; preparedStatement = connection.prepareStatement(query);
	 * preparedStatement.setLong(1, survey.getStudyId());
	 * preparedStatement.setString(2, survey.getName());
	 * preparedStatement.setString(3, survey.getDescription());
	 * preparedStatement.setString(4, survey.getCreatedBy());
	 * preparedStatement.setString(5, survey.getCreationTime());
	 * preparedStatement.setString(6, survey.getCreationTimeZone());
	 * preparedStatement.setString(7, survey.getModificationTime());
	 * preparedStatement.setString(8, survey.getModificationTimeZone());
	 * preparedStatement.setString(9, survey.getPublishTime());
	 * preparedStatement.setString(10, survey.getPublishTimeZone());
	 * preparedStatement.setInt(11, survey.getPublishedVersion());
	 * preparedStatement.setInt(12, survey.getState());
	 * preparedStatement.setString(13, survey.getStartTime());
	 * preparedStatement.setString(14, survey.getStartTimeZone());
	 * preparedStatement.setString(15, survey.getEndTime());
	 * preparedStatement.setString(16, survey.getEndTimeZone());
	 * preparedStatement.setString(17, survey.getSchedule());
	 * 
	 * preparedStatement.execute();
	 * 
	 * query = "select last_insert_id() as id"; preparedStatement =
	 * connection.prepareStatement(query); ResultSet resultSet =
	 * preparedStatement.executeQuery(); if (resultSet.next()) { String id =
	 * resultSet.getString("id"); survey.setId(Integer.parseInt(id)); }
	 * 
	 * }
	 * 
	 * if (survey.getId() > 0) { query =
	 * "delete from mcs.survey_task where study_id=? and survey_id=? and version=?"
	 * ; preparedStatement = connection.prepareStatement(query);
	 * preparedStatement.setLong(1, survey.getStudyId());
	 * preparedStatement.setLong(2, survey.getId()); preparedStatement.setInt(3,
	 * workingVersion); preparedStatement.executeUpdate(); }
	 * 
	 * if (taskList.size() > 0) { query =
	 * "insert into mcs.survey_task (study_id, survey_id, version, task_id, task_text, type, possible_input, order_id)"
	 * + " values(?,?,?,?,?,?,?,?)"; preparedStatement =
	 * connection.prepareStatement(query); for (int i = 0; i < taskList.size();
	 * i++) { System.out.println("insert a task " + i + ", task id:" +
	 * taskList.get(i).getTaskId() + ", type:" + taskList.get(i).getType());
	 * preparedStatement.setLong(1, survey.getStudyId());
	 * preparedStatement.setLong(2, survey.getId()); preparedStatement.setInt(3,
	 * workingVersion); preparedStatement.setInt(4,
	 * taskList.get(i).getTaskId()); preparedStatement.setString(5,
	 * taskList.get(i).getTaskText()); preparedStatement.setString(6,
	 * taskList.get(i).getType()); preparedStatement.setString(7,
	 * taskList.get(i).getPossibleInput()); preparedStatement.setInt(8,
	 * taskList.get(i).getOrderId());
	 * 
	 * preparedStatement.execute(); } }
	 * 
	 * try { query = "update mcs.study set modification_time = ? where id=?";
	 * preparedStatement = connection.prepareStatement(query);
	 * preparedStatement.setString(1, survey.getModificationTime());
	 * preparedStatement.setString(2, survey.getStudyId()+"");
	 * 
	 * preparedStatement.execute(); } catch (Exception e) { // TODO: handle
	 * exception }
	 * 
	 * response.setCode(0); } catch (Exception e) { // TODO: handle exception
	 * e.printStackTrace(); } finally {
	 * System.out.println("Closing the connection."); if (connection != null)
	 * try { connection.close(); } catch (SQLException ignore) { } }
	 * 
	 * return response;
	 * 
	 * }
	 * 
	 * @Path("save/config")
	 * 
	 * @POST
	 * 
	 * @Consumes(MediaType.APPLICATION_XML)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response
	 * saveSurvey(JAXBElement<SurveyConfiguration> jaxbElement) {
	 * SurveyConfiguration surveyConfig = jaxbElement.getValue(); int
	 * workingVersion = surveyConfig.getSurveySummary().getPublishedVersion() +
	 * 1; System.out.println("working version :" + workingVersion); return
	 * updateSurvey(surveyConfig, workingVersion); }
	 * 
	 * @Path("publish/config")
	 * 
	 * @POST
	 * 
	 * @Consumes(MediaType.APPLICATION_XML)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response
	 * publishSurvey(JAXBElement<SurveyConfiguration> jaxbElement) {
	 * SurveyConfiguration surveyConfig = jaxbElement.getValue(); int
	 * workingVersion = surveyConfig.getSurveySummary().getPublishedVersion();
	 * System.out.println("working version :" + workingVersion); return
	 * updateSurvey(surveyConfig, workingVersion); }
	 * 
	 * @Path("tasklist/{studyId}/{surveyId}")
	 * 
	 * @GET
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public ArrayList<SurveyTask>
	 * getTaskList(@PathParam("studyId") String studyId,
	 * 
	 * @PathParam("surveyId") String surveyId) { ArrayList<SurveyTask> taskList
	 * = new ArrayList<>(); try { Integer.parseInt(studyId);
	 * Integer.parseInt(surveyId); System.out.println("study id:" + studyId +
	 * ", survey id:" + surveyId); connection =
	 * DatabaseUtil.connectToDatabase();
	 * 
	 * String query =
	 * "select max(version) as max_version from mcs.survey_task where study_id="
	 * + studyId + " and survey_id=" + surveyId; preparedStatement =
	 * connection.prepareStatement(query); ResultSet resultSet =
	 * preparedStatement.executeQuery(); if(resultSet.next()){
	 * System.out.println("current max version "+
	 * resultSet.getString("max_version")); int version =
	 * resultSet.getInt("max_version"); query =
	 * "select * from mcs.survey_task where study_id=" + studyId +
	 * " and survey_id=" + surveyId +" and version="+ version
	 * +" order by task_id"; preparedStatement =
	 * connection.prepareStatement(query); resultSet =
	 * preparedStatement.executeQuery(); while (resultSet.next()) {
	 * 
	 * try { SurveyTask task = new SurveyTask();
	 * task.setStudyId(Integer.parseInt(studyId));
	 * task.setSurveyId(Integer.parseInt(surveyId));
	 * 
	 * int taskId = resultSet.getInt("task_id"); task.setTaskId(taskId);
	 * 
	 * String taskText = resultSet.getString("task_text");
	 * task.setTaskText(taskText);
	 * 
	 * String type = resultSet.getString("type"); task.setType(type);
	 * 
	 * String possibleInput = resultSet.getString("possible_input");
	 * task.setPossibleInput(possibleInput);
	 * 
	 * int orderId = resultSet.getInt("order_id"); task.setOrderId(orderId);
	 * 
	 * taskList.add(task);
	 * 
	 * } catch (Exception e) { // TODO: handle exception } }
	 * 
	 * } } catch (Exception e) { // TODO: handle exception } finally {
	 * System.out.println("Closing the connection."); if (connection != null)
	 * try { connection.close(); } catch (SQLException ignore) { }
	 * 
	 * } return taskList; }
	 */

	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestMessage(@PathParam("studyId") String studyId) {
		System.out.println("receive the path test " + studyId);
		return new Response(0, "Test Message");
	}

	@Path("all/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Participant> getAllParticipants(@PathParam("studyId") String studyId) {
		ArrayList<Participant> list = new ArrayList<>();
		try {
			long id = Long.parseLong(studyId);
			connection = DatabaseUtil.connectToDatabase();

			String query = "select * from mcs.participant where study_id = " + id + " order by user_email";
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {

				try {
					Participant participant = new Participant();
					participant.setStudyId(id);

					String email = resultSet.getString("user_email");
					participant.setUserEmail(email);

					String firstName = resultSet.getString("first_name");
					participant.setFirstName(firstName);

					String lastName = resultSet.getString("last_name");
					participant.setLastName(lastName);

					String organization = resultSet.getString("organization");
					participant.setOrganization(organization);

					String status = resultSet.getString("status");
					participant.setStatus(status);

					list.add(participant);

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
		return list;
	}

	@Path("delete/{list}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeParticipant(@PathParam("studyId") String studyId, @PathParam("list") String list) {
		Response response = new Response(0, "");
		System.out.println("list:"+ list);
		try {
			long id = Long.parseLong(studyId);
			String listAfterDecode = new String(Base64.decodeBase64(list));
			String[] participantList = listAfterDecode.trim().split("\\|");
			connection = DatabaseUtil.connectToDatabase();
			String query = "delete from mcs.participant where study_id=? and user_email=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			for (String email : participantList) {
				try {
					preparedStatement.setLong(1, id);;
					preparedStatement.setString(2, email);
					int deletedRows = preparedStatement.executeUpdate();
					System.out.println("response from delete : "+ deletedRows);
					if (deletedRows == 0) {
						response.setCode(-1);
					}

				} catch(SQLException e){
					System.out.println("sqlException in individual delete " + e.getMessage());
				}catch (Exception e) {
					System.out.println("Exception in individual delete " + e.getMessage());
					// TODO: handle exception
				}
			}

		} catch (Exception ex) {
			System.out.println("Exception in individual delete " + ex.getMessage());
			response.setCode(-1);
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

}
