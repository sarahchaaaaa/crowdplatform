package mlab.mcsweb.client.services;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.DaywiseCount;
import mlab.mcsweb.shared.FileObjectInfo;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.PingInfo;
import mlab.mcsweb.shared.Response;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("participant")
public interface ParticipantService extends RemoteService {

	ArrayList<Participant> getAllParticipants(long studyId);
	Response addParticipant(Participant participant);
	Response editParticipant(String currentEmail, String currentIdentifier, Participant detailsToUpdate);
	Response deleteParticipants(List<Participant> participants);
	
	ArrayList<DaywiseCount> getDaywisePingHistory(long studyId, String email, String uuid, int days);
	ArrayList<PingInfo> getPingHistory(long studyId, String email, String uuid, int days);

	ArrayList<FileObjectInfo> getObjectHistory(long studyId, String email, String uuid);
	
}
