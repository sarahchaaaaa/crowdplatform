package mlab.mcsweb.client.study.settings;


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

import mlab.mcsweb.client.services.SettingsService;
import mlab.mcsweb.client.services.SettingsServiceAsync;
//import mlab.mcsweb.client.study.settings.Collaborators;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.Util;


public class CollaboratorUnit extends Composite{

	private final SettingsServiceAsync service = GWT.create(SettingsService.class);

	private static CollaboratorUnitUiBinder uiBinder = GWT.create(CollaboratorUnitUiBinder.class);

	interface CollaboratorUnitUiBinder extends UiBinder<Widget, CollaboratorUnit> {
	}
	
	
	@UiField
	TextBox textEmail;
	
	@UiField
	Button buttonSubmit, buttonCancel;

	@UiField
	Label errorLabel;
	
	private Study study;
	private String currentEmail;
	private boolean fromEdit = false;
	private Collaborators collaborators;
	
	
	
	public CollaboratorUnit(Collaborators collaborators, Study study){
		initWidget(uiBinder.createAndBindUi(this));  
		this.study = study;
		this.collaborators = collaborators;
		currentEmail = "";
		fromEdit = false;
		errorLabel.setText("");
		
		
	}
	
	public CollaboratorUnit(Collaborators collaborators, Study study, String email){
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		this.collaborators = collaborators;
		currentEmail = email;
		fromEdit = true;
		errorLabel.setText("");	
		
		textEmail.setText(email);
	}	

	
	@UiHandler("buttonCancel")
	void cancelAction(ClickEvent event) {
		errorLabel.setText("");
		currentEmail = "";
		this.removeFromParent();
	}
	
	@UiHandler("buttonSubmit")
	void enroll(ClickEvent event) {
	
		errorLabel.setText("");
		String email = textEmail.getText().trim();
		if (Util.isEmailFOrmatValid(email)) {

			if (fromEdit) {
				//update

				service.editCollaborator(study.getId(), currentEmail, email, new AsyncCallback<Response>() {
				
					@Override
					public void onSuccess(Response result) {
						// TODO Auto-generated method stub
						if (result.getCode() == 0) {
							collaborators.reloadTable();
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
				service.addCollaborator(study.getId(), email, new AsyncCallback<Response>() {

					@Override
					public void onSuccess(Response result) {
						// TODO Auto-generated method stub
						if (result.getCode() == 0) {
							collaborators.reloadTable();
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
				
				errorLabel.setText("Email submitted.");

			} 

		} else {
			errorLabel.setText("Invalid Email!");
		}

	}
	
	
	
}

	
	
	
	