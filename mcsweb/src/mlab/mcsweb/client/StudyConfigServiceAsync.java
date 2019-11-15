package mlab.mcsweb.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.Response;

/**
 * The async counterpart of <code>StudyConfigService</code>.
 */
public interface StudyConfigServiceAsync {

	//setting
	void addCollaborator(long studyId, String email, AsyncCallback<Response> callback);
}
