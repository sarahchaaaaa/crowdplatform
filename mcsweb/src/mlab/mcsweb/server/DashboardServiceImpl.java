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
import mlab.mcsweb.shared.FileIdentifier;

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
			dbUrl = properties.getProperty("db_host") + "/" + properties.getProperty("db_schema") + "?serverTimezone=UTC";
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
	public ArrayList<FileIdentifier> getFileIdentifiers(long studyId) {
		
		Connection conn = null;
		ArrayList<FileIdentifier> list = new ArrayList<>();
		try {
			conn = connect();

			String query = "select * from mcs.email_uuid_mapping where user_email in "
					+ "(select participant_email from mcs.enrollment where study_id="+ studyId +");";
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

}
