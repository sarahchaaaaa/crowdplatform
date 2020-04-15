package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.AutoNotInfo;
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
	AutoNotInfo getAutoNotSetting(long studyId);
	Response updateAutoNotInfoSetting(AutoNotInfo info);

}
