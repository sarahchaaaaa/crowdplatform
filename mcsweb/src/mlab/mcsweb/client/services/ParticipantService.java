package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
	Response editParticipant(String currentEmail, Participant detailsToUpdate);
	Response deleteParticipants(long studyId, String list);
	ArrayList<PingInfo> getPingHistory(long studyId, String email, int days);
	
	
}
