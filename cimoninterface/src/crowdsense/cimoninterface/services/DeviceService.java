package crowdsense.cimoninterface.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import crowdsense.cimoninterface.util.Response;
import crowdsense.cimoninterface.util.Util;


@Path("/device")
public class DeviceService {

	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
		
	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertPingData(@HeaderParam(Util.globalServiceKey) String keyValue, @QueryParam("email") String email, @QueryParam("uuid") String uuid, 
			@QueryParam("os_type") String osType, @QueryParam("os_version") String osVersion, @QueryParam("network") String network, 
			@QueryParam("app_version") String appVersion, @QueryParam("data") String data) {
		if (!Util.isClientValid(keyValue)) {
			return null;
		}
		
		Response response = new Response();

		if (Util.isEmptyString(email)) {
			response.setCode(-1);
			return response;
		}

		try {
			connection = Util.connectToDatabase();
			String query = "insert ignore into mcs.ping_history (user_email, device_uuid, ping_time, network, os_type, "
					+ "os_version, app_version, data) values (?, ?, unix_timestamp(), ?, ?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, uuid);
			preparedStatement.setString(3, network);
			preparedStatement.setString(4, osType);
			preparedStatement.setString(5,  osVersion);
			preparedStatement.setString(6, appVersion);
			preparedStatement.setString(7, data);
			
			preparedStatement.execute();
			response.setCode(0);
			
			try {
				resetNotificationProfile(email, uuid);
			} catch (Exception e) {
				// TODO: handle exception
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
	
	private  void resetNotificationProfile(String email, String uuid){
		Connection connection = null;
		try {
			connection = Util.connectToDatabase();
			String query = "";
			//device defined identifier
			if(uuid.length()>30){
				query = "update mcs.auto_notification_profile set current_count=0, total_count=0, last_push_time='', admin_notified=0"
						+ " where user_email=?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, email);
			}else{
				query = "update mcs.auto_notification_profile set current_count=0, total_count=0, last_push_time='', admin_notified=0"
						+ " where user_email=? and device_uuid=?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, email);
				preparedStatement.setString(2, uuid);
	
			}
			
			preparedStatement.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}

		}
	}

	
	@GET
	@Path("fcm")
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertFcm(@HeaderParam(Util.globalServiceKey) String keyValue, @QueryParam("email") String email, @QueryParam("uuid") String uuid, 
			@QueryParam("fcm_token") String fcmToken, @QueryParam("device_token") String deviceToken) {
		
		if (!Util.isClientValid(keyValue)) {
			return null;
		}

		
		Response response = new Response();
				
		if (Util.isEmptyString(email) || Util.isEmptyString(fcmToken)) {
			System.out.println("email or fcm token is empty");
			response.setCode(-1);
			return response;
		}

		try {
			connection = Util.connectToDatabase();
			String query = "insert into mcs.fcm_register (user_email, device_uuid, fcm_token, device_token, "
					+ "last_register_time) values (?, ?, ?, ?, unix_timestamp()) on duplicate key update "
					+ " fcm_token=?, device_token=?";
			System.out.println("going to insert fcm register, email:" + email + ", uuid:" + uuid + ", fcm:"+ fcmToken + ", device_token:"+ deviceToken);
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, uuid);
			preparedStatement.setString(3, fcmToken);
			preparedStatement.setString(4, deviceToken == null ? "" : deviceToken);
			
			preparedStatement.setString(5, fcmToken);
			preparedStatement.setString(6, deviceToken == null ? "" : deviceToken);

			preparedStatement.execute();
			response.setCode(0);

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

	
}


//@Path("/device")
//public class DeviceService {
//
//	private Connection connection = null;
//	private PreparedStatement preparedStatement = null;
//	public final static String globalServiceKey = "secret";
//	public final static String globalServiceValue = "koiosByAfzalFrommLabND@sralabDoD";
//
//	@GET
//	@Path("ping")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response insertPingData(@HeaderParam(globalServiceKey) String keyValue, @QueryParam("email") String email, @QueryParam("uuid") String uuid, 
//			@QueryParam("os_type") String osType, @QueryParam("os_version") String osVersion, @QueryParam("network") String network, 
//			@QueryParam("app_version") String appVersion, @QueryParam("data") String data) {
//		
//		Response response = new Response();
//		
//		if (Util.isEmptyString(keyValue)) {
//			System.out.println("host is empty");
//			return null;
//		}else {
//			System.out.println("host value received from header " + keyValue);
//			if (!globalServiceValue.equals(keyValue)) {
//				return null;
//			}
//		}
//
//		if (Util.isEmptyString(email)) {
//			response.setCode(-1);
//			return response;
//		}
//
//		try {
//			connection = DatabaseUtil.connectToDatabase();
//			String query = "insert ignore into mcs.ping_history (user_email, device_uuid, ping_time, network, os_type, "
//					+ "os_version, app_version, data) values (?, ?, unix_timestamp(), ?, ?, ?, ?, ?)";
//			
//			preparedStatement = connection.prepareStatement(query);
//			preparedStatement.setString(1, email);
//			preparedStatement.setString(2, uuid);
//			preparedStatement.setString(3, network);
//			preparedStatement.setString(4, osType);
//			preparedStatement.setString(5,  osVersion);
//			preparedStatement.setString(6, appVersion);
//			preparedStatement.setString(7, data);
//			
//			preparedStatement.execute();
//			response.setCode(0);
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		} finally {
//			if (connection != null)
//				try {
//					connection.close();
//				} catch (SQLException ignore) {
//				}
//
//		}
//		return response;
//
//	}
//
//	
//	@GET
//	@Path("fcm")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response insertFcm(@HeaderParam(globalServiceKey) String keyValue, @QueryParam("email") String email, @QueryParam("uuid") String uuid, 
//			@QueryParam("fcm_token") String fcmToken, @QueryParam("device_token") String deviceToken) {
//		
//		Response response = new Response();
//				
//		if (Util.isEmptyString(keyValue)) {
//			System.out.println("host is empty");
//			return null;
//		}else {
//			System.out.println("host value received from header " + keyValue);
//			if (!globalServiceValue.equals(keyValue)) {
//				System.out.println("host value not matched");
//				return null;
//			}
//		}
//
//		if (Util.isEmptyString(email) || Util.isEmptyString(fcmToken)) {
//			System.out.println("email or fcm token is empty");
//			response.setCode(-1);
//			return response;
//		}
//
//		try {
//			connection = DatabaseUtil.connectToDatabase();
//			String query = "insert into mcs.fcm_register (user_email, device_uuid, fcm_token, device_token, "
//					+ "last_register_time) values (?, ?, ?, ?, unix_timestamp()) on duplicate key update "
//					+ " fcm_token=?, device_token=?";
//			System.out.println("going to insert fcm register, email:" + email + ", uuid:" + uuid + ", fcm:"+ fcmToken + ", device_token:"+ deviceToken);
//			
//			preparedStatement = connection.prepareStatement(query);
//			preparedStatement.setString(1, email);
//			preparedStatement.setString(2, uuid);
//			preparedStatement.setString(3, fcmToken);
//			preparedStatement.setString(4, deviceToken == null ? "" : deviceToken);
//			
//			preparedStatement.setString(5, fcmToken);
//			preparedStatement.setString(6, deviceToken == null ? "" : deviceToken);
//
//			preparedStatement.execute();
//			response.setCode(0);
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		} finally {
//			if (connection != null)
//				try {
//					connection.close();
//				} catch (SQLException ignore) {
//				}
//
//		}
//		return response;
//
//	}
//
//	
//}
