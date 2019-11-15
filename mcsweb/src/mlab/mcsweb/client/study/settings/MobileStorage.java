package mlab.mcsweb.client.study.settings;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.services.SettingsService;
import mlab.mcsweb.client.services.SettingsServiceAsync;
import mlab.mcsweb.shared.MobileStorageInfo;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;

public class MobileStorage extends Composite {
	
	@UiField
	Button buttonSubmit, buttonCancel;
	
	@UiField
	Label errorLabel;
	
	@UiField
	TextBox textFileSize, textNumFiles, textMaxCap; 
	
	private Study study;
			
	private final SettingsServiceAsync service = GWT.create(SettingsService.class);

	private static MobileStorageUiBinder uiBinder = GWT.create(MobileStorageUiBinder.class);

	interface MobileStorageUiBinder extends UiBinder<Widget, MobileStorage> {
	}

	public MobileStorage(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		errorLabel.setText("");
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		
		service.getMobileStorageSetting(this.study.getId(), new AsyncCallback<MobileStorageInfo>() {

			@Override
			public void onSuccess(MobileStorageInfo result) {
				// TODO Auto-generated method stub
				textFileSize.setText(Integer.toString(result.getFileObjectSize()));
				textNumFiles.setText(Integer.toString(result.getMaxFiles()));
				textMaxCap.setText(Integer.toString(result.getMaxCapacity()));
				
			}
			

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				errorLabel.setText("Error: Unable to load onLoad");
				
			}
		});

	}
	
	@UiHandler("buttonSubmit")
	void submitMobileStorageSetting(ClickEvent event){
		boolean valid = false;
		MobileStorageInfo info = null;
		int fileObjSize = 0;
		int numFiles = 0;
		int maxCap = 0;
		
		try {
			fileObjSize = Integer.parseInt(textFileSize.getText());
			numFiles = Integer.parseInt(textNumFiles.getText());
			maxCap = Integer.parseInt(textMaxCap.getText());
			
			Window.alert(fileObjSize + "|" + numFiles+ "|" + maxCap );
			
			if (fileObjSize >= 0 && numFiles >= 0 && maxCap >= 0)
				valid = true;
		}
		catch (Exception ex) {
			errorLabel.setText("Error: Enter a positive integer for all fields");
		
		}
		
		if (valid) {
		
			info = new MobileStorageInfo(study.getId(), "", JSUtil.getUnixtime(),JSUtil.getTimezoneOffset() , fileObjSize, numFiles, maxCap);
			
			service.updateMobileStorageSetting(info,  new AsyncCallback<Response>() {
	
				@Override
				public void onSuccess(Response result) {
					errorLabel.setText("");
				}
	
				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText("Service is not available, please try later!");
					
				}
	
			});					
		}
	}
	
	
	@UiHandler("buttonCancel")
	void cancelMobileStorageSetting(ClickEvent event){
		textFileSize.clear();
		textNumFiles.clear();
		textMaxCap.clear();
	}
}
