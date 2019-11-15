package mlab.mcsweb.server;

import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.Session;

import mlab.mcsweb.client.services.GreetingService;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.User;
import mlab.mcsweb.shared.Util;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	static String serverRoot = "";
	//private static final String DRIVER = "com.mysql.jdbc.Driver";
	
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static String dbUrl, dbUsername, dbPassword;

	private static String gmailAccount, gmailUser, gmailPass;

	
	static {
		try {
			Class.forName(DRIVER).newInstance();
			System.out.println("Load DB driver successfully");
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
			dbUrl = properties.getProperty("db_host") + "/" + properties.getProperty("db_schema");
			dbUsername = properties.getProperty("db_username");
			dbPassword = properties.getProperty("db_password");
			System.out.println("db prop, dburl:" + dbUrl + ", user:" + dbUsername + ", pass:" + dbPassword);
			
			gmailAccount = properties.getProperty("gmail_account");
			gmailUser = properties.getProperty("gmail_user");
			gmailPass = properties.getProperty("gmail_password");
			System.out.println("Gmail account:"+ gmailAccount + ", user:"+ gmailUser + ", gmail pass:"+ gmailPass);

		} catch (Exception e) {
			e.printStackTrace();
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
	
//	private Connection connect(){
//		try {
//			System.out.println("In the new connect method.....s");
//			int lport=5656;
//		    String rhost="mlab.crc.nd.edu";
//		    String host="mlab.crc.nd.edu";
//		    int rport=3306;
//		    String sshUser="mlabguest";
//		    String sshPassword="mL@bGuest11";
//	        String url = "jdbc:mysql://mlab.crc.nd.edu:"+lport+"/mcs";
//	        String driverName="com.mysql.jdbc.Driver";
//	        Connection conn = null;
//	        com.jcraft.jsch.Session session= null;
//		    try{
//		    	//Set StrictHostKeyChecking property to no to avoid UnknownHostKey issue
//		    	java.util.Properties config = new java.util.Properties(); 
//		    	config.put("StrictHostKeyChecking", "no");
//		    	System.out.println("config has been updated......");
//		    	JSch jsch = new JSch();
//		    	System.out.println("JSch object created......");
//		    	session=jsch.getSession(sshUser, host, 22);
//		    	session.setPassword(sshPassword);
//		    	session.setConfig(config);
//		    	System.out.println("going to connect session.......");
//		    	session.connect();
//		    	System.out.println("Connected");
//		    	int assinged_port=session.setPortForwardingL(lport, rhost, rport);
//		        System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
//		    	System.out.println("Port Forwarded");
//		    	
//		    	//mysql database connectivity
//	            Class.forName(driverName).newInstance();
//	            conn = DriverManager.getConnection (url, dbUsername, dbPassword);
//	            System.out.println ("Database connection established");
//	            System.out.println("DONE");
//	            return conn;
//		    }catch(Exception e){
//		    	e.printStackTrace();
//		    	//System.out.println("Exception in database session connection " + e.getLocalizedMessage());
//		    }finally{
//		    	if(conn != null && !conn.isClosed()){
//		    		System.out.println("Closing Database Connection");
//		    		conn.close();
//		    	}
//		    	if(session !=null && session.isConnected()){
//		    		System.out.println("Closing SSH Connection");
//		    		session.disconnect();
//		    	}
//		    }
//		
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return null;
//	}


	@Override
	public Response signup(User user) {

		Response response = new Response();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = connect();
			String email = user.getEmail().trim();

			preparedStatement = connection.prepareStatement("select * from mcs.users where email=?");
			preparedStatement.setString(1, user.getEmail());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				response.setCode(1);
				response.setMessage("Email already exist");
			} else {
				preparedStatement = connection
						.prepareStatement("insert ignore into  mcs.users (email, password, token) values ( ?,?,?)");
				preparedStatement.setString(1, email);
				preparedStatement.setString(2, user.getPassword());
				String randomString = randomString(32);
				preparedStatement.setString(3, randomString);
				preparedStatement.execute();

				preparedStatement = connection.prepareStatement("select token from mcs.users where email=?");
				preparedStatement.setString(1, email);
				resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					// got token
					String token = resultSet.getString("token");

					String subject = "Activate Account";
					String name = "";
					String emailBody = "\n\nThank you for registering with the mLab crowdsensing platform Koios.\n"
							+ "\nClick the link below to complete your registration:\n"
							+ getEmailVerificationRoot() + "verifyemail?token=" + token;

					sendEmail(email, subject, emailBody, name);
					System.out.println("Email has been sent to verify account");
				} else {
					System.out.println("This case should not happen, there must be a token after insertion");
				}

				response.setCode(0);

			}

		} catch (Exception e) {
			System.out.println("Error in user creation " + e.getMessage());
			e.printStackTrace();
			response.setCode(-1);
			response.setMessage("Error in user creation. Try later.");
		} finally {
			System.out.println("Finally closing the connection.");
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}
		}
		return response;

	}
	
	private String getEmailVerificationRoot() {
		// return "http://129.74.247.110/mcsweb/";
		return "https://koiosplatform.com/mcsweb/";
	}
	
	private String randomString(int len) {
		final String alphanumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(alphanumeric.charAt(rnd.nextInt(alphanumeric.length())));
		return sb.toString();
	}


	private boolean sendEmail(String toAddress, String subject, String messageBody, String name) {

		try {
//			InputStream propertiesInputStream = null;
//			Properties properties = new Properties();
//			propertiesInputStream = GreetingServiceImpl.class.getClassLoader()
//					.getResourceAsStream("../system.properties");
//			properties.load(propertiesInputStream);
//			final String emailAccount = properties.getProperty("gmail_account");
//			final String emailUser = properties.getProperty("gmail_user");
//			final String emailPassword = properties.getProperty("gmail_password");

			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(gmailUser, gmailPass);
					//return new PasswordAuthentication(emailUser, emailPassword);
					// return new PasswordAuthentication("ndspeechrepo",
					// "mcomlab2017");

				}
			});

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(gmailAccount));
				//message.setFrom(new InternetAddress(emailAccount));
				// message.setFrom(new
				// InternetAddress("ndspeechrepo@gmail.com"));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
				message.setSubject(subject);
				if (Util.isEmptyString(name.trim())) {
					name = "User";
				}
				String header = "Dear " + name + ",\n";
				String footer = "\n\nRegards,\nMobile Computing Lab,\nUniversity of Notre Dame";
				message.setText(header + messageBody + footer);

				Transport.send(message);

				System.out.println("Done");
				return true;
			} catch (MessagingException e) {
				// throw new RuntimeException(e);
				e.printStackTrace();
			}
			return false;

		} catch (Exception e) {
			System.out.println("Error while sending email "+ e.getMessage());
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	
	@Override
	public Response login(User user) {
		Connection conn = null;
		Response response = new Response();
		try {
			conn = connect();
			String query = "select * from mcs.users where email=?";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
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
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}
		}
		return response;
	}

	@Override
	public Response createStudy(Study study) {

		Response response = new Response();
		Connection connection = null;
		try {
			connection = connect();

			String query = "select * from mcs.study where created_by=? and name=?";
			System.out.println("query:" + query);
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, study.getCreatedBy());
			preparedStatement.setString(2, study.getName());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				response.setCode(1);
				response.setMessage("Name already exist");
			} else {
				query = "insert into mcs.study (name, description, created_by, creation_time, creation_time_zone, is_public, instruction, icon_url) "
						+ " values (?,?,?,?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, study.getName());
				preparedStatement.setString(2, study.getDescription());
				preparedStatement.setString(3, study.getCreatedBy());
				preparedStatement.setString(4, study.getCreationTime());
				preparedStatement.setString(5, study.getCreationTimeZone());
				preparedStatement.setInt(6, study.getIsPublic());
				preparedStatement.setString(7, study.getInstruction());
				preparedStatement.setString(8, study.getIconUrl());

				preparedStatement.execute();

				query = "select last_insert_id() as id";
				preparedStatement = connection.prepareStatement(query);
				resultSet = preparedStatement.executeQuery();
				String id = "";
				if (resultSet.next()) {
					id = resultSet.getString("id");
				}

				response.setCode(0);
				response.setMessage(id);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return response;

	}

	// private Response genericPostMethod(String url, Object genericObject) {
	// long start = Calendar.getInstance().getTimeInMillis();
	// ClientConfig config = new DefaultClientConfig();
	// Client client = Client.create(config);
	// WebResource service = client.resource(url);
	// String result =
	// service.accept(MediaType.APPLICATION_JSON).post(String.class,
	// genericObject);
	// Response response = new Gson().fromJson(result, Response.class);
	//
	// System.out.println("response code generic post method: " +
	// response.getCode());
	// long end = Calendar.getInstance().getTimeInMillis();
	// System.out.println("time diff generic post call: " + url + ", time to
	// complete: " + (end - start) + "\n");
	//
	// return response;
	//
	// }

	@Override
	public ArrayList<Study> getStudyList(String email) {

		ArrayList<Study> studyList = new ArrayList<>();
		Connection connection = null;
		if (email == null || email.isEmpty()) {

		} else {
			try {

				connection = connect();
				String query = "select * from mcs.study where created_by='" + email
						+ "' or id in (select study_id from mcs.collaborators where collaborator_email='" + email + "')"
						+ " order by creation_time desc";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					try {
						Study study = new Study();
						int id = resultSet.getInt("id");
						String name = resultSet.getString("name");
						String description = resultSet.getString("description");
						String createdBy = resultSet.getString("created_by");
						int state = resultSet.getInt("state");

						study.setId(id);
						study.setName(name);
						study.setDescription(description);
						study.setCreatedBy(createdBy);
						study.setState(state);

						studyList.add(study);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

		}

		return studyList;

	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
