package mlab.mcsweb.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

//@XmlRootElement
public class DaywiseCount implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String date = "";
	private int count;
	public DaywiseCount() {
		// TODO Auto-generated constructor stub
	}
	
	public DaywiseCount(String date, int count) {
		super();
		this.date = date;
		this.count = count;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDate() {
		return date;
	}
	public int getCount() {
		return count;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setCount(int count) {
		this.count = count;
	}
	

	
	
	
}