package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.DaywiseCount;
import mlab.mcsweb.shared.FileIdentifier;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.Response;

/**
 * The async counterpart of <code>DashboardService</code>.
 */
public interface DashboardServiceAsync {
	
	//summary
	void getTotalParticipant(long studyId, AsyncCallback<Integer> callback);
	void getInstalledDevicesCount(long studyId, AsyncCallback<Integer> callback);
	
	void getParticipantsWithNoPing(long studyId, int days, AsyncCallback<ArrayList<Participant>> callback);
	void getDailyPingCount(long studyId, int days, AsyncCallback<ArrayList<DaywiseCount>> callback);
	void getDailyPingCountFromUniqueDevices(long studyId, int days, AsyncCallback<ArrayList<DaywiseCount>> callback);

	
	void getDailyObjectCount(long studyId, int days, AsyncCallback<ArrayList<DaywiseCount>> callback);
	void getDailyObjectCountFromUniqueDevices(long studyId, int days, AsyncCallback<ArrayList<DaywiseCount>> callback);
	void getParticipantsWithNoData(long studyId, int days, AsyncCallback<ArrayList<Participant>> callback);
	
	void getDailyLabelCount(long studyId, int days, AsyncCallback<ArrayList<DaywiseCount>> callback);
	void getDailyLabelCountFromUniqueDevices(long studyId, int days, AsyncCallback<ArrayList<DaywiseCount>> callback);
	void getParticipantsWithNoLabel(long studyId, int days, AsyncCallback<ArrayList<Participant>> callback);

	
	void getFileIdentifiers(long studyId, AsyncCallback<ArrayList<FileIdentifier>> callback);
	
	//notification
	void sendNotification(ArrayList<Participant> participants, AsyncCallback<Response> callback);
		
}
