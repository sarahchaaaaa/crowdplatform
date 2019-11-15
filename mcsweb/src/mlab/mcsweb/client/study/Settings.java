package mlab.mcsweb.client.study;

import org.gwtbootstrap3.client.ui.LinkedGroupItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.study.settings.AutoNotification;
import mlab.mcsweb.client.study.settings.CloudStorage;
import mlab.mcsweb.client.study.settings.Collaborators;
import mlab.mcsweb.client.study.settings.DataUpload;
import mlab.mcsweb.client.study.settings.MobileStorage;
import mlab.mcsweb.shared.Study;

public class Settings extends Composite {

	@UiField
	HTMLPanel contentPanel;
	
	@UiField
	LinkedGroupItem cloudStorageLink, mobileStorageLink, uploadLink, autoNotifLink, collaboratorsLink;

	
	private Study study;
	private CloudStorage cloudStorage;
	private MobileStorage mobileStorage;
	private DataUpload dataUpload;
	private AutoNotification autoNotification;
	private Collaborators collaborators;

	private static SettingsUiBinder uiBinder = GWT.create(SettingsUiBinder.class);

	interface SettingsUiBinder extends UiBinder<Widget, Settings> {
	}

	public Settings(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		setCloudStorage(null);
	}

	@UiHandler("cloudStorageLink")
	void setCloudStorage(ClickEvent event){
		cloudStorageLink.setActive(true);
		mobileStorageLink.setActive(false);
		uploadLink.setActive(false);
		autoNotifLink.setActive(false);
		collaboratorsLink.setActive(false);
		contentPanel.clear();
		if (cloudStorage == null) {
			cloudStorage = new CloudStorage(study);
		}
		contentPanel.add(cloudStorage);
	}
	
	@UiHandler("mobileStorageLink")
	void setMobileStorage(ClickEvent event){
		cloudStorageLink.setActive(false);
		mobileStorageLink.setActive(true);
		uploadLink.setActive(false);
		autoNotifLink.setActive(false);	
		collaboratorsLink.setActive(false);
		contentPanel.clear();
		if (mobileStorage == null) {
			mobileStorage = new MobileStorage(study);
		}
		contentPanel.add(mobileStorage);
	}

	@UiHandler("uploadLink")
	void setUploadStrategy(ClickEvent event){
		cloudStorageLink.setActive(false);
		mobileStorageLink.setActive(false);
		uploadLink.setActive(true);
		autoNotifLink.setActive(false);	
		collaboratorsLink.setActive(false);
		contentPanel.clear();
		if (dataUpload == null) {
			dataUpload = new DataUpload();
		}
		contentPanel.add(dataUpload);
	}
	
	@UiHandler("autoNotifLink")
	void setAutoNotification(ClickEvent event){
		cloudStorageLink.setActive(false);
		mobileStorageLink.setActive(false);
		uploadLink.setActive(false);
		autoNotifLink.setActive(true);
		collaboratorsLink.setActive(false);
		contentPanel.clear();
		if (autoNotification == null) {
			autoNotification = new AutoNotification();
		}
		contentPanel.add(autoNotification);
	}
	
	@UiHandler("collaboratorsLink")
	void setCollaborations(ClickEvent event){
		cloudStorageLink.setActive(false);
		mobileStorageLink.setActive(false);
		uploadLink.setActive(false);
		autoNotifLink.setActive(false);
		collaboratorsLink.setActive(true);
		contentPanel.clear();
		if (collaborators == null) {
			collaborators = new Collaborators(study);
		}
		contentPanel.add(collaborators);
		
	}
	
}
