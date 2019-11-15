package mlab.mcsweb.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.Response;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("study")
public interface StudyConfigService extends RemoteService {
	//settings
	Response addCollaborator(long studyId, String email);
}
