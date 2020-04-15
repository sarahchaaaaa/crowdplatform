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

import mlab.mcsweb.client.services.LabelingService;
import mlab.mcsweb.shared.LabelingInfo;
import mlab.mcsweb.shared.PingInfo;
import mlab.mcsweb.shared.Util;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LabelingServiceImpl extends RemoteServiceServlet implements LabelingService {

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

	@Override
	public ArrayList<LabelingInfo> getLabelingHistory(long studyId, String email, String uuid) {
		ArrayList<LabelingInfo> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {

			connection = connect();
			
			email = Util.isEmptyString(email) ? "" : email;
			uuid = Util.isEmptyString(uuid) ? "" : uuid;
			
			System.out.println("get labeling history study id:"+ studyId + ", email:" + email + ", uuid:"+ uuid);

			String query = "select * from mcs.labeling_history where study_id = ? and (user_email = ? or device_uuid = ?) order by label_time desc";
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, studyId);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, uuid);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				try {
					LabelingInfo info = new LabelingInfo();

					info.setStudyId(studyId);
					info.setEmail(email);
					info.setUuid(uuid);


					String time = resultSet.getString("label_time");
					info.setTime(time);

					String label = resultSet.getString("label");
					info.setLabel(label);

					String labelType = resultSet.getString("label_type");
					info.setType(labelType);

					list.add(info);

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
		return list;
	}

}
