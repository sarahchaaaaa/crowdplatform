package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.LabelingInfo;
import mlab.mcsweb.shared.PingInfo;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface LabelingServiceAsync {
	
	void getLabelingHistory(long studyId, String email, String uuid, AsyncCallback<ArrayList<LabelingInfo>> callback);
	
}
