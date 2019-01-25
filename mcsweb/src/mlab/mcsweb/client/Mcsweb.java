
package mlab.mcsweb.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mcsweb implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	
	final static SimpleEventBus eventBus = new SimpleEventBus();
	private static boolean loggedIn = false;
	private static String browser="";
	
	private GlobalHomePage globalHomePage = null;
//	private UserHomePage userHomePage = null;
	private MainPage mainPage = null;
	
	
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		loadGloablHome();
	}
	
	private void loadGloablHome(){
		RootPanel.get().clear();
//		String paramValue = Window.Location.getParameter("emailverified");
		globalHomePage = new GlobalHomePage(this);
		RootPanel.get().add(globalHomePage);

	}
	
	protected void loggedIn(String userId){
		loggedIn = true;
		mainPage = new MainPage(this, userId);
		RootPanel.get().clear();
		RootPanel.get().add(mainPage);		
		
	}
	
	protected boolean isLoggedIn() {
		return loggedIn;
	}
	
	public static EventBus getEventBus() {
		return eventBus;
	}
	
	protected void logout() {
		RootPanel.get().clear();
		loggedIn = false;
		//onModuleLoad();
		Window.Location.replace("https://koiosplatform.com/");
	}
	
	public native void getBrowser()/*-{
		var nVer = navigator.appVersion;
		var nAgt = navigator.userAgent;
		var browserName  = navigator.appName;
		var fullVersion  = ''+parseFloat(navigator.appVersion); 
		var majorVersion = parseInt(navigator.appVersion,10);
		var nameOffset,verOffset,ix;
		
		// In Opera 15+, the true version is after "OPR/" 
		if ((verOffset=nAgt.indexOf("OPR/"))!=-1) {
		 browserName = "Opera";
		 fullVersion = nAgt.substring(verOffset+4);
		}
		// In older Opera, the true version is after "Opera" or after "Version"
		else if ((verOffset=nAgt.indexOf("Opera"))!=-1) {
		 browserName = "Opera";
		 fullVersion = nAgt.substring(verOffset+6);
		 if ((verOffset=nAgt.indexOf("Version"))!=-1) 
		   fullVersion = nAgt.substring(verOffset+8);
		}
		// In MSIE, the true version is after "MSIE" in userAgent
		else if ((verOffset=nAgt.indexOf("Edge"))!=-1) {
		 browserName = "Microsoft Internet Explorer";
		 fullVersion = nAgt.substring(verOffset+5);
		}
		// In Chrome, the true version is after "Chrome" 
		else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
		 browserName = "Chrome";
		 fullVersion = nAgt.substring(verOffset+7);
		}
		// In Safari, the true version is after "Safari" or after "Version" 
		else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {
		 browserName = "Safari";
		 fullVersion = nAgt.substring(verOffset+7);
		 if ((verOffset=nAgt.indexOf("Version"))!=-1) 
		   fullVersion = nAgt.substring(verOffset+8);
		}
		// In Firefox, the true version is after "Firefox" 
		else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {
		 browserName = "Firefox";
		 fullVersion = nAgt.substring(verOffset+8);
		}
		// In most other browsers, "name/version" is at the end of userAgent 
		else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) < 
		          (verOffset=nAgt.lastIndexOf('/')) ) 
		{
		 browserName = nAgt.substring(nameOffset,verOffset);
		 fullVersion = nAgt.substring(verOffset+1);
		 if (browserName.toLowerCase()==browserName.toUpperCase()) {
		  browserName = navigator.appName;
		 }
		}
		// trim the fullVersion string at semicolon/space if present
		if ((ix=fullVersion.indexOf(";"))!=-1)
		   fullVersion=fullVersion.substring(0,ix);
		if ((ix=fullVersion.indexOf(" "))!=-1)
		   fullVersion=fullVersion.substring(0,ix);
		
		majorVersion = parseInt(''+fullVersion,10);
		if (isNaN(majorVersion)) {
		 fullVersion  = ''+parseFloat(navigator.appVersion); 
		 majorVersion = parseInt(navigator.appVersion,10);
		}
		@mlab.mcsweb.client.Mcsweb::browser=browserName;
	
}-*/;
	public static String getBrowserName(){
		return browser;
	}
	
}
