package mlab.mcsweb.client;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;

public class StudyCreator extends Composite {
	
	@UiField
	Button newStudyButton, createButton, cancelButton;
	@UiField
	TextBox nameText;
	
	@UiField
	TextArea descriptionArea;
	
	@UiField
	HTMLPanel studyFormPanel;
	
	@UiField
	Label serviceErrorLabel;
	
	private String userId;
	private UserHomePage userHomePage;
	
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);


	private static StudyCreatorUiBinder uiBinder = GWT.create(StudyCreatorUiBinder.class);

	interface StudyCreatorUiBinder extends UiBinder<Widget, StudyCreator> {
	}

	public StudyCreator(String userId, UserHomePage userHomePage) {
		initWidget(uiBinder.createAndBindUi(this));
		this.userId = userId;
		this.userHomePage = userHomePage;
	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		resetNewStudyForm();
	}
	
	private void resetNewStudyForm(){
		nameText.setText("");
		descriptionArea.setText("");
		
		newStudyButton.setVisible(true);
		studyFormPanel.setVisible(false);
		serviceErrorLabel.setVisible(false);
	}
	
	@UiHandler("cancelButton")
	void cancelClicked(ClickEvent event){
		resetNewStudyForm();
	}
	
	private boolean validateStudyForm(){
		if(nameText.getText().trim().isEmpty()){
			serviceErrorLabel.setText("Name required");
			serviceErrorLabel.setVisible(true);
			return false;
		}
		return true;
	}
	
	
	@UiHandler("createButton")
	void createStudy(ClickEvent event){
		if(validateStudyForm()){
			final Study study = new Study();
			study.setName(nameText.getText().trim());
			study.setDescription(descriptionArea.getText().trim());
			study.setCreationTime(JSUtil.getUnixtime());
			study.setCreationTimeZone(JSUtil.getTimezoneOffset());
			study.setCreatedBy(this.userId);
			greetingService.createStudy(study, new AsyncCallback<Response>() {
				
				@Override
				public void onSuccess(Response result) {
					// TODO Auto-generated method stub
					if(result.getCode()==0){
						try {
							study.setId(Integer.parseInt(result.getMessage()));	
							userHomePage.addCreatedStudyToList(study);
						} catch (Exception e) {
							// TODO: handle exception
						}
						resetNewStudyForm();
					}else {
						serviceErrorLabel.setVisible(true);
						serviceErrorLabel.setText(result.getMessage());
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					serviceErrorLabel.setVisible(true);
					serviceErrorLabel.setText("Service not available, please try later.");
					
				}
			});
		}
				
	}
	
	@UiHandler("newStudyButton")
	void createStudyForm(ClickEvent event){
		nameText.setText("");
		descriptionArea.setText("");
		
		newStudyButton.setVisible(false);
		studyFormPanel.setVisible(true);
		serviceErrorLabel.setVisible(false);
	}

}
