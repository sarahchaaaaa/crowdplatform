package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.FileIdentifier;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("dashboard")
public interface DashboardService extends RemoteService {
	
	ArrayList<FileIdentifier> getFileIdentifiers(long studyId);
	
}
