package crowdsense.cimoninterface.util;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ServiceUtil {
	
	static final String numeric = "0123456789";
	static SecureRandom rnd = new SecureRandom();
	
	static{
		System.out.println("Statis code execution from service util...");
	}

	public static String randomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( numeric.charAt( rnd.nextInt(numeric.length()) ) );
	   return sb.toString();
	}
	public static boolean isEmptyString(String msg){
		if(msg == null || msg.isEmpty()){
			return true;
		}
		return false;
	}
	
	
	
	public static boolean sendEmail(String toAddress, String subject, String messageBody, String name) {
		
		try{
		    InputStream propertiesInputStream = null;
	        Properties properties = new Properties();
	        propertiesInputStream = DatabaseUtil.class.getClassLoader().getResourceAsStream("../system.properties");
	        properties.load(propertiesInputStream);
			final String emailAccount = properties.getProperty("gmail_account");
			final String emailUser =properties.getProperty("gmail_user");
			final String emailPassword = properties.getProperty("gmail_password");
			
			
			
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
							//return new PasswordAuthentication(emailUser, emailPassword);
							return new PasswordAuthentication("ndspeechrepo", "mcomlab2017");

						}
					});

			try {

				Message message = new MimeMessage(session);
				//message.setFrom(new InternetAddress(emailAccount));
				message.setFrom(new InternetAddress("ndspeechrepo@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(toAddress));
				message.setSubject(subject);
				if(ServiceUtil.isEmptyString(name.trim())){
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
	
	public static void main(String[] args) {
		String json = "[{\"taskId\":3,\"answer\":\"\",\"surveyId\":13,\"submissionTime\":\"1521575818.45475\",\"studyId\":1,"
				+ "\"answerType\":\"reference\",\"submissionTimeZone\":\"\",\"version\":0},{\"taskId\":2,\"answer\":\"yes\",\"surveyId\":"
				+ "13,\"submissionTime\":\"1521575818.45315\",\"studyId\":1,\"answerType\":\"value\","
				+ "\"submissionTimeZone\":\"\",\"version\":0}]";

		ClientConfig config = new DefaultClientConfig();
	    Client client = Client.create(config);
		String url = "http://129.74.247.110/cimoninterface/study/survey/response";
	    WebResource service = client.resource(url);
		ClientResponse clientResponse = service.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);
		System.out.println("response:"+ clientResponse);
		String jsonResponse = clientResponse.getEntity(String.class);
		System.out.println("json response:"+ jsonResponse);

	}


}
