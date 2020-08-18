package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.AutoNotificationInfo;
import mlab.mcsweb.shared.CloudStorageInfo;
import mlab.mcsweb.shared.DataUploadInfo;
import mlab.mcsweb.shared.MobileStorageInfo;
import mlab.mcsweb.shared.Response;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("settings")
public interface SettingsService extends RemoteService {
	ArrayList<String> getAllCollaborators(long studyId);
	Response addCollaborator(long studyId, String collabEmail);
	Response editCollaborator(long studyId, String currentEmail, String newEmail);
	Response deleteCollaborators(long studyId, String collabEmail);
	
	//Cloud storage
	CloudStorageInfo getCloudSettingInfo(long studyId);
	Response updateCloudSetting(CloudStorageInfo info);
	
	//mobile storage
	MobileStorageInfo getMobileStorageSetting(long studyId);
	Response updateMobileStorageSetting(MobileStorageInfo info);
	
	//data upload
	DataUploadInfo getDataUploadSetting(long studyId);
	Response updateDataUploadSetting(DataUploadInfo info);
	
	//auto notification
	AutoNotificationInfo getAutoNotificationSetting(long studyId);
	Response updateAutoNotificationSetting(AutoNotificationInfo info);
	Response updateAutoNotificationState(long studyId, int active);

	ArrayList<String> getAllNotificationAdmin(long studyId);
	Response addNotificationAdmin(long studyId, String adminEmail);
	Response editNotificationAdmin(long studyId, String currentEmail, String newEmail);
	Response deleteNotificationAdmins(long studyId, String adminEmails);

	
}
