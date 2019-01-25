package crowdsense.cimoninterface.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import crowdsense.cimoninterface.util.DatabaseUtil;
import crowdsense.cimoninterface.util.Response;
import crowdsense.cimoninterface.util.ServiceUtil;

@Path("/signup")
public class SignupService {
	
	
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	@GET
	@Path("register")
	@Produces(MediaType.APPLICATION_JSON)
	public Response signup(@QueryParam("uuid") String uuid, @QueryParam("email") String email){
		Response response = new Response();
		if(ServiceUtil.isEmptyString(uuid) || ServiceUtil.isEmptyString(email)){
			response.setMessage("Invalid input");
			return response;
		}
		
		try {
			connection = DatabaseUtil.connectToDatabase();

			String token = ServiceUtil.randomString(4);

			preparedStatement = connection.prepareStatement("insert into mcs.mobile_users (email, device_uuid, token, state) values(?,?,?, 0) on"
					+ " duplicate key update token=?");
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, uuid);
			preparedStatement.setString(3, token);
			preparedStatement.setString(4, token);
			
			preparedStatement.execute();
			response.setCode(0);
			
			String subject = "Cimon Verification Token";
			String name = "User";
			String emailBody = 	"\n\nThank you for registering with the University of Notre Dame Crowd Sensing Initiative.\n"
								+ "\nVerification Token:"
								+ "\t" + token;

			ServiceUtil.sendEmail(email, subject, emailBody, name);
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setCode(-9);
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}

		}
		return response;

	}
	
	@GET
	@Path("verify")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyToken(@QueryParam("uuid") String uuid, @QueryParam("email") String email, @QueryParam("token") String token){
		Response response = new Response();
		
		if(ServiceUtil.isEmptyString(token)){
			response.setMessage("Invalid token");
		}
		
		if(ServiceUtil.isEmptyString(uuid) || ServiceUtil.isEmptyString(email)){
			response.setCode(-2);
			response.setMessage("Invalid input");
			return response;
		}
		
		try {
			connection = DatabaseUtil.connectToDatabase();
			
			System.out.println("email:"+ email + ", uuid:" + uuid);

			preparedStatement = connection.prepareStatement("select token from mcs.mobile_users where email=? and device_uuid=?");
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, uuid);
			
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				String dbToken = resultSet.getString("token");
				System.out.println("token from db:"+ dbToken + ", from client:"+ token);
				if(dbToken.equals(token)){
					System.out.println("token match");
					try {
						preparedStatement = connection.prepareStatement("update mcs.mobile_users set state=1 where email=? and device_uuid=?");
						preparedStatement.setString(1, email);
						preparedStatement.setString(2, uuid);

						preparedStatement.executeUpdate();
						System.out.println("going to update db");
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					response.setCode(0);
				} else{
					System.out.println("token does not match");
					response.setCode(-3);
					response.setMessage("Token mismatch");
				}
			}else{
				response.setCode(-2);
				response.setMessage("Invalid input");				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setCode(-9);
			response.setMessage("Service not available");
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}

		}
		return response;
	}
	
	@POST
	@Path("/push_register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerPushToken(final PushRegistry registry){
		Response response = new Response();
		if(ServiceUtil.isEmptyString(registry.getUserEmail()) || ServiceUtil.isEmptyString(registry.getDeviceUuid()) || ServiceUtil.isEmptyString(registry.getPuskToken())){
			response.setMessage("Invalid Input");
			return response;
		}
		try {
			connection = DatabaseUtil.connectToDatabase();
			
			//System.out.println("");

			String query = "insert into mcs.push_register (participant_email, device_uuid, push_token, last_register_time, last_register_time_zone, active)"
					+ " values (?,?,?,?,?, 1) on duplicate key update push_token=?, last_register_time=?, last_register_time_zone=?, active=1";
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, registry.getUserEmail());
			preparedStatement.setString(2, registry.getDeviceUuid());
			preparedStatement.setString(3, registry.getPuskToken());
			preparedStatement.setString(4, registry.getLastRegisterTime());
			preparedStatement.setString(5, registry.getLastRegisterTimeZone());
			
			//on duplicate
			preparedStatement.setString(6, registry.getPuskToken());
			preparedStatement.setString(7, registry.getLastRegisterTime());
			preparedStatement.setString(8, registry.getLastRegisterTimeZone());
			
			preparedStatement.execute();
			response.setCode(0);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}
		}
		
		return response;
	}

}
