package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.FileIdentifier;

/**
 * The async counterpart of <code>DashboardService</code>.
 */
public interface DashboardServiceAsync {
	
	void getFileIdentifiers(long studyId, AsyncCallback<ArrayList<FileIdentifier>> callback);
		
}
