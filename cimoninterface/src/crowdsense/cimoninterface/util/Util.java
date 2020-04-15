package crowdsense.cimoninterface.util;



import java.io.InputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Util {
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    
    
	public final static String globalServiceKey = "secret";
	public final static List<String> globalServiceValue = Arrays.asList(new String[]{"koiosByAfzalFromNDmL@b", "koiosByAfzalFrommLabND@sralabDoD"});

    
    static String dbUrl, username, password;
    
	static final String numeric = "0123456789";
	static SecureRandom rnd = new SecureRandom();
	
	
	public static boolean isEmailFOrmatValid(Object value) {
		if (value == null)
			return true;

		String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$";

		boolean valid = false;

		if (value.getClass().toString().equals(String.class.toString())) {
			valid = ((String) value).matches(emailPattern);
		} else {
			valid = ((Object) value).toString().matches(emailPattern);
		}

		return valid;
	}
	
	public static boolean isURLFormatValid(String url) {
		
		if (url == null || url == "")
			return false;
		
		String urlPattern = "^(?:http(s)?:\\/\\/)?[\\w.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#[\\]@!\\$&'\\(\\)\\*\\+,;=.]+$";
		
		return (url).matches(urlPattern);
	
	}
	
	/**
	 * Per thread MD5 instance.
	 */
	/*
	 * private static final ThreadLocal<MessageDigest> perThreadMd5 = new
	 * ThreadLocal<MessageDigest>(){
	 * 
	 * @Override protected MessageDigest initialValue() { try { return
	 * MessageDigest.getInstance("MD5"); } catch (NoSuchAlgorithmException e) {
	 * throw new RuntimeException("MD5 implementation not found", e); } }; };
	 */
	/**
	 * Generate MD5 digest.
	 * 
	 * @param input
	 *            input data to be hashed.
	 * @return MD5 digest.
	 */
	public static byte[] getMd5Digest(byte[] input) throws Exception {
		// MessageDigest md5 = perThreadMd5.get();
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.reset();
		md5.update(input);
		return md5.digest();
	}

	public static String getMD5String(String input) throws Exception {
		byte[] byteData = getMd5Digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}

	public static boolean isEmptyString(String input){
		if(input == null){
			return true;
		} else if (input.trim().isEmpty()) {
			return true;
		}else{
			return false;
		}
	}

	
	public static boolean isClientValid(String keyValue){
		if (isEmptyString(keyValue)) {
			System.out.println("host is empty");
			return false;
		}else {
			System.out.println("host value received from header " + keyValue);
			if (!Util.globalServiceValue.contains(keyValue)) {
				return false;
			}
		}
		return true;
	}

	
	public static String randomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( numeric.charAt( rnd.nextInt(numeric.length()) ) );
	   return sb.toString();
	}


	
	static {
		try {
			Class.forName(DRIVER).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try {			
			
		    InputStream propertiesInputStream = null;
            Properties properties = new Properties();
            propertiesInputStream = Util.class.getClassLoader().getResourceAsStream("../system.properties");
            properties.load(propertiesInputStream);
			dbUrl=properties.getProperty("db_host")+"/"+properties.getProperty("db_schema");
			username =properties.getProperty("db_username");
			password = properties.getProperty("db_password");
			
			System.out.println(Util.class.getSimpleName() + " : read properties, " + dbUrl + ", username:"+ username+", password:"+ password);
		} catch(Exception e ){
			e.printStackTrace();
		}

	}

	
	public static Connection connectToDatabase() {
		Connection connection = null;
		try {						
			System.out.println(Util.class.getSimpleName()+ ": Connecting database..., url:"+ dbUrl + ", username:"+ username+", password:"+ password);
            connection = DriverManager.getConnection(dbUrl + "?useSSL=false", username, password);
		} catch(Exception e ){
			e.printStackTrace();
		}
		
		return connection;
	}
	
	
	public static boolean sendEmail(String toAddress, String subject, String messageBody, String name) {
		
		try{
		    InputStream propertiesInputStream = null;
	        Properties properties = new Properties();
	        propertiesInputStream = Util.class.getClassLoader().getResourceAsStream("../system.properties");
	        properties.load(propertiesInputStream);
			final String emailAccount = properties.getProperty("gmail_account");
			final String emailUser =properties.getProperty("gmail_user");
			final String emailPassword = "mdwmmritkawvqcva"; 
					//properties.getProperty("gmail_password");
			
			
			
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(emailUser, emailPassword);
							//return new PasswordAuthentication("ndspeechrepo", "mcomlab2017");

						}
					});

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(emailAccount));
				//message.setFrom(new InternetAddress("ndspeechrepo@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(toAddress));
				message.setSubject(subject);
				if(isEmptyString(name.trim())){
					name = "User";
				}
				String header = "Dear "+ name + ",\n";
				String footer = "\n\nRegards,\nMobile Computing Lab,\n University of Notre Dame"; 
				message.setText(header + messageBody + footer);

				Transport.send(message);

				System.out.println("Done");
				return true;
			} catch (MessagingException e) {
				// throw new RuntimeException(e);
				e.printStackTrace();
			}
			return false;
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}


}


