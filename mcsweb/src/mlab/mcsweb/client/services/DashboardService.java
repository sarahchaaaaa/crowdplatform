package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.DaywiseCount;
import mlab.mcsweb.shared.FileIdentifier;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.Response;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("dashboard")
public interface DashboardService extends RemoteService {
	
	//summary
	
	Integer getTotalParticipant(long studyId);
	Integer getInstalledDevicesCount(long studyId);
	
	ArrayList<Participant> getParticipantsWithNoPing(long studyId, int days);
	ArrayList<DaywiseCount> getDailyPingCount(long studyId, int days);
	ArrayList<DaywiseCount> getDailyPingCountFromUniqueDevices(long studyId, int days);
	
	ArrayList<DaywiseCount> getDailyObjectCount(long studyId, int days);
	ArrayList<DaywiseCount> getDailyObjectCountFromUniqueDevices(long studyId, int days);
	ArrayList<Participant> getParticipantsWithNoData(long studyId, int days);
	
	ArrayList<DaywiseCount> getDailyLabelCount(long studyId, int days);
	ArrayList<DaywiseCount> getDailyLabelCountFromUniqueDevices(long studyId, int days);
	ArrayList<Participant> getParticipantsWithNoLabel(long studyId, int days);
	
	
	ArrayList<FileIdentifier> getFileIdentifiers(long studyId);
	
	
	//notification
	Response sendNotification(ArrayList<Participant> participants);
	
	
}
