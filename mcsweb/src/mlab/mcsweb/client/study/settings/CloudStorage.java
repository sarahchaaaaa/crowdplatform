package mlab.mcsweb.client.study.settings;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Radio;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

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
import mlab.mcsweb.shared.CloudStorageInfo;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.Util;

public class CloudStorage extends Composite {

	
	@UiField
	Radio s3Radio, urlRadio, defaultRadio;
	
	@UiField
	Button updateButton;
	
	@UiField
	Label globalErrorLabel, s3ErrorLabel, urlErrorLabel;
	
	@UiField
	TextBox textAccessKey, textSecretAccess, textBucketPath, textUrlPath; 
	
	private Study study;
	
	private final SettingsServiceAsync service = GWT.create(SettingsService.class);

	private static CloudStorageUiBinder uiBinder = GWT.create(CloudStorageUiBinder.class);

	interface CloudStorageUiBinder extends UiBinder<Widget, CloudStorage> {
	}

	public CloudStorage(Study study) {	
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		globalErrorLabel.setText("");
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
				
		service.getCloudSettingInfo(this.study.getId(), new AsyncCallback<CloudStorageInfo>() {
			
			@Override
			public void onSuccess(CloudStorageInfo result) {
				String params = "";
								
				//select radio button based on result.gettype()
				if (result.getType() == "s3") {
										
					s3Radio.setValue(true);
					params = result.getParameter();
		            //split and set variables / check for edge cases
					String[] data = params.split("\\|");
					if (data.length != 3) { //incorrect number of entries
						s3ErrorLabel.setText("Error: Invalid Amazon S3 Entry");
						onFailure(null);
					}					
					
					String key = data[0];
					String secret = data[1];
					String bucket = data[2];
					textAccessKey.setText(key);
					textSecretAccess.setText(secret);
					textBucketPath.setText(bucket);
					
					
					
				}
				else if (result.getType() == "url") {
										
					urlRadio.setValue(true);
					params = result.getParameter();
					if(Util.isURLFormatValid(params)) {
						textUrlPath.setText(params);
					}
					else {
						urlErrorLabel.setText("Error: Invalid URL");
					}
					
				}
				else if (result.getType() == "default") {
					defaultRadio.setValue(true);
					
					
				}
				else {
					globalErrorLabel.setText("Error: Invalid type");
					onFailure(null);					 
				}
			
				
				//populate textfield(s) if necessary by parsing result.getParameter()
				// params: S3 : "key|access|path"
				//		   URL: "url"
				//		   Default: none
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//set text to global error label
				globalErrorLabel.setText("Error: Unable to load onLoad");
			}
		});
	}
	
	@UiHandler("updateButton")
	void updateStorageSetting(ClickEvent event){
		//check which radio button is selected
		//validate additional parameter, i.e., url is empty
		//call service.updateCloudSetting()
		//if response is success, set global error to empty
		//if response is failure, set global error - "Service is not available, please try later!"
		
		boolean valid = false;
		String params = "";
		String type = "";
		CloudStorageInfo info = null; //initialize
		
		
		if (s3Radio.getValue()) {
			
			
			String key = textAccessKey.getText();
			String secret = textSecretAccess.getText();
			String bucket = textBucketPath.getText();
			
			if (!key.isEmpty() && !secret.isEmpty() && !bucket.isEmpty()) {
				params = key + "|" + secret + "|" + bucket;
				type = "s3";
				valid = true;
			}			
			else
				s3ErrorLabel.setText("Error: Must fill in all fields");
			
		}
		else if (urlRadio.getValue()) {
			
			String url = textUrlPath.getText();
			
			if (Util.isURLFormatValid(url)) {
				params = url;
				type = "url";
				valid = true;
			}
			else
				urlErrorLabel.setText("Error: Invalid URL");
				
		}
		else if (defaultRadio.getValue()) {
			type = "default";
			valid = true;
			
		}
		else {
			Notify.notify("Select a storage setting to edit", NotifyType.WARNING);
			//fail
			
		}
		

		
		if (valid) {
		info = new CloudStorageInfo(study.getId(), "", JSUtil.getUnixtime(),JSUtil.getTimezoneOffset() , type, params);
				
		service.updateCloudSetting(info,  new AsyncCallback<Response>() {

			@Override
			public void onSuccess(Response result) {
				globalErrorLabel.setText("");
				s3ErrorLabel.setText("");
				urlErrorLabel.setText("");
				
			}

			@Override
			public void onFailure(Throwable caught) {
				globalErrorLabel.setText("Service is not available, please try later!");
				
			}

		});					
				
		} //end if (valid) 
		
		else {
			globalErrorLabel.setText("Error: Invalid Storage Setting Entry");
			
		}
	
		
	}
	
}
