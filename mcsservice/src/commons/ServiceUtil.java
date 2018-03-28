package commons;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ServiceUtil {
	
	static final String alphanumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();
	
	static{
		System.out.println("Statis code execution from service util...");
	}

	public static String randomString( int len ){
		   StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( alphanumeric.charAt( rnd.nextInt(alphanumeric.length()) ) );
		   return sb.toString();
		}
	public static boolean isEmptyString(String msg){
		if(msg == null || msg.isEmpty()){
			return true;
		}
		return false;
	}
	
	
	public static void main(String[] args) {
		String str = ServiceUtil.randomString(5);
		System.out.println(str);
	}
	
	public static String getEmailVerificationRoot(){
		return "http://129.74.247.110/mcsweb/";
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


}
