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

import mlab.mcsweb.client.services.DashboardService;
import mlab.mcsweb.shared.DaywiseCount;
import mlab.mcsweb.shared.FileIdentifier;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.Response;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DashboardServiceImpl extends RemoteServiceServlet implements DashboardService {

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

	// ------------------Summary ----------------------------//

	@Override
	public Integer getTotalParticipant(long studyId) {
		Connection conn = null;
		if (studyId < 1) {
			return 0;
		}
		try {
			conn = connect();

			String query = "select count(*) as cn from mcs.enrollment where study_id=" + studyId;

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("cn");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("Closing the connection.");
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return 0;
	}

	@Override
	public Integer getInstalledDevicesCount(long studyId) {
		Connection conn = null;
		if (studyId < 1) {
			return 0;
		}
		try {
			conn = connect();

			String query = "select count(*) as cn from mcs.enrollment where ((length(device_uuid)=0 and participant_email in "
					+ " (select email from mcs.mobile_users where length(device_uuid)=36)) or (length(device_uuid)>0 and participant_email "
					+ "in (select email from mcs.mobile_users where length(device_uuid)<36))) and study_id=" + studyId;

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("cn");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("Closing the connection.");
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return 0;
	}

	@Override
	public ArrayList<DaywiseCount> getDailyPingCount(long studyId, int days) {
		Connection conn = null;
		ArrayList<DaywiseCount> list = new ArrayList<>();
		if (studyId < 1 || days < 1) {
			return list;
		}
		try {
			conn = connect();

			String query = "select count(*) as cn, from_unixtime(ping_time, '%b %d') as dwise from mcs.ping_history where (unix_timestamp(now()) - ping_time) < "
					+ days
					+ " * 24 * 3600  and concat(user_email, '-', device_uuid) in (select distinct concat(participant_email, '-', device_uuid) from mcs.enrollment "
					+ "where study_id=" + studyId + ") group by dwise";

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {

				try {
					int count = resultSet.getInt("cn");
					String date = resultSet.getString("dwise");

					list.add(new DaywiseCount(date, count));

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
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return list;

	}

	@Override
	public ArrayList<DaywiseCount> getDailyPingCountFromUniqueDevices(long studyId, int days) {
		Connection conn = null;
		ArrayList<DaywiseCount> list = new ArrayList<>();
		if (studyId < 1 || days < 1) {
			return list;
		}
		try {
			conn = connect();

			String query = "select count(*) as cn, daywise from (select distinct user_email, device_uuid, from_unixtime(ping_time, '%b %d') as daywise from mcs.ping_history "
					+ "where (unix_timestamp(now()) - ping_time) < " + days
					+ " * 24 * 3600 and concat(user_email, '-', device_uuid) in "
					+ "(select distinct concat(participant_email, '-', device_uuid) from mcs.enrollment where study_id="
					+ studyId + ")) t group by t.daywise";

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {

				try {
					int count = resultSet.getInt("cn");
					String date = resultSet.getString("daywise");

					list.add(new DaywiseCount(date, count));

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			System.out.println("Closing the connection.");
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return list;
	}

	@Override
	public ArrayList<Participant> getParticipantsWithNoPing(long studyId, int days) {
		Connection conn = null;
		ArrayList<Participant> list = new ArrayList<>();
		if (studyId < 1 || days < 1) {
			return list;
		}
		try {
			conn = connect();

			String query = "select * from mcs.enrollment where study_id=" + studyId
					+ " and concat(participant_email, '-', device_uuid) not in ("
					+ "select distinct concat(user_email, '-', device_uuid) from mcs.ping_history where (unix_timestamp(now()) - ping_time) < "
					+ days + " * 24 * 3600)";

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {

				try {
					Participant participant = new Participant();
					participant.setStudyId(studyId);
					String email = resultSet.getString("participant_email");
					participant.setUserEmail(email);
					String identifier = resultSet.getString("device_uuid");
					participant.setIdentifier(identifier);

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
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return list;
	}

	@Override
	public ArrayList<DaywiseCount> getDailyObjectCount(long studyId, int days) {
		Connection conn = null;
		ArrayList<DaywiseCount> list = new ArrayList<>();
		if (studyId < 1 || days < 1) {
			return list;
		}
		try {
			conn = connect();

			String query = "select count(*) as cn, from_unixtime(upload_time, '%b %d') as dwise from mcs.object_history where (unix_timestamp(now()) - upload_time) < "
					+ days
					+ " * 24 * 3600  and concat(user_email, '-', device_uuid) in (select distinct concat(participant_email, '-', device_uuid) from mcs.enrollment "
					+ "where study_id=" + studyId + ") group by dwise";

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {

				try {
					int count = resultSet.getInt("cn");
					String date = resultSet.getString("dwise");

					list.add(new DaywiseCount(date, count));

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
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return list;

	}

	@Override
	public ArrayList<DaywiseCount> getDailyObjectCountFromUniqueDevices(long studyId, int days) {
		Connection conn = null;
		ArrayList<DaywiseCount> list = new ArrayList<>();
		if (studyId < 1 || days < 1) {
			return list;
		}
		try {
			conn = connect();

			String query = "select count(*) as cn, daywise from (select distinct user_email, device_uuid, from_unixtime(upload_time, '%b %d') as daywise from mcs.object_history "
					+ "where (unix_timestamp(now()) - upload_time) < " + days
					+ " * 24 * 3600 and concat(user_email, '-', device_uuid) in "
					+ "(select distinct concat(participant_email, '-', device_uuid) from mcs.enrollment where study_id="
					+ studyId + ")) t group by t.daywise";

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {

				try {
					int count = resultSet.getInt("cn");
					String date = resultSet.getString("daywise");

					list.add(new DaywiseCount(date, count));

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			System.out.println("Closing the connection.");
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return list;
	}

	@Override
	public ArrayList<Participant> getParticipantsWithNoData(long studyId, int days) {

		Connection conn = null;
		ArrayList<Participant> list = new ArrayList<>();
		if (studyId < 1 || days < 1) {
			return list;
		}
		try {
			conn = connect();

			String query = "select * from mcs.enrollment where study_id=" + studyId
					+ " and concat(participant_email, '-', device_uuid) not in ("
					+ " select distinct concat(user_email, '-', device_uuid) from mcs.object_history where study_id="
					+ studyId + " and upload_time is not null" + " and (unix_timestamp(now()) - upload_time) < " + days
					+ " * 24 * 3600)";

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {

				try {
					Participant participant = new Participant();
					participant.setStudyId(studyId);
					String email = resultSet.getString("participant_email");
					participant.setUserEmail(email);
					String identifier = resultSet.getString("device_uuid");
					participant.setIdentifier(identifier);

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
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return list;
	}

	@Override
	public ArrayList<DaywiseCount> getDailyLabelCount(long studyId, int days) {
		Connection conn = null;
		ArrayList<DaywiseCount> list = new ArrayList<>();
		if (studyId < 1 || days < 1) {
			return list;
		}
		try {
			conn = connect();

			String query = "select count(*) as cn, from_unixtime(label_time, '%b %d') as dt from mcs.labeling_history where label_type='start' "
					+ "and study_id="+ studyId +" and (unix_timestamp(now()) - label_time) < "+ days +" * 24 * 3600 group by dt";

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {

				try {
					int count = resultSet.getInt("cn");
					String date = resultSet.getString("dt");

					list.add(new DaywiseCount(date, count));

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
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return list;

	}

	@Override
	public ArrayList<DaywiseCount> getDailyLabelCountFromUniqueDevices(long studyId, int days) {
		Connection conn = null;
		ArrayList<DaywiseCount> list = new ArrayList<>();
		if (studyId < 1 || days < 1) {
			return list;
		}
		try {
			conn = connect();

			String query = "select count(*) as cn, date_format(dt, '%b %d') as dwise from (select "
					+ "distinct user_email, device_uuid, from_unixtime(label_time, '%Y-%m-%d') as dt from mcs.labeling_history where label_type='start' "
					+ "and study_id="+ studyId +" and  (unix_timestamp(now()) - label_time) < "+ days +" * 24 * 3600) t group by t.dt";

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {

				try {
					int count = resultSet.getInt("cn");
					String date = resultSet.getString("dwise");

					list.add(new DaywiseCount(date, count));

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			System.out.println("Closing the connection.");
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return list;
	}

	@Override
	public ArrayList<Participant> getParticipantsWithNoLabel(long studyId, int days) {

		Connection conn = null;
		ArrayList<Participant> list = new ArrayList<>();
		if (studyId < 1 || days < 1) {
			return list;
		}
		try {
			conn = connect();

			String query = "select * from mcs.enrollment where study_id="+ studyId +" and concat(participant_email, '-', device_uuid) not in ("
					+ "select distinct concat(user_email, '-', device_uuid) from mcs.labeling_history "
					+ "where str_to_date(label_time, '%Y-%m-%d') >= date(now()) - interval "+ (days+1) +" day)";

			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {

				try {
					Participant participant = new Participant();
					participant.setStudyId(studyId);
					String email = resultSet.getString("participant_email");
					participant.setUserEmail(email);
					String identifier = resultSet.getString("device_uuid");
					participant.setIdentifier(identifier);

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
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return list;
	}

	
	// ------------------File Mapping ----------------------//
	@Override
	public ArrayList<FileIdentifier> getFileIdentifiers(long studyId) {

		Connection conn = null;
		ArrayList<FileIdentifier> list = new ArrayList<>();
		try {
			conn = connect();

			String query = "select * from mcs.email_uuid_mapping where user_email in "
					+ "(select participant_email from mcs.enrollment where study_id=" + studyId + ")";
			System.out.println("query: " + query);
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {

				try {
					FileIdentifier identifier = new FileIdentifier();

					String email = resultSet.getString("user_email");
					identifier.setEmail(email);

					String uuid = resultSet.getString("device_uuid");
					identifier.setUuid(uuid);
					list.add(identifier);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			System.out.println("Closing the connection.");
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}

		}
		return list;

	}
	
	@Override
	public Response sendNotification(ArrayList<Participant> participants) {
		Response response = new Response(0, "success");
		return response;
	}

}
