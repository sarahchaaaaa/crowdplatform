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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import org.apache.commons.codec.binary.Base64;

import commons.DatabaseUtil;
import commons.Response;
import commons.ServiceUtil;

@Path("study/{studyId}/participation")
public class ParticipationService {

	private Connection connection;
	private PreparedStatement preparedStatement;


	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Participant> getAllParticipants(@PathParam("studyId") String studyId) {
		ArrayList<Participant> list = new ArrayList<>();
		try {
			long id = Long.parseLong(studyId);
			connection = DatabaseUtil.connectToDatabase();

			String query = "select * from mcs.enrollment where study_id = " + id + " order by participant_email";
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {

				try {
					Participant participant = new Participant();
					participant.setStudyId(id);

					String email = resultSet.getString("participant_email");
					participant.setUserEmail(email);

					String firstName = resultSet.getString("first_name");
					participant.setFirstName(firstName);

					String lastName = resultSet.getString("last_name");
					participant.setLastName(lastName);

//					String organization = resultSet.getString("organization");
//					participant.setOrganization(organization);
//
//					String status = resultSet.getString("status");
//					participant.setStatus(status);

					int state = resultSet.getInt("state");
					if (state == 1) {
						participant.setStatus("enrolled");
					}else {
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
	
	@Path("add")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response enroll(JAXBElement<Participant> jaxbElement){
		Response response = new Response();
		Participant participant = jaxbElement.getValue();
		if (ServiceUtil.isEmptyString(participant.getUserEmail())) {
			return response;
		}
		try {
			connection = DatabaseUtil.connectToDatabase();
			String query = "insert into mcs.enrollment (study_id, participant_email, state,"
					+ " first_name, last_name) values (?,?,?,?,?) on duplicate key update state=?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, participant.getStudyId());
			preparedStatement.setString(2, participant.getUserEmail());
			preparedStatement.setInt(3, 1);
			preparedStatement.setString(4, participant.getFirstName());
			preparedStatement.setString(5, participant.getLastName());
			preparedStatement.setInt(6, 1);
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
	
	@Path("update/{email}")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateInfo(JAXBElement<Participant> jaxbElement, @PathParam("studyId") String studyId, @PathParam("email") String email){
		Response response = new Response();
		Participant participant = jaxbElement.getValue();
		if (ServiceUtil.isEmptyString(participant.getUserEmail())) {
			return response;
		}
		try {
			connection = DatabaseUtil.connectToDatabase();
			String query = "update mcs.enrollment set participant_email=?, first_name=?, last_name=?, state=1"
					+ " where study_id=? and participant_email=?";
			System.out.println("query " + query);
			long id = Long.parseLong(studyId);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, participant.getUserEmail());
			preparedStatement.setString(2, participant.getFirstName());
			preparedStatement.setString(3, participant.getLastName());
			preparedStatement.setLong(4, id);
			preparedStatement.setString(5, email);
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
			String query = "delete from mcs.enrollment where study_id=? and participant_email=?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			for (String email : participantList) {
				try {
					preparedStatement.setLong(1, id);;
					preparedStatement.setString(2, email);
					System.out.println("id:" + id + ", email:" + email);
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
	
	
	private boolean isEnrolled(long studyId, String email){
		try {
			connection = DatabaseUtil.connectToDatabase();
			String query = "select * from mcs.enrollment where study_id=? and participant_email = ?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, studyId);
			preparedStatement.setString(2, email);
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
	
	@Path("pinghistory")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<PingInfo> getPingInfo(@PathParam("studyId") String studyId, @QueryParam("email") String email, @QueryParam("days") int days) {
		ArrayList<PingInfo> list = new ArrayList<>();
		try {
			long id = Long.parseLong(studyId);
			if (ServiceUtil.isEmptyString(email)) {
				return list;
			}
			
			if (isEnrolled(id, email)) {
				connection = DatabaseUtil.connectToDatabase();
				
				String query = "select *, from_unixtime(ping_time) as datetime from mcs.ping_history where user_email = ? "
						+ "and from_unixtime(ping_time )  between (now() - interval ? day) and now() order by ping_time desc";
				System.out.println("query: " + query);
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, email);
				preparedStatement.setInt(2, days);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {

					try {
						PingInfo info = new PingInfo();

						String userEmail = resultSet.getString("user_email");
						info.setEmail(userEmail);
						
						String uuid = resultSet.getString("device_uuid");
						info.setUuid(uuid);
						
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


}
