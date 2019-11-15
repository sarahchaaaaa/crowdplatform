package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.CloudStorageInfo;
import mlab.mcsweb.shared.MobileStorageInfo;
import mlab.mcsweb.shared.Response;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SettingsServiceAsync{
	
	//collaborator
	void getAllCollaborators(long studyId, AsyncCallback<ArrayList<String>> callback);
	void addCollaborator(long studyId, String collabEmail, AsyncCallback<Response> callback);
	void editCollaborator(long studyId, String currentEmail, String newEmail, AsyncCallback<Response> callback);
	void deleteCollaborators(long studyId, String collabEmail, AsyncCallback<Response> callback);
	
	//cloud storage
	void getCloudSettingInfo(long studyId, AsyncCallback<CloudStorageInfo> callback);
	void updateCloudSetting(CloudStorageInfo info, AsyncCallback<Response> callback);
	
	//mobile storage
	void getMobileStorageSetting(long studyId, AsyncCallback<MobileStorageInfo> callback);
	void updateMobileStorageSetting(MobileStorageInfo info, AsyncCallback<Response> callback);
}
