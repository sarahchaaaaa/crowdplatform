package mlab.mcsweb.client.study.settings;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Radio;
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

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.services.SettingsService;
import mlab.mcsweb.client.services.SettingsServiceAsync;
import mlab.mcsweb.shared.AutoNotInfo;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;


public class AutoNotification extends Composite {
	
	@UiField
	Button updateButton;
	
	@UiField
	Radio yesRadio, noRadio;
	
	@UiField
	Label daysErrorLabel, repeatErrorLabel, MsgErrorLabel, globalErrorLabel;
	
	@UiField
	TextBox notMsgText, daysText; 
	
	private Study study;
			
	private final SettingsServiceAsync service = GWT.create(SettingsService.class);

	private static AutoNotificationUiBinder uiBinder = GWT.create(AutoNotificationUiBinder.class);

	interface AutoNotificationUiBinder extends UiBinder<Widget, AutoNotification> {
	}

	public AutoNotification(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		globalErrorLabel.setText("");
		daysErrorLabel.setText("");
		repeatErrorLabel.setText("");
		MsgErrorLabel.setText("");		
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		
		service.getAutoNotSetting(this.study.getId(), new AsyncCallback<AutoNotInfo>() {

			@Override
			public void onSuccess(AutoNotInfo result) {
				// TODO Auto-generated method stub
				
				//set freq text
				daysText.setText(Integer.toString(result.getDays()));
				
				//set repeat radio
				if(result.isRepeat()) {
				  yesRadio.setValue(true);
				}
				else if(!result.isRepeat()) {
					noRadio.setValue(true);
				}
				else {
					repeatErrorLabel.setText("Error: invalid Repeat Setting");
				}
				
				//set message
				notMsgText.setText(result.getMessage());
				
			}
			

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				globalErrorLabel.setText("Error: Unable to load onLoad");
				
			}
		});

	}  //end of onLoad
	
	@UiHandler("updateButton")
	void updateDataUploadSetting(ClickEvent event){
		//check which radio button is selected
		//validate additional parameter, i.e., url is empty
		//call service.updateCloudSetting()
		//if response is success, set global error to empty
		//if response is failure, set global error - "Service is not available, please try later!"
		
		boolean valid = true;
		boolean repeat = false;
		int days = 0;
		String message = "";
		AutoNotInfo info = null; //initialize
		
		//Notification Frequency
		try {
			days = Integer.parseInt(daysText.getText());
		}
		catch(Exception e){
			valid = false;
			daysErrorLabel.setText("Error: must enter an integer");
		}
		finally {}		

		//Battery Radio
		if (yesRadio.getValue()) {
			repeat = true;			
		}	
		else if (noRadio.getValue()) {
			repeat = false;
		}
		else {
			valid = false;
			repeatErrorLabel.setText("Error: invalid Repeat Setting");
		}
		
		
		//Message
		message = notMsgText.getText();
		
		
		if (valid) {
			info = new AutoNotInfo(study.getId(), "", JSUtil.getUnixtime(),JSUtil.getTimezoneOffset() , days, repeat, message);
					
			service.updateAutoNotInfoSetting(info,  new AsyncCallback<Response>() {
	
				@Override
				public void onSuccess(Response result) {
					globalErrorLabel.setText("");
					daysErrorLabel.setText("");
					MsgErrorLabel.setText("");
					repeatErrorLabel.setText("");
				}
	
				@Override
				public void onFailure(Throwable caught) {
					globalErrorLabel.setText("Service is not available, please try later!");
					
				}
			});	
				
		} //end if (valid) 
		
		else {
			globalErrorLabel.setText("Error: Invalid Auto Notification Setting Entry");
			
		}
	
	} //end of update


	
	
}
