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

import mlab.mcsweb.client.study.wearable.AuthorizeDevice;
import mlab.mcsweb.client.study.wearable.IndividualWearableStat;
import mlab.mcsweb.client.study.wearable.StudyWearableStat;
import mlab.mcsweb.shared.Study;

public class WearableManagement extends Composite {
	
	@UiField
	HTMLPanel contentPanel;
	
	@UiField
	LinkedGroupItem authorizeLink, individualLink, studyLink;
	
	private Study study;
	private AuthorizeDevice authorizeDevice;
	private IndividualWearableStat individualStat;
	private StudyWearableStat studyStat;

	private static WearableManagementUiBinder uiBinder = GWT.create(WearableManagementUiBinder.class);

	interface WearableManagementUiBinder extends UiBinder<Widget, WearableManagement> {
	}

	public WearableManagement() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("authorizeLink")
	void authorizeDevice(ClickEvent event){
		authorizeLink.setActive(true);
		individualLink.setActive(false);
		studyLink.setActive(false);
		contentPanel.clear();
		if (authorizeDevice == null) {
			authorizeDevice = new AuthorizeDevice();
		}
		contentPanel.add(authorizeDevice);
	}
	
	@UiHandler("individualLink")
	void openIndividualStat(ClickEvent event){
		authorizeLink.setActive(false);
		individualLink.setActive(true);
		studyLink.setActive(false);
		contentPanel.clear();
		if (individualStat == null) {
			individualStat = new IndividualWearableStat();
		}
		contentPanel.add(individualStat);
	}
	
	@UiHandler("studyLink")
	void openStudyStat(ClickEvent event){
		authorizeLink.setActive(false);
		individualLink.setActive(false);
		studyLink.setActive(true);		
		contentPanel.clear();
		if (studyStat == null) {
			studyStat = new StudyWearableStat();
		}
		contentPanel.add(studyStat);
	}

}
