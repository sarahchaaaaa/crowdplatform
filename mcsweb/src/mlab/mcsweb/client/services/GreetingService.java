package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.User;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	
	//auth
	Response signup(User user);
	Response login(User user);
	
	//study
	Response createStudy(Study study);
	ArrayList<Study> getStudyList(String email);
	
}
