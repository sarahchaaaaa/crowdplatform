package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.LabelingInfo;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("labeling")
public interface LabelingService extends RemoteService {
	
	ArrayList<LabelingInfo> getLabelingHistory(long studyId, String email, String uuid);
	
}
