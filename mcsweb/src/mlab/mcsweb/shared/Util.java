package mlab.mcsweb.shared;

import java.security.MessageDigest;


public class Util {
	
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
	



}
