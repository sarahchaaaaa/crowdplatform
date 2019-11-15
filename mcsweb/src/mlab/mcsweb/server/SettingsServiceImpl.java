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
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import mlab.mcsweb.client.services.SettingsService;
import mlab.mcsweb.shared.CloudStorageInfo;
import mlab.mcsweb.shared.MobileStorageInfo;
import mlab.mcsweb.shared.Response;

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
			dbUrl = properties.getProperty("db_host") + "/" + properties.getProperty("db_schema");
			dbUsername = properties.getProperty("db_username");
			dbPassword = properties.getProperty("db_password");
			System.out.println("db prop, dburl:" + dbUrl + ", user:" + dbUsername + ", pass:" + dbPassword);

		} catch (Exception e) {
		}

	}

//	private Connection connect() {
//		try {
//			return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return null;
//	}

	private Connection connect(){
		try {
			System.out.println("In the new connect method.....s");
			int lport=5656;
		    String rhost="mlab.crc.nd.edu";
		    String host="mlab.crc.nd.edu";
		    int rport=3306;
		    String sshUser="mlabguest";
		    String sshPassword="mL@bGuest11";
	        String url = "jdbc:mysql://localhost:"+lport+"/mydb";
	        String driverName="com.mysql.jdbc.Driver";
	        Connection conn = null;
	        Session session= null;
		    try{
		    	//Set StrictHostKeyChecking property to no to avoid UnknownHostKey issue
		    	java.util.Properties config = new java.util.Properties(); 
		    	config.put("StrictHostKeyChecking", "no");
		    	System.out.println("config has been updated......");
		    	JSch jsch = new JSch();
		    	System.out.println("JSch object created......");
		    	session=jsch.getSession(sshUser, host, 22);
		    	session.setPassword(sshPassword);
		    	session.setConfig(config);
		    	System.out.println("going to connect session.......");
		    	session.connect();
		    	System.out.println("Connected");
		    	int assinged_port=session.setPortForwardingL(lport, rhost, rport);
		        System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
		    	System.out.println("Port Forwarded");
		    	
		    	//mysql database connectivity
	            Class.forName(driverName).newInstance();
	            conn = DriverManager.getConnection (url, dbUsername, dbPassword);
	            System.out.println ("Database connection established");
	            System.out.println("DONE");
	            return conn;
		    }catch(Exception e){
		    	//e.printStackTrace();
		    	System.out.println("Exception in database session connection " + e.getLocalizedMessage());
		    }finally{
		    	if(conn != null && !conn.isClosed()){
		    		System.out.println("Closing Database Connection");
		    		conn.close();
		    	}
		    	if(session !=null && session.isConnected()){
		    		System.out.println("Closing SSH Connection");
		    		session.disconnect();
		    	}
		    }
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	
	//Collaborator Functions/////

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
			//String query = "insert into mcs.collaborators (study_id, participant_email, state,"
			//		+ " first_name, last_name) values (?,?,?,?,?) on duplicate key update state=?";
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
			//long id = participant.getStudyId();
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

			String query = "select * from mcs.collaborators where study_id = " + studyId + " order by collaborator_email";
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
		System.out.println("list:"+ list);
		try {
			String[] collaboratorList = list.trim().split("\\|");
			connection = connect();
			String query = "delete from mcs.collaborators where study_id=? and collaborator_email=?";
			System.out.println("query " + query);
			preparedStatement = connection.prepareStatement(query);
			for (String email : collaboratorList) {
				try {
					preparedStatement.setLong(1, studyId);;
					preparedStatement.setString(2, email);
					System.out.println("id:" + studyId + ", email:" + email);
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
	
	
	@Override
	public CloudStorageInfo getCloudSettingInfo(long studyId) {
		CloudStorageInfo info = null;  //new CloudStorageInfo to be populated in function from db
		//create connection
		//run query to select data from db
		//update info object if there is any result in resultset
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("Program in getCloudSettingInfo");
		try {
			connection = connect();

			String query = "select * from mcs.cloud_storage_setting where study_id = " + studyId ;
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				try {  //setting parts of info
	
					
					String modT = resultSet.getString("modification_time");
					String modTZ = resultSet.getString("modification_time_zone");
					String type = resultSet.getString("type");
					String params = resultSet.getString("parameter");
					
					
					//create new object with fields set (modifiedBy = blank)
					info = new CloudStorageInfo(studyId, "" , modT, modTZ, type, params);
	
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
		//create connect
		//insert or update
		//set response code properly
				
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
		
		MobileStorageInfo info = null;  //new CloudStorageInfo to be populated in function from db
		//create connection
		//run query to select data from db
		//update info object if there is any result in resultset
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("Program in getMobileStorageSetting");
		try {
			connection = connect();

			String query = "select * from mcs.mobile_storage_setting where study_id = " + studyId ;
			System.out.println("query: " + query);
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				try {  //setting parts of info
	
					
					String modT = resultSet.getString("modification_time");
					String modTZ = resultSet.getString("modification_time_zone");
					int fileSize = resultSet.getInt("file_object_size");
					int maxFile = resultSet.getInt("max_files");
					int maxCap = resultSet.getInt("max_capacity");
					
					
					//create new object with fields set (modifiedBy = blank)
					info = new MobileStorageInfo(studyId, "" , modT, modTZ, fileSize, maxFile, maxCap);
	
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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

	public Response updateMobileStorageSetting(MobileStorageInfo info) {
		//create connect
		//insert or update
		//set response code properly
						
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

	

}
