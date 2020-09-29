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

import mlab.mcsweb.client.study.dashboard.EmailUUIDMappingList;
import mlab.mcsweb.client.study.dashboard.SendCommand;
import mlab.mcsweb.client.study.dashboard.SendNotification;
import mlab.mcsweb.client.study.dashboard.StudySummary;
import mlab.mcsweb.shared.Study;

public class Dashboard extends Composite {

	@UiField
	HTMLPanel contentPanel;
	
	@UiField
	LinkedGroupItem summaryLink, fileMapLink, sendNotificationLink, sendCommandLink;

	private Study study;
	private StudySummary studySummary;
	private EmailUUIDMappingList mappingList;
	private SendNotification sendNotification;
	private SendCommand sendCommand;
	
	private static DashboardUiBinder uiBinder = GWT.create(DashboardUiBinder.class);

	interface DashboardUiBinder extends UiBinder<Widget, Dashboard> {
	}

	public Dashboard(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		getSummary(null);
	}

	
	@UiHandler("summaryLink")
	void getSummary(ClickEvent event){
		summaryLink.setActive(true);
		fileMapLink.setActive(false);
		sendNotificationLink.setActive(false);
		sendCommandLink.setActive(false);
		contentPanel.clear();
		if (studySummary == null) {
			studySummary = new StudySummary(this.study.getId());
		}
		contentPanel.add(studySummary);
	}
	
	
	@UiHandler("fileMapLink")
	void getFileMapping(ClickEvent event){
		fileMapLink.setActive(true);
		summaryLink.setActive(false);
		sendNotificationLink.setActive(false);
		sendCommandLink.setActive(false);
		contentPanel.clear();
		if (mappingList == null) {
			try {
				mappingList = new EmailUUIDMappingList(this.study.getId());				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		contentPanel.add(mappingList);
	}
	
	@UiHandler("sendNotificationLink")
	void sendNotification(ClickEvent event){
		summaryLink.setActive(false);
		fileMapLink.setActive(false);
		sendNotificationLink.setActive(true);
		sendCommandLink.setActive(false);
		contentPanel.clear();
		if (sendNotification == null) {
			sendNotification = new SendNotification(this.study.getId()); // TODO: changed here
		}
		contentPanel.add(sendNotification);
	}
	
	@UiHandler("sendCommandLink")
	void sendCommand(ClickEvent event){
		summaryLink.setActive(false);
		fileMapLink.setActive(false);
		sendNotificationLink.setActive(false);
		sendCommandLink.setActive(true);
		contentPanel.clear();
		if (sendCommand == null) {
			sendCommand = new SendCommand();
		}
		contentPanel.add(sendCommand);
	}
	
}
