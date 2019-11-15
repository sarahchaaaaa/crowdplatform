package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.User;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	//auth
	void signup(User user, AsyncCallback<Response> callback);
	void login(User user, AsyncCallback<Response> callback);
	
	//study
	void createStudy(Study study, AsyncCallback<Response> callback);
	void getStudyList(String email, AsyncCallback<ArrayList<Study>> callback);
	
}
