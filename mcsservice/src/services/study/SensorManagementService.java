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

import commons.DatabaseUtil;
import commons.Response;

@Path("study/{studyId}/sensorconfig")
public class SensorManagementService {

	private Connection connection;
	private PreparedStatement preparedStatement;

	private Response updateSensorConfig(SensorConfiguration sensorConfig, int workingVersion) {
		Response response = new Response();
		try {
			SensorSummary sensorSummary = sensorConfig.getSensorSummary();
			ArrayList<SensorAction> actionList = sensorConfig.getActionList();

			connection = DatabaseUtil.connectToDatabase();

			String query = "";
			if (sensorSummary.getId() > 0) {
				query = "update mcs.sensor_summary set name=?, description=?, modification_time=?, modification_time_zone=?, "
						+ " publish_time=?, publish_time_zone=?, published_version=?, state=?, start_time=?, start_time_zone=?, end_time=?, end_time_zone=?, "
						+ " schedule=? where id=? and study_id=?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, sensorSummary.getName());
				preparedStatement.setString(2, sensorSummary.getDescription());
				preparedStatement.setString(3, sensorSummary.getModificationTime());
				preparedStatement.setString(4, sensorSummary.getModificationTimeZone());
				preparedStatement.setString(5, sensorSummary.getPublishTime());
				preparedStatement.setString(6, sensorSummary.getPublishTimeZone());
				preparedStatement.setInt(7, sensorSummary.getPublishedVersion());
				preparedStatement.setInt(8, sensorSummary.getState());
				preparedStatement.setString(9, sensorSummary.getStartTime());
				preparedStatement.setString(10, sensorSummary.getStartTimeZone());
				preparedStatement.setString(11, sensorSummary.getEndTime());
				preparedStatement.setString(12, sensorSummary.getEndTimeZone());
				preparedStatement.setString(13, sensorSummary.getSchedule());

				preparedStatement.setLong(14, sensorSummary.getId());
				preparedStatement.setLong(15, sensorSummary.getStudyId());

				preparedStatement.execute();

			} else {
				query = "insert into mcs.sensor_summary (study_id, name, description, created_by, creation_time, creation_time_zone,"
						+ " modification_time, modification_time_zone, publish_time, publish_time_zone, published_version, state, start_time, start_time_zone,"
						+ " end_time, end_time_zone, schedule) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setLong(1, sensorSummary.getStudyId());
				//TODO:temporary hard coding name
				//String name = "Sensor Config " + new Date();
				preparedStatement.setString(2, sensorSummary.getName());
				preparedStatement.setString(3, sensorSummary.getDescription());
				preparedStatement.setString(4, sensorSummary.getCreatedBy());
				preparedStatement.setString(5, sensorSummary.getCreationTime());
				preparedStatement.setString(6, sensorSummary.getCreationTimeZone());
				preparedStatement.setString(7, sensorSummary.getModificationTime());
				preparedStatement.setString(8, sensorSummary.getModificationTimeZone());
				preparedStatement.setString(9, sensorSummary.getPublishTime());
				preparedStatement.setString(10, sensorSummary.getPublishTimeZone());
				preparedStatement.setInt(11, sensorSummary.getPublishedVersion());
				preparedStatement.setInt(12, sensorSummary.getState());
				preparedStatement.setString(13, sensorSummary.getStartTime());
				preparedStatement.setString(14, sensorSummary.getStartTimeZone());
				preparedStatement.setString(15, sensorSummary.getEndTime());
				preparedStatement.setString(16, sensorSummary.getEndTimeZone());
				preparedStatement.setString(17, sensorSummary.getSchedule());

				preparedStatement.execute();

				query = "select last_insert_id() as id";
				preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					String id = resultSet.getString("id");
					sensorSummary.setId(Integer.parseInt(id));
				}

			}

			if (sensorSummary.getId() > 0) {
				query = "delete from mcs.sensor_action where study_id=? and sensor_config_id=? and version=?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setLong(1, sensorSummary.getStudyId());
				preparedStatement.setLong(2, sensorSummary.getId());
				preparedStatement.setInt(3, workingVersion);
				preparedStatement.executeUpdate();
			}

			if (actionList.size() > 0) {
				query = "insert into mcs.sensor_action (study_id, sensor_config_id, version, sensor_action_code, type,  is_enabled, frequency)"
						+ " values(?,?,?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(query);
				for (int i = 0; i < actionList.size(); i++) {
					System.out.println("insert a sensor action " + i + ", action code:" + actionList.get(i).getSensorActionCode() + ", type:"
							+ actionList.get(i).getType());
					preparedStatement.setLong(1, sensorSummary.getStudyId());
					preparedStatement.setLong(2, sensorSummary.getId());
					preparedStatement.setInt(3, workingVersion);
					preparedStatement.setString(4, actionList.get(i).getSensorActionCode());
					preparedStatement.setString(5, actionList.get(i).getType());
					preparedStatement.setInt(6, actionList.get(i).getIsEnabled());
					preparedStatement.setFloat(7, actionList.get(i).getFrequency());

					preparedStatement.execute();
				}
			}
			
			try {
				query = "update mcs.study set modification_time = ? where id=?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, sensorSummary.getModificationTime());
				preparedStatement.setString(2, sensorSummary.getStudyId()+"");
				
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

	@Path("save")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveSensorConfig(JAXBElement<SensorConfiguration> jaxbElement) {
		SensorConfiguration sensorConfig = jaxbElement.getValue();
		int workingVersion = sensorConfig.getSensorSummary().getPublishedVersion() + 1;
		System.out.println("working version :" + workingVersion);
		return updateSensorConfig(sensorConfig, workingVersion);
	}

	@Path("publish")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response publishSensorConfig(JAXBElement<SensorConfiguration> jaxbElement) {
		SensorConfiguration sensorConfig = jaxbElement.getValue();
		int workingVersion = sensorConfig.getSensorSummary().getPublishedVersion();
		System.out.println("working version :" + workingVersion);
		return updateSensorConfig(sensorConfig, workingVersion);
	}

	@Path("{configId}/actionlist")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<SensorAction> getSensorActionList(@PathParam("studyId") String studyId,
			@PathParam("configId") String configId) {
		ArrayList<SensorAction> actionList = new ArrayList<>();
		try {
			Integer.parseInt(studyId);
			Integer.parseInt(configId);
			System.out.println("study id:" + studyId + ", config id:" + configId);
			connection = DatabaseUtil.connectToDatabase();
			
			String query = "select max(version) as max_version from mcs.sensor_action where study_id=" + studyId + " and sensor_config_id=" + configId;
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				System.out.println("current max version "+ resultSet.getString("max_version"));
				int version  = resultSet.getInt("max_version");
				query = "select * from mcs.sensor_action where study_id=" + studyId + " and sensor_config_id=" + configId +" and version="+ version;
				preparedStatement = connection.prepareStatement(query);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {

					try {
						SensorAction action = new SensorAction();
						action.setStudyId(Integer.parseInt(studyId));
						action.setSensorConfigId(Integer.parseInt(configId));
						
						String actionCode = resultSet.getString("sensor_action_code");
						action.setSensorActionCode(actionCode);
						
						String type = resultSet.getString("type");
						action.setType(type);

						int isEnabled = resultSet.getInt("is_enabled");
						action.setIsEnabled(isEnabled);
						
						float frequency = resultSet.getFloat("frequency");
						action.setFrequency(frequency);					

						String timeBound = resultSet.getString("time_bound");
						action.setTimeBound(timeBound);
						
						String batteryBound = resultSet.getString("battery_bound");
						action.setBatteryBound(batteryBound);
						
						String param1 = resultSet.getString("param_1");
						action.setParam1(param1);

						String param2 = resultSet.getString("param_2");
						action.setParam2(param2);

						String param3 = resultSet.getString("param_3");
						action.setParam3(param3);

						actionList.add(action);

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
		return actionList;
	}

	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<SensorSummary> getSensorConfigs(@PathParam("studyId") String studyId) {
		ArrayList<SensorSummary> sensorConfigList = new ArrayList<>();

		try {
			Integer.parseInt(studyId);
			System.out.println("study id:" + studyId);
			connection = DatabaseUtil.connectToDatabase();
			String query = "select * from mcs.sensor_summary where study_id=" + studyId + " order by modification_time desc";
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				try {

					SensorSummary sensorSummary = new SensorSummary();
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

					sensorSummary.setId(id);
					sensorSummary.setStudyId(Integer.parseInt(studyId));
					sensorSummary.setName(name);
					sensorSummary.setDescription(description);
					sensorSummary.setModificationTime(modificationTime);
					sensorSummary.setModificationTimeZone(modificationTimeZone);
					sensorSummary.setPublishedVersion(publishedVersion);
					sensorSummary.setPublishTime(publishTime);
					sensorSummary.setPublishTimeZone(publishTimeZone);
					sensorSummary.setState(state);
					sensorSummary.setResponseCount(responseCount);
					sensorSummary.setStartTime(startTime);
					sensorSummary.setStartTimeZone(startTimeZone);
					sensorSummary.setEndTime(endTime);
					sensorSummary.setEndTimeZone(endTimeZone);
					sensorSummary.setSchedule(schedule);

					sensorConfigList.add(sensorSummary);
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

		return sensorConfigList;
	}

}
