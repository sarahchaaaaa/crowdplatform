package services.study;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import commons.DatabaseUtil;

@Path("study/{studyId}/dashboard")
public class StudyDashboardService {

	private Connection connection;
	private PreparedStatement preparedStatement;


	@Path("filemapping")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<FileIdentifier> getFileIdentifiers(@PathParam("studyId") String studyId) {
		ArrayList<FileIdentifier> list = new ArrayList<>();
		try {
			long id = Long.parseLong(studyId);
			connection = DatabaseUtil.connectToDatabase();

			String query = "select * from mcs.email_uuid_mapping where user_email in "
					+ "(select participant_email from mcs.enrollment where study_id="+ id +");";
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
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
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}

		}
		return list;
	}
	

}
