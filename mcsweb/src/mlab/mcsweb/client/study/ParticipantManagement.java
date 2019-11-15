package mlab.mcsweb.client.study;

import org.gwtbootstrap3.client.ui.LinkedGroupItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.study.participant.AllParticipants;
import mlab.mcsweb.client.study.participant.FileObjectHistory;
import mlab.mcsweb.client.study.participant.IndividualParticipant;
import mlab.mcsweb.shared.Study;

public class ParticipantManagement extends Composite{

	@UiField
	HTMLPanel contentPanel;
	
	@UiField
	LinkedGroupItem allParticipantLink, participantLink, objectLink;

	
	private Study study;
	private AllParticipants allParticipants;
	private IndividualParticipant participant;
	private FileObjectHistory fileHistory;

	private static ParticipantManagementUiBinder uiBinder = GWT.create(ParticipantManagementUiBinder.class);

	interface ParticipantManagementUiBinder extends UiBinder<Widget, ParticipantManagement> {
	}

	public ParticipantManagement(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		allParticipant(null);
	}


	@UiHandler("allParticipantLink")
	void allParticipant(ClickEvent event){
		//Window.alert("load all");
		allParticipantLink.setActive(true);
		participantLink.setActive(false);
		objectLink.setActive(false);
		
		contentPanel.clear();
		if (allParticipants == null){
			try {
				allParticipants = new AllParticipants(this.study.getId());				
			} catch (Exception e) {
				// TODO: handle exception
				Window.alert("exception in all "+ e.getMessage());
			}
		}
		contentPanel.add(allParticipants);
	}
	
	@UiHandler("participantLink")
	void loadGroupParticipant(ClickEvent event){
		//Window.alert("group participation");
		allParticipantLink.setActive(false);
		participantLink.setActive(true);
		objectLink.setActive(false);
		
		contentPanel.clear();
		if(participant == null){
			participant = new IndividualParticipant(this.study.getId());
		}
		contentPanel.add(participant);
	}
	
	@UiHandler("objectLink")
	void loadObjectHistory(ClickEvent event){
		//Window.alert("group participation");
		allParticipantLink.setActive(false);
		participantLink.setActive(false);
		objectLink.setActive(true);
		
		contentPanel.clear();
		if (fileHistory == null) {
			fileHistory = new FileObjectHistory();
		}
		contentPanel.add(fileHistory);
	}
	
}
