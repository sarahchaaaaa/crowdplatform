package mlab.mcsweb.client.study.participant;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.services.ParticipantService;
import mlab.mcsweb.client.services.ParticipantServiceAsync;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Util;

public class ParticipantUnit extends Composite {

	@UiField
	TextBox textEmail, textIdentifier, textFName, textLName/* , textOrg */;

	@UiField
	Button buttonSubmit, buttonCancel;

	@UiField
	Label errorLabel;

	private AllParticipants allParticipants;
	private long studyId;
	private String currentEmail;
	private String currentIdentifier;
	private boolean fromEdit = false;
	private final ParticipantServiceAsync service = GWT.create(ParticipantService.class);

	private static ParticipantUnitUiBinder uiBinder = GWT.create(ParticipantUnitUiBinder.class);

	interface ParticipantUnitUiBinder extends UiBinder<Widget, ParticipantUnit> {
	}

	public ParticipantUnit(AllParticipants allParticipants, long studyId) {
		initWidget(uiBinder.createAndBindUi(this));
		this.allParticipants = allParticipants;
		this.studyId = studyId;
		currentEmail = "";
		currentIdentifier = "";
		fromEdit = false;
		errorLabel.setText("");
	}

	public ParticipantUnit(AllParticipants allParticipants, Participant participant) {
		initWidget(uiBinder.createAndBindUi(this));
		this.allParticipants = allParticipants;
		fromEdit = true;
		studyId = participant.getStudyId();
		currentEmail = participant.getUserEmail();
		currentIdentifier = participant.getIdentifier();
		textEmail.setText(participant.getUserEmail());
		textIdentifier.setText(participant.getIdentifier());
		textFName.setText(participant.getFirstName());
		textLName.setText(participant.getLastName());
		// textOrg.setText(participant.getOrganization());
		errorLabel.setText("");
	}

	@UiHandler("buttonCancel")
	void cancelAction(ClickEvent event) {
		errorLabel.setText("");
		currentEmail = "";
		currentIdentifier = "";
		this.removeFromParent();
	}

	@UiHandler("buttonSubmit")
	void enroll(ClickEvent event) {
		errorLabel.setText("");
		Participant participant = new Participant();
		participant.setStudyId(studyId);
		String email = textEmail.getText().trim();
		String identifier = textIdentifier.getText().trim();
		String firstName = textFName.getText().trim();
		String lastName = textLName.getText().trim();
		participant.setUserEmail(email);
		participant.setIdentifier(identifier);
		participant.setFirstName(firstName);
		participant.setLastName(lastName);
		if (Util.isEmailFOrmatValid(email)) {

			if (fromEdit) {
				//update
				service.editParticipant(currentEmail, currentIdentifier, participant, new AsyncCallback<Response>() {
					@Override
					public void onSuccess(Response result) {
						// TODO Auto-generated method stub
						if (result.getCode() == 0) {
							allParticipants.reloadTable();
							removeFromParent();
						} else {
							errorLabel.setText("Service not available, please try later.");
						}
						
					}
					@Override
					public void onFailure(Throwable caught) {
						errorLabel.setText("Service not available, please try later.");
					}
				});
			} else {
				service.addParticipant(participant, new AsyncCallback<Response>() {

					@Override
					public void onSuccess(Response result) {
						// TODO Auto-generated method stub
						if (result.getCode() == 0) {
							allParticipants.reloadTable();
							removeFromParent();
						} else {
							errorLabel.setText("Service not available, please try later.");
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						errorLabel.setText("Service not available, please try later.");
					}
				});

			}

		} else {
			errorLabel.setText("Invalid Email!");
		}

	}
}
