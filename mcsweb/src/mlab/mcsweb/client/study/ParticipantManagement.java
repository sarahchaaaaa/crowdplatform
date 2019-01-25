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
import mlab.mcsweb.client.study.participant.GroupParticipants;
import mlab.mcsweb.shared.Study;

public class ParticipantManagement extends Composite{

	@UiField
	HTMLPanel contentPanel;
	
	@UiField
	LinkedGroupItem allParticipantLink, groupParticipantLink;

	
	private Study study;
	private AllParticipants allParticipants;
	private GroupParticipants groupParticipants;

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
		groupParticipantLink.setActive(false);
		
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
	
	@UiHandler("groupParticipantLink")
	void loadGroupParticipant(ClickEvent event){
		//Window.alert("group participation");
		allParticipantLink.setActive(false);
		groupParticipantLink.setActive(true);
		
		contentPanel.clear();
		if(groupParticipants == null){
			groupParticipants = new GroupParticipants();
		}
		contentPanel.add(groupParticipants);
	}
}
