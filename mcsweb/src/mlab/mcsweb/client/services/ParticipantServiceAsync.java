package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.PingInfo;
import mlab.mcsweb.shared.Response;

/**
 * The async counterpart of <code>ParticipantService</code>.
 */
public interface ParticipantServiceAsync {
	
	void getAllParticipants(long studyId, AsyncCallback<ArrayList<Participant>> callback);
	void addParticipant(Participant participant, AsyncCallback<Response> callback);
	void editParticipant(String currentEmail, Participant detailsToUpdate, AsyncCallback<Response> callback);
	void deleteParticipants(long studyId, String list, AsyncCallback<Response> callback);
	void getPingHistory(long studyId, String email, int days, AsyncCallback<ArrayList<PingInfo>> callback);
}
