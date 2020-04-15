package mlab.mcsweb.server;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import mlab.mcsweb.client.services.ParticipantService;
import mlab.mcsweb.shared.FileObjectInfo;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.PingInfo;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Util;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ParticipantServiceImpl extends RemoteServiceServlet implements ParticipantService {

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
			dbUrl = properties.getProperty("db_host") + "/" + properties.getProperty("db_schema")
					+ "?serverTimezone=UTC";
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

	// participation
	@Override
	public Response addParticipant(Participant participant) {
		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (participant.getUserEmail() == null || participant.getUserEmail().isEmpty()) {
			return response;
		}
		try {
			connection = connect();
			String query = "insert into mcs.enrollment (study_id, participant_email, device_uuid, state,"
					+ " first_name, last_name) values (?,?,?,?,?,?) on duplicate key update state=?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, participant.getStudyId());
			preparedStatement.setString(2, participant.getUserEmail());
			preparedStatement.setString(3, participant.getIdentifier());
			preparedStatement.setInt(4, 1);
			preparedStatement.setString(5, participant.getFirstName());
			preparedStatement.setString(6, participant.getLastName());
			preparedStatement.setInt(7, 1);
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
	public Response editParticipant(String currentEmail, String currentIdentifier, Participant participant) {

		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if (participant.getUserEmail() == null || participant.getUserEmail().isEmpty()) {
			return response;
		}
		try {
			connection = connect();
			String query = "update mcs.enrollment set participant_email=?, device_uuid=?, first_name=?, last_name=?, state=1"
					+ " where study_id=? and participant_email=? and device_uuid=?";
			System.out.println("query " + query);
			long id = participant.getStudyId();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, participant.getUserEmail());
			preparedStatement.setString(2, participant.getIdentifier());
			preparedStatement.setString(3, participant.getFirstName());
			preparedStatement.setString(4, participant.getLastName());
			preparedStatement.setLong(5, id);
			preparedStatement.setString(6, currentEmail);
			preparedStatement.setString(7, currentIdentifier);
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
	public ArrayList<Participant> getAllParticipants(long studyId) {
		ArrayList<Participant> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = connect();

			String query = "select * from mcs.enrollment where study_id = " + studyId + " order by participant_email";
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {

				try {
					Participant participant = new Participant();
					participant.setStudyId(studyId);

					String email = resultSet.getString("participant_email");
					participant.setUserEmail(email);

					String identifier = resultSet.getString("device_uuid");
					participant.setIdentifier(identifier);
					;

					String firstName = resultSet.getString("first_name");
					participant.setFirstName(firstName);

					String lastName = resultSet.getString("last_name");
					participant.setLastName(lastName);

					int state = resultSet.getInt("state");
					if (state == 1) {
						participant.setStatus("enrolled");
					} else {
						participant.setStatus("pending");
					}
					list.add(participant);

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
	public Response deleteParticipants(List<Participant> participants) {

		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		// System.out.println("list:"+ list);
		try {
			// String[] participantList = list;//list.trim().split("\\|");
			connection = connect();
			String query = "delete from mcs.enrollment where study_id=? and participant_email=? and device_uuid=?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			for (int i = 0; i < participants.size(); i++) {
				try {
					preparedStatement.setLong(1, participants.get(i).getStudyId());
					;
					preparedStatement.setString(2, participants.get(i).getUserEmail());
					preparedStatement.setString(3, participants.get(i).getIdentifier());
					// System.out.println("id:" + studyId + ", email:" + email);
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

	private boolean isEnrolled(long studyId, String email, String uuid) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if (Util.isEmptyString(email) && Util.isEmptyString(uuid)) {
			return false;
		}
		try {
			connection = connect();
			String query = "select * from mcs.enrollment where study_id=? ";
			if (!Util.isEmptyString(email)) {
				query += " and participant_email = ?";
			}
			if (!Util.isEmptyString(uuid)) {
				query += " and device_uuid = ?";
			}

			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			int counter = 1;
			preparedStatement.setLong(counter++, studyId);
			if (!Util.isEmptyString(email)) {
				preparedStatement.setString(counter++, email);
			}

			if (!Util.isEmptyString(uuid)) {
				preparedStatement.setString(counter++, uuid);
			}
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}

		} catch (Exception ex) {
			System.out.println("Exception in individual delete " + ex.getMessage());
		} finally {
			System.out.println("Closing the connection.");
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}
		}

		return false;

	}

	@Override
	public ArrayList<PingInfo> getPingHistory(long studyId, String email, String uuid, int days) {
		ArrayList<PingInfo> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			System.out.println("study Id: " + studyId + ", email:" + email + ", uuid:" + uuid + ", days:" + days);
			if (Util.isEmptyString(email) && Util.isEmptyString(uuid)) {
				return list;
			}

			if (isEnrolled(studyId, email, uuid)) {
				connection = connect();

				String query = "select *, from_unixtime(ping_time) as datetime from mcs.ping_history where ";
				if (!Util.isEmptyString(email)) {
					query += " user_email = ? and ";
				}
				if (!Util.isEmptyString(uuid)) {
					query += " device_uuid = ? and ";
				}

				query += " from_unixtime(ping_time )  between (now() - interval ? day) and now() order by ping_time desc";

				System.out.println("query: " + query);
				preparedStatement = connection.prepareStatement(query);
				int counter = 1;
				if (!Util.isEmptyString(email)) {
					preparedStatement.setString(counter++, email);
				}
				if (!Util.isEmptyString(uuid)) {
					preparedStatement.setString(counter++, uuid);
				}

				preparedStatement.setInt(counter++, days);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {

					try {
						PingInfo info = new PingInfo();

						String userEmail = resultSet.getString("user_email");
						info.setEmail(userEmail);

						String deviceUuid = resultSet.getString("device_uuid");
						info.setUuid(deviceUuid);

						String time = resultSet.getString("datetime");
						info.setTime(time);

						String network = resultSet.getString("network");
						info.setNetwork(network);

						String osType = resultSet.getString("os_type");
						info.setOsType(osType);

						String osVersion = resultSet.getString("os_version");
						info.setOsVersion(osVersion);

						String appVersion = resultSet.getString("app_version");
						info.setAppVersion(appVersion);

						String data = resultSet.getString("data");
						info.setData(data);

						list.add(info);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
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
		return list;
	}

	@Override
	public ArrayList<FileObjectInfo> getObjectHistory(long studyId, String email, String uuid) {
		ArrayList<FileObjectInfo> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			System.out.println("study Id: " + studyId + ", email:" + email + ", uuid:" + uuid);
			if (Util.isEmptyString(email) && Util.isEmptyString(uuid)) {
				return list;
			}

			connection = connect();

			String query = "select * from mcs.object_history where study_id= "+ studyId;
			if (!Util.isEmptyString(email)) {
				query += " and user_email = ? ";
			}
			if (!Util.isEmptyString(uuid)) {
				query += " and device_uuid = ? ";
			}

			query += " order by ready_to_upload_time desc";

			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			int counter = 1;
			if (!Util.isEmptyString(email)) {
				preparedStatement.setString(counter++, email);
			}
			if (!Util.isEmptyString(uuid)) {
				preparedStatement.setString(counter++, uuid);
			}

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {

				try {
					FileObjectInfo info = new FileObjectInfo();

					info.setStudyId(studyId);
					
					String userEmail = resultSet.getString("user_email");
					info.setEmail(userEmail);

					String deviceUuid = resultSet.getString("device_uuid");
					info.setUuid(deviceUuid);

					String name = resultSet.getString("object_name");
					info.setName(name);

					String readyToUploadTime = resultSet.getString("ready_to_upload_time");
					info.setReadyToUploadTime(readyToUploadTime);

					String uploadTime = resultSet.getString("upload_time");
					info.setUploadTime(uploadTime);

					String deleteTime = resultSet.getString("delete_time");
					info.setDeleteTime(deleteTime);


					list.add(info);

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

}