//import java.io.InputStream;
//import java.security.SecureRandom;
//import java.util.Properties;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import javax.ws.rs.core.MediaType;
//
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.client.config.ClientConfig;
//import com.sun.jersey.api.client.config.DefaultClientConfig;
//
//public class Util {
//	
//	static final String numeric = "0123456789";
//	static SecureRandom rnd = new SecureRandom();
//	
//	static{
//		System.out.println("Statis code execution from service util...");
//	}
//
//	public static String randomString( int len ){
//	   StringBuilder sb = new StringBuilder( len );
//	   for( int i = 0; i < len; i++ ) 
//	      sb.append( numeric.charAt( rnd.nextInt(numeric.length()) ) );
//	   return sb.toString();
//	}
//	public static boolean isEmptyString(String msg){
//		if(msg == null || msg.isEmpty()){
//			return true;
//		}
//		return false;
//	}
//	
//	
//	
//	public static boolean sendEmail(String toAddress, String subject, String messageBody, String name) {
//		
//		try{
//		    InputStream propertiesInputStream = null;
//	        Properties properties = new Properties();
//	        propertiesInputStream = DatabaseUtil.class.getClassLoader().getResourceAsStream("../system.properties");
//	        properties.load(propertiesInputStream);
//			final String emailAccount = properties.getProperty("gmail_account");
//			final String emailUser =properties.getProperty("gmail_user");
//			final String emailPassword = properties.getProperty("gmail_password");
//			
//			
//			
//			Properties props = new Properties();
//			props.put("mail.smtp.host", "smtp.gmail.com");
//			props.put("mail.smtp.socketFactory.port", "465");
//			props.put("mail.smtp.socketFactory.class",
//					"javax.net.ssl.SSLSocketFactory");
//			props.put("mail.smtp.auth", "true");
//			props.put("mail.smtp.port", "465");
//
//			Session session = Session.getDefaultInstance(props,
//					new javax.mail.Authenticator() {
//						protected PasswordAuthentication getPasswordAuthentication() {
//							//return new PasswordAuthentication(emailUser, emailPassword);
//							return new PasswordAuthentication("ndspeechrepo", "mcomlab2017");
//
//						}
//					});
//
//			try {
//
//				Message message = new MimeMessage(session);
//				//message.setFrom(new InternetAddress(emailAccount));
//				message.setFrom(new InternetAddress("ndspeechrepo@gmail.com"));
//				message.setRecipients(Message.RecipientType.TO,
//						InternetAddress.parse(toAddress));
//				message.setSubject(subject);
//				if(Util.isEmptyString(name.trim())){
//					name = "User";
//				}
//				String header = "Dear "+ name + ",\n";
//				String footer = "\n\nRegards,\nMobile Computing Lab,\n University of Notre Dame"; 
//				message.setText(header + messageBody + footer);
//
//				Transport.send(message);
//
//				System.out.println("Done");
//				return true;
//			} catch (MessagingException e) {
//				// throw new RuntimeException(e);
//				e.printStackTrace();
//			}
//			return false;
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return false;
//	}
//	
//	public static void main(String[] args) {
//		String json = "[{\"taskId\":3,\"answer\":\"\",\"surveyId\":13,\"submissionTime\":\"1521575818.45475\",\"studyId\":1,"
//				+ "\"answerType\":\"reference\",\"submissionTimeZone\":\"\",\"version\":0},{\"taskId\":2,\"answer\":\"yes\",\"surveyId\":"
//				+ "13,\"submissionTime\":\"1521575818.45315\",\"studyId\":1,\"answerType\":\"value\","
//				+ "\"submissionTimeZone\":\"\",\"version\":0}]";
//
//		ClientConfig config = new DefaultClientConfig();
//	    Client client = Client.create(config);
//		String url = "http://129.74.247.110/cimoninterface/study/survey/response";
//	    WebResource service = client.resource(url);
//		ClientResponse clientResponse = service.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);
//		System.out.println("response:"+ clientResponse);
//		String jsonResponse = clientResponse.getEntity(String.class);
//		System.out.println("json response:"+ jsonResponse);
//
//	}
//
//
//}
