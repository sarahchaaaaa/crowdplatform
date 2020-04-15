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
import mlab.mcsweb.shared.DataUploadInfo;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;

public class DataUpload extends Composite {
	
	@UiField
	Button updateButton;
	
	@UiField
	Radio wifiRadio, LTERadio, anyNetworkRadio, plugRadio, anyBatteryRadio;
	
	@UiField
	Label FreqErrorLabel, BatteryErrorLabel, NetworkErrorLabel, globalErrorLabel;
	
	@UiField
	TextBox uploadFreq; 
	
	private Study study;
			
	private final SettingsServiceAsync service = GWT.create(SettingsService.class);

	private static DataUploadUiBinder uiBinder = GWT.create(DataUploadUiBinder.class);

	interface DataUploadUiBinder extends UiBinder<Widget, DataUpload> {
	}

	public DataUpload(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		globalErrorLabel.setText("");
		FreqErrorLabel.setText("");
		BatteryErrorLabel.setText("");
		NetworkErrorLabel.setText("");
	}


	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		
		service.getDataUploadSetting(this.study.getId(), new AsyncCallback<DataUploadInfo>() {

			@Override
			public void onSuccess(DataUploadInfo result) {
				// TODO Auto-generated method stub
				
				//set network radio
				switch(result.getNetwork()) {
				  case "wifi":
					  wifiRadio.setValue(true);
					  break;
				  case "LTE":
					  LTERadio.setValue(true);
					  break;
				  case "any":
					  anyNetworkRadio.setValue(true);
					  break;
				  default:
					  NetworkErrorLabel.setText("Error: invalid Network Setting");
				}
				
				//set Battery radio
				switch(result.getBattery()) {
				  case "plug":
					  plugRadio.setValue(true);
					  break;
				  case "any":
					  anyBatteryRadio.setValue(true);
					  break;
				  default:
					  BatteryErrorLabel.setText("Error: invalid Battery Setting");
				}
				
				uploadFreq.setText(Integer.toString(result.getFrequency()));
				
				
				
			}
			

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				globalErrorLabel.setText("Error: Unable to load onLoad");
				
			}
		});

	}
	
	@UiHandler("updateButton")
	void updateDataUploadSetting(ClickEvent event){
		//check which radio button is selected
		//validate additional parameter, i.e., url is empty
		//call service.updateCloudSetting()
		//if response is success, set global error to empty
		//if response is failure, set global error - "Service is not available, please try later!"
		
		boolean valid = true;
		String battery = "";
		String network = "";
		int freq = 0;
		DataUploadInfo info = null; //initialize
		
		//wifiRadio, LTERadio, anyNetworkRadio, plugRadio, anyBatteryRadio;
		//Network Radio
		if (wifiRadio.getValue()) {
			network = "wifi";			
		}	
		else if (LTERadio.getValue()) {
			network = "LTE";			
		}
		else if (anyNetworkRadio.getValue()) {
			network = "any";
		}
		else {
			valid = false;
			NetworkErrorLabel.setText("Error: invalid Network Setting");
		}
			

		//Battery Radio
		if (plugRadio.getValue()) {
			battery = "plug";			
		}	
		else if (anyBatteryRadio.getValue()) {
			battery = "any";
		}
		else {
			valid = false;
			NetworkErrorLabel.setText("Error: invalid Battery Setting");
		}
		
		
		//Frequency
		try {
			freq = Integer.parseInt(uploadFreq.getText());
		}
		catch(Exception e){
			valid = false;
			FreqErrorLabel.setText("Error: must enter an integer");
		}
		finally {}		
		
		if (valid) {
			info = new DataUploadInfo(study.getId(), "", JSUtil.getUnixtime(),JSUtil.getTimezoneOffset() , network, battery, freq);
					
			service.updateDataUploadSetting(info,  new AsyncCallback<Response>() {
	
				@Override
				public void onSuccess(Response result) {
					globalErrorLabel.setText("");
					NetworkErrorLabel.setText("");
					BatteryErrorLabel.setText("");
					FreqErrorLabel.setText("");
					globalErrorLabel.setText("");
					
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
	
		
	} //end of update
	
	
	
	
	
}
