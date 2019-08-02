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

import mlab.mcsweb.client.study.participant.AllParticipants;
import mlab.mcsweb.client.study.participant.IndividualParticipant;
import mlab.mcsweb.client.study.settings.AutoNotification;
import mlab.mcsweb.client.study.settings.CloudStorage;
import mlab.mcsweb.client.study.settings.Collaborations;
import mlab.mcsweb.client.study.settings.MobileStorage;
import mlab.mcsweb.shared.Study;

public class Settings extends Composite {

	@UiField
	HTMLPanel contentPanel;
	
	@UiField
	LinkedGroupItem cloudStorageLink, mobileStorageLink, autoNotifLink, collaboratorsLink;

	
	private Study study;
	private CloudStorage cloudStorage;
	private MobileStorage mobileStorage;
	private AutoNotification autoNotification;
	private Collaborations collaborations;

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
		autoNotifLink.setActive(false);
		collaboratorsLink.setActive(false);
		contentPanel.clear();
		if (cloudStorage == null) {
			cloudStorage = new CloudStorage();
		}
		contentPanel.add(cloudStorage);
	}
	
	@UiHandler("mobileStorageLink")
	void setMobileStorage(ClickEvent event){
		cloudStorageLink.setActive(false);
		mobileStorageLink.setActive(true);
		autoNotifLink.setActive(false);	
		collaboratorsLink.setActive(false);
		contentPanel.clear();
		if (mobileStorage == null) {
			mobileStorage = new MobileStorage();
		}
		contentPanel.add(mobileStorage);
	}
	
	@UiHandler("autoNotifLink")
	void setAutoNotification(ClickEvent event){
		cloudStorageLink.setActive(false);
		mobileStorageLink.setActive(false);
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
		autoNotifLink.setActive(false);
		collaboratorsLink.setActive(true);
		contentPanel.clear();
		if (collaborations == null) {
			collaborations = new Collaborations();
		}
		contentPanel.add(collaborations);
		
	}
	
}
