package commons;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response {
	int code;
	String message;
	
	public Response() {
		// TODO Auto-generated constructor stub
		this.code = -1;
		this.message = "";
	}
	public Response(int code, String message) {
		// TODO Auto-generated constructor stub
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
}
