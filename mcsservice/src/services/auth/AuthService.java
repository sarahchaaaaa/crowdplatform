package services.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import commons.ServiceUtil;

@Path("/auth")
public class AuthService {

	private Connection connection;
	private PreparedStatement preparedStatement;

	/**
	 * 
	 * @param userProfileJAXBElement
	 * @return code=0: success, =1: email exist, =-1: failure
	 */
	@POST
	@Path("signup")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signup(JAXBElement<User> userProfileJAXBElement) {
		User user = userProfileJAXBElement.getValue();
		// String success = "";
		Response response = new Response();
		try {

			System.out.println("Connecting database...");
			connection = DatabaseUtil.connectToDatabase();
			try {
				// statement = connection.createStatement();
				String email = user.getEmail().trim();

				preparedStatement = connection.prepareStatement("select * from mcs.users where email=?");
				preparedStatement.setString(1, user.getEmail());
				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					response.setCode(1);
					response.setMessage("Email already exist");
				} else{
					preparedStatement = connection
							.prepareStatement("insert ignore into  mcs.users (email, password, token) values ( ?,?,?)");
					preparedStatement.setString(1, email);
					preparedStatement.setString(2, user.getPassword());
					preparedStatement.setString(3, ServiceUtil.randomString(32));
					preparedStatement.execute();

					preparedStatement = connection.prepareStatement("select token from mcs.users where email=?");
					preparedStatement.setString(1, email);
					resultSet = preparedStatement.executeQuery();
					if (resultSet.next()) {
						// got token
						String token = resultSet.getString("token");
						String subject = "Activate Account";
						String name = "";
						String emailBody = "\n\nThank you for registering with the MLab Crowdsensing Platform.\n"
								+ "\nClick the link below to complete your registration:\n"
								+ ServiceUtil.getEmailVerificationRoot() + "verifyemail?token=" + token;

						ServiceUtil.sendEmail(email, subject, emailBody, name);
					}

					// } catch (MySQLIntegrityConstraintViolationException e){
					// success = "The user " + userProfile.getEmail() +
					// " already exist in the system." ;
					response.setCode(0);
					
				}


			} catch (SQLException e) {
				// success =
				// "Error in user creation. Please refer server logs for more
				// information";
				e.printStackTrace();
				response.setCode(-1);
				response.setMessage("Error in user creation. Try later.");
			}
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

	@GET
	@Path("verification/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response emailVerification(@PathParam("token") String token) {
		Response response = new Response();
		try {
			connection = DatabaseUtil.connectToDatabase();
			// statement = connection.createStatement();

			preparedStatement = connection.prepareStatement("select * from mcs.users where token=?");
			preparedStatement.setString(1, token);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int state = resultSet.getInt("state");
				if (state == 0) {
					preparedStatement = connection.prepareStatement("update mcs.users set state=1 where token=?");
					preparedStatement.setString(1, token);
					preparedStatement.executeUpdate();
					// successfully update account state
					response.setCode(0);
				} else {
					// email already verified
					response.setCode(2);
				}
			} else {
				// token does not exist
				response.setCode(1);
				response.setMessage("Invalid verification code. Please sign up.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
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
	@Path("login")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(JAXBElement<User> userJaxbElement) {
		Response response = new Response();
		try {
			User user = userJaxbElement.getValue();
			System.out.println("pass from client:" + user.getPassword());
			connection = DatabaseUtil.connectToDatabase();
			connection.createStatement();
			preparedStatement = connection.prepareStatement("select * from mcs.users where email=?");
			preparedStatement.setString(1, user.getEmail());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int state = resultSet.getInt("state");
				if (state == 0) {
					response.setCode(2);
					response.setMessage("Please verify your account before login");
				} else {
					// check password
					if (user.getPassword().equals(resultSet.getString("password"))) {
						response.setCode(0);
					} else {
						response.setCode(3);
						response.setMessage("Incorrect password");
					}

				}
			} else {
				response.setCode(1);
				response.setMessage("Incorrect username");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			response.setCode(-1);
			response.setMessage("Service not available, please try later");
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

	@Path("reset")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_JSON })
	/**
	 * 
	 * @param email
	 * @return code=0: success, code=-1:failure, code=1: user doesn't exist
	 */
	public Response resetPassword(JAXBElement<User> userJaxbElement) {
		Response response = new Response();
		try {
			connection = DatabaseUtil.connectToDatabase();
			connection.createStatement();

			User user = userJaxbElement.getValue();

			preparedStatement = connection.prepareStatement("select email from mcs.users where state=1 and email=?");
			preparedStatement.setString(1, user.getEmail());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String newPass = ServiceUtil.randomString(12);
				String encryptedNewpass = ServiceUtil.getMD5String(newPass);
				preparedStatement = connection.prepareStatement("update mcs.users set password= ? where email=?");
				preparedStatement.setString(1, encryptedNewpass);
				preparedStatement.setString(2, user.getEmail());
				preparedStatement.executeUpdate();

				try {
					String subject = "Reset Speech Marker Password";
					String emailBody = "\n\nYour password has been reset, and use new one to login. Please change your password from the profile section.\n"
							+ "\n New Password:		" + newPass;

					ServiceUtil.sendEmail(user.getEmail(), subject, emailBody, "");

				} catch (Exception e) {
					// TODO: handle exception
				}

				response.setCode(0);

			} else {
				response.setCode(1);
				response.setMessage("User does not exist");
			}
		} catch (Exception ex) {

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
	@Path("test")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String test(final String input) {
		return "{\"result\": \"Hello world\"}";

	}

}
