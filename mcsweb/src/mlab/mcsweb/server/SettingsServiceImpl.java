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

import mlab.mcsweb.client.services.SettingsService;
import mlab.mcsweb.shared.AutoNotificationInfo;
import mlab.mcsweb.shared.CloudStorageInfo;
import mlab.mcsweb.shared.DataUploadInfo;
import mlab.mcsweb.shared.MobileStorageInfo;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Util;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SettingsServiceImpl extends RemoteServiceServlet implements SettingsService {

	static String serverRoot = "";
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static String dbUrl, dbUsername, dbPassword;

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
			dbUrl = properties.getProperty("db_host") + "/" + properties.getProperty("db_schema")
					+ "?serverTimezone=UTC";
			dbUsername = properties.getProperty("db_username");
			dbPassword = properties.getProperty("db_password");
			System.out.println("db prop, dburl:" + dbUrl + ", user:" + dbUsername + ", pass:" + dbPassword);

		} catch (Exception e) {
		}

	}

	private Connection connect() {
		try {
			return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	// Collaborator Functions/////

	@Override
	public Response addCollaborator(long studyId, String email) {
		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (email == null || email.isEmpty()) {
			return response;
		}
		try {
			connection = connect();
			// String query = "insert into mcs.collaborators (study_id,
			// participant_email, state,"
			// + " first_name, last_name) values (?,?,?,?,?) on duplicate key
			// update state=?";
			String query = "insert into mcs.collaborators (study_id, collaborator_email) values (?,?) on duplicate key update collaborator_email=?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, studyId);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, email);
			preparedStatement.execute();
			response.setCode(0);

		} catch (Exception ex) {
			System.out.println("Exception in individual add " + ex.getMessage());
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

	@Override
	public Response editCollaborator(long studyId, String currentEmail, String newEmail) {

		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if (newEmail == null || newEmail.isEmpty()) {
			return response;
		}
		try {
			connection = connect();
			String query = "update mcs.collaborators set collaborator_email=?"
					+ " where study_id=? and collaborator_email=?";
			System.out.println("query " + query);
			// long id = participant.getStudyId();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, newEmail);
			preparedStatement.setLong(2, studyId);
			preparedStatement.setString(3, currentEmail);
			preparedStatement.execute();
			response.setCode(0);

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

	@Override
	public ArrayList<String> getAllCollaborators(long studyId) {
		ArrayList<String> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("Program comes here... get list");
		try {
			connection = connect();

			String query = "select * from mcs.collaborators where study_id = " + studyId
					+ " order by collaborator_email";
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {

				try {
					String listEmail = resultSet.getString("collaborator_email");

					list.add(listEmail);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
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

	@Override
	public Response deleteCollaborators(long studyId, String list) {

		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("list:" + list);
		try {
			String[] collaboratorList = list.trim().split("\\|");
			connection = connect();
			String query = "delete from mcs.collaborators where study_id=? and collaborator_email=?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			for (String email : collaboratorList) {
				try {
					preparedStatement.setLong(1, studyId);
					;
					preparedStatement.setString(2, email);
					System.out.println("id:" + studyId + ", email:" + email);
					int deletedRows = preparedStatement.executeUpdate();
					System.out.println("response from delete : " + deletedRows);
					if (deletedRows == 0) {
						response.setCode(-1);
					}

				} catch (SQLException e) {
					System.out.println("sqlException in individual delete " + e.getMessage());
				} catch (Exception e) {
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

	@Override
	public CloudStorageInfo getCloudSettingInfo(long studyId) {
		CloudStorageInfo info = null; // new CloudStorageInfo to be populated in
										// function from db
		// create connection
		// run query to select data from db
		// update info object if there is any result in resultset

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("Program in getCloudSettingInfo");
		try {
			connection = connect();

			String query = "select * from mcs.cloud_storage_setting where study_id = " + studyId;
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				try { // setting parts of info

					String modT = resultSet.getString("modification_time");
					String modTZ = resultSet.getString("modification_time_zone");
					String type = resultSet.getString("type");
					String params = resultSet.getString("parameter");

					// create new object with fields set (modifiedBy = blank)
					info = new CloudStorageInfo(studyId, "", modT, modTZ, type, params);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		finally {
			System.out.println("Closing the connection.");
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}

		}

		return info;
	}

	@Override
	public Response updateCloudSetting(CloudStorageInfo info) {
		// create connect
		// insert or update
		// set response code properly

		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if (info == null) {
			return response;
		}

		try {
			connection = connect();
			String query = "replace mcs.cloud_storage_setting set study_id=?, modification_time=?, modification_time_zone=?, type=?, parameter=?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, info.getStudyId());
			preparedStatement.setString(2, info.getModificationTime());
			preparedStatement.setString(3, info.getModificationTimeZone());
			preparedStatement.setString(4, info.getType());
			preparedStatement.setString(5, info.getParameter());
			preparedStatement.execute();
			response.setCode(0);

		} catch (Exception ex) {
			System.out.println("Exception in Cloud Setting Update " + ex.getMessage());
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

	@Override

	public MobileStorageInfo getMobileStorageSetting(long studyId) {

		MobileStorageInfo info = null; // new CloudStorageInfo to be populated
										// in function from db
		// create connection
		// run query to select data from db
		// update info object if there is any result in resultset

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("Program in getMobileStorageSetting");
		try {
			connection = connect();

			String query = "select * from mcs.mobile_storage_setting where study_id = " + studyId;
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				try { // setting parts of info

					String modT = resultSet.getString("modification_time");
					String modTZ = resultSet.getString("modification_time_zone");
					int fileSize = resultSet.getInt("file_object_size");
					int maxFile = resultSet.getInt("max_files");
					int maxCap = resultSet.getInt("max_capacity");

					// create new object with fields set (modifiedBy = blank)
					info = new MobileStorageInfo(studyId, "", modT, modTZ, fileSize, maxFile, maxCap);

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

		return info;

	}

	@Override

	public Response updateMobileStorageSetting(MobileStorageInfo info) {
		// create connect
		// insert or update
		// set response code properly

		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if (info == null) {
			return response;
		}

		try {
			connection = connect();
			String query = "replace into mcs.mobile_storage_setting (study_id, modification_time, modification_time_zone, file_object_size, max_files, max_capacity)"
					+ "values (?,?,?,?,?,?)";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, info.getStudyId());
			preparedStatement.setString(2, info.getModificationTime());
			preparedStatement.setString(3, info.getModificationTimeZone());
			preparedStatement.setInt(4, info.getFileObjectSize());
			preparedStatement.setInt(5, info.getMaxFiles());
			preparedStatement.setInt(6, info.getMaxCapacity());
			System.out.println("query " + preparedStatement);
			preparedStatement.execute();
			response.setCode(0);
		} catch (Exception ex) {
			System.out.println("Exception in Mobile Setting Update " + ex.getMessage());
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

	// Data Upload Settings
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public DataUploadInfo getDataUploadSetting(long studyId) {

		DataUploadInfo info = null; // new DataUploadInfo to be populated in
									// function from db
		// create connection
		// run query to select data from db
		// update info object if there is any result in resultset

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("Program in getDataUploadSetting");
		try {
			connection = connect();

			String query = "select * from mcs.data_upload_setting where study_id = " + studyId;
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				try { // setting parts of info

					String modT = resultSet.getString("modification_time");
					String modTZ = resultSet.getString("modification_time_zone");
					String network = resultSet.getString("network");
					String battery = resultSet.getString("battery");
					int freq = resultSet.getInt("upload_frequency");

					// create new object with fields set (modifiedBy = blank)
					info = new DataUploadInfo(studyId, "", modT, modTZ, network, battery, freq);

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

		return info;

	}

	@Override
	public Response updateDataUploadSetting(DataUploadInfo info) {
		// create connect
		// insert or update
		// set response code properly

		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if (info == null) {
			return response;
		}

		try {
			connection = connect();
			String query = "replace into mcs.data_upload_setting (study_id, modification_time, modification_time_zone, network, battery, upload_frequency)"
					+ "values (?,?,?,?,?,?)";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, info.getStudyId());
			preparedStatement.setString(2, info.getModificationTime());
			preparedStatement.setString(3, info.getModificationTimeZone());
			preparedStatement.setString(4, info.getNetwork());
			preparedStatement.setString(5, info.getBattery());
			preparedStatement.setInt(6, info.getFrequency());

			System.out.println("query " + preparedStatement);

			preparedStatement.execute();
			response.setCode(0);
		} catch (Exception ex) {
			System.out.println("Exception in Data Info Setting Update " + ex.getMessage());
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

	// Auto Notification Settings
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public AutoNotificationInfo getAutoNotificationSetting(long studyId) {

		AutoNotificationInfo info = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("Program in getAutoNotInfoSetting");
		try {
			connection = connect();

			String query = "select * from mcs.auto_notification_setting where study_id = " + studyId;
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				try { // setting parts of info

					double baseInterval = resultSet.getDouble("base_interval");
					double increaseFactor = resultSet.getDouble("increase_factor");
					int numberOfPushAtAStage = resultSet.getInt("number_of_push_at_a_stage");
					int attemptsBeforeNotify = resultSet.getInt("attempts_before_notify");
					int daysBeforeNotify = resultSet.getInt("days_before_notify");
					String notifyBooleanOperator = resultSet.getString("notify_boolean_operator");
					int attemptsBeforeTerminate = resultSet.getInt("attempts_before_terminate");
					int daysBeforeTerminate = resultSet.getInt("days_before_terminate");
					String terminateBooleanOperator = resultSet.getString("terminate_boolean_operator");
					String notificationTitle = resultSet.getString("notification_title");
					String notificationMessage = resultSet.getString("notification_message");
					int active = resultSet.getInt("active");

					info = new AutoNotificationInfo();

					info.setBaseInterval(baseInterval);
					info.setIncreaseFactor(increaseFactor);
					info.setNumberOfPushAtAStage(numberOfPushAtAStage);
					info.setAttemptsBeforeNotify(attemptsBeforeNotify);
					info.setDaysBeforeNotify(daysBeforeNotify);
					info.setNotifyBooleanOperator(notifyBooleanOperator);
					info.setAttemptsBeforeTerminate(attemptsBeforeTerminate);
					info.setDaysBeforeTerminate(daysBeforeTerminate);
					info.setTerminateBooleanOperator(terminateBooleanOperator);
					info.setNotificationTitle(notificationTitle);
					info.setNotificationMessage(notificationMessage);
					info.setActive(active);
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

		return info;

	}

	@Override
	public Response updateAutoNotificationSetting(AutoNotificationInfo info) {
		// create connect
		// insert or update
		// set response code properly

		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if (info == null) {
			System.out.println("Info is null");
			return response;
		}else{
			System.out.println("info is not null, going to update data...");
		}
		try {
			connection = connect();

			String query = "select * from mcs.auto_notification_setting where study_id=" + info.getStudyId();
			preparedStatement = connection.prepareStatement(query);
			System.out.println("auto notif query:" + query);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				// update query
				query = "update mcs.auto_notification_setting set modification_time=?, modification_time_zone=?, base_interval="
						+ info.getBaseInterval() + ", increase_factor=" + info.getIncreaseFactor()
						+ ", number_of_push_at_a_stage=" + info.getNumberOfPushAtAStage() + ", attempts_before_notify="
						+ info.getAttemptsBeforeNotify() + ", days_before_notify=" + info.getDaysBeforeNotify()
						+ ", notify_boolean_operator=?, attempts_before_terminate=" + info.getAttemptsBeforeTerminate()
						+ ", days_before_terminate=" + info.getDaysBeforeTerminate()
						+ ", terminate_boolean_operator=?, notification_title=?, notification_message=? where study_id="
						+ info.getStudyId();
				System.out.println("auto notif query:" + query);
			} else {
				// insert query
				query = "insert into mcs.auto_notification_setting (study_id, modification_time, modification_time_zone, base_interval, increase_factor,"
						+ " number_of_push_at_a_stage, attempts_before_notify, days_before_notify, notify_boolean_operator, attempts_before_terminate, days_before_terminate,"
						+ " terminate_boolean_operator, notification_title, notification_message) values("
						+ info.getStudyId() + ",?,?," + info.getBaseInterval() + "," + info.getIncreaseFactor() + " ,"
						+ info.getNumberOfPushAtAStage() + "," + info.getAttemptsBeforeNotify() + ","
						+ info.getDaysBeforeNotify() + ",?," + info.getAttemptsBeforeTerminate() + " ,"
						+ info.getDaysBeforeTerminate() + ",?,?,?)";
				System.out.println("auto notif query:" + query);
			}
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, info.getModificationTime());
			preparedStatement.setString(2, info.getModificationTimeZone());
			preparedStatement.setString(3, info.getNotifyBooleanOperator());
			preparedStatement.setString(4, info.getTerminateBooleanOperator());
			preparedStatement.setString(5, info.getNotificationTitle());
			preparedStatement.setString(6, info.getNotificationMessage());
			System.out.println("query " + preparedStatement);

			preparedStatement.execute();
			response.setCode(0);

		} catch (Exception ex) {
			System.out.println("Exception in Auto Notification Setting Update " + ex.getMessage());
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

	@Override
	public Response updateAutoNotificationState(long studyId, int active) {
		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = connect();
			
			String query = "select * from mcs.auto_notification_setting where study_id=" + studyId;
			preparedStatement = connection.prepareStatement(query);
			System.out.println("auto notif query:" + query);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				query = "update mcs.auto_notification_setting set active=" + active + " where study_id=" + studyId;
				System.out.println("query " + query);
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.execute();
				response.setCode(0);
			}

		} catch (Exception ex) {
			System.out.println("Exception in individual add " + ex.getMessage());
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

	@Override
	public ArrayList<String> getAllNotificationAdmin(long studyId) {
		ArrayList<String> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("Program comes here... get list");
		try {
			connection = connect();

			String query = "select * from mcs.auto_notification_admin where study_id = " + studyId
					+ " order by admin_email";
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {

				try {
					String listEmail = resultSet.getString("admin_email");

					list.add(listEmail);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
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

	@Override
	public Response addNotificationAdmin(long studyId, String adminEmail) {
		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (Util.isEmptyString(adminEmail)) {
			return response;
		}
		try {
			connection = connect();
			// String query = "insert into mcs.collaborators (study_id,
			// participant_email, state,"
			// + " first_name, last_name) values (?,?,?,?,?) on duplicate key
			// update state=?";
			String query = "insert into mcs.auto_notification_admin (study_id, admin_email) values (?,?) on duplicate key update admin_email=?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, studyId);
			preparedStatement.setString(2, adminEmail);
			preparedStatement.setString(3, adminEmail);
			preparedStatement.execute();
			response.setCode(0);

		} catch (Exception ex) {
			System.out.println("Exception in individual add " + ex.getMessage());
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

	@Override
	public Response editNotificationAdmin(long studyId, String currentEmail, String newEmail) {

		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if (newEmail == null || newEmail.isEmpty()) {
			return response;
		}
		try {
			connection = connect();
			String query = "update mcs.auto_notification_admin set admin_email=?"
					+ " where study_id=? and admin_email=?";
			System.out.println("query " + query);
			// long id = participant.getStudyId();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, newEmail);
			preparedStatement.setLong(2, studyId);
			preparedStatement.setString(3, currentEmail);
			preparedStatement.execute();
			response.setCode(0);

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

	@Override
	public Response deleteNotificationAdmins(long studyId, String adminEmails) {
		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("list:" + adminEmails);
		try {
			String[] adminList = adminEmails.trim().split("\\|");
			connection = connect();
			String query = "delete from mcs.auto_notification_admin where study_id=? and admin_email=?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			for (String email : adminList) {
				try {
					preparedStatement.setLong(1, studyId);
					;
					preparedStatement.setString(2, email);
					System.out.println("id:" + studyId + ", email:" + email);
					int deletedRows = preparedStatement.executeUpdate();
					System.out.println("response from delete : " + deletedRows);
					if (deletedRows == 0) {
						response.setCode(-1);
					}

				} catch (SQLException e) {
					System.out.println("sqlException in individual delete " + e.getMessage());
				} catch (Exception e) {
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
