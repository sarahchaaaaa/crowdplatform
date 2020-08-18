package mlab.mcsweb.client.services;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.DaywiseCount;
import mlab.mcsweb.shared.FileObjectInfo;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.PingInfo;
import mlab.mcsweb.shared.Response;

/**
 * The async counterpart of <code>ParticipantService</code>.
 */
public interface ParticipantServiceAsync {
	
	void getAllParticipants(long studyId, AsyncCallback<ArrayList<Participant>> callback);
	void addParticipant(Participant participant, AsyncCallback<Response> callback);
	void editParticipant(String currentEmail, String currentIdentifier, Participant detailsToUpdate, AsyncCallback<Response> callback);
	void deleteParticipants(List<Participant> participants, AsyncCallback<Response> callback);
	
	void getDaywisePingHistory(long studyId, String email, String uuid, int days, AsyncCallback<ArrayList<DaywiseCount>> callback);
	void getPingHistory(long studyId, String email, String uuid, int days, AsyncCallback<ArrayList<PingInfo>> callback);
	
	void getObjectHistory(long studyId, String email, String uuid, AsyncCallback<ArrayList<FileObjectInfo>> callback);
}
