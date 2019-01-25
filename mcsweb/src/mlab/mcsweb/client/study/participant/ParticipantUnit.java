package mlab.mcsweb.client.study.participant;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.shared.Participant;

public class ParticipantUnit extends Composite {
	
	@UiField
	TextBox textEmail, textFName, textLName, textOrg;
	
	@UiField
	Button buttonSubmit, buttonCancel;
	
	private boolean fromEdit = false;

	private static ParticipantUnitUiBinder uiBinder = GWT.create(ParticipantUnitUiBinder.class);

	interface ParticipantUnitUiBinder extends UiBinder<Widget, ParticipantUnit> {
	}

	public ParticipantUnit() {
		initWidget(uiBinder.createAndBindUi(this));
		fromEdit = false;
	}
	public ParticipantUnit(Participant participant) {
		initWidget(uiBinder.createAndBindUi(this));
		fromEdit = true;
		textEmail.setText(participant.getUserEmail());
		textFName.setText(participant.getFirstName());
		textLName.setText(participant.getLastName());
		textOrg.setText(participant.getOrganization());
	}
	
	@UiHandler("buttonCancel")
	void cancelAction(ClickEvent event){
		this.removeFromParent();
	}
}
