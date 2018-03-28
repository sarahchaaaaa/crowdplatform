package mlab.mcsweb.client.study;


import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.NavTabs;
import org.gwtbootstrap3.client.ui.TabContent;
import org.gwtbootstrap3.client.ui.TabListItem;
import org.gwtbootstrap3.client.ui.TabPane;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.events.StudyEvent;
import mlab.mcsweb.client.events.StudyState;
import mlab.mcsweb.client.events.StudyState.StudySpecificState;
import mlab.mcsweb.client.study.sensor.SensorEditor;
import mlab.mcsweb.shared.Study;

public class StudyDetails extends Composite {
	
	@UiField
	AnchorButton homeAnchor;
	@UiField
	Heading nameHeading;
	
	@UiField
	HTMLPanel detailsContentPanel;
	
	
	private NavTabs tabNavigation;
	private TabContent tabContent;
	
	private TabListItem dashboardTab, participantTab, phoneSensingTab, surveyTab, settingsTab;
	private TabPane dashboadPane, participantPane, phoneSensingPane, surveyPane, settingsPane;
	
	private SurveyManagement surveyManagement;
	private SensorEditor sensorEditor;
	
	private boolean isSurveyClicked = false, isSensingClicked = false;
	
	private Study study;
	private static StudyDetailsUiBinder uiBinder = GWT.create(StudyDetailsUiBinder.class);

	interface StudyDetailsUiBinder extends UiBinder<Widget, StudyDetails> {
	}

	public StudyDetails(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		
		
		nameHeading.setText(study.getName());
		
		tabNavigation = new NavTabs();
		tabContent = new TabContent();
		
		
		surveyManagement = new SurveyManagement(study);
		
		
		dashboardTab = new TabListItem("Dashboard");
		dashboadPane = new TabPane();
		dashboardTab.setDataTargetWidget(dashboadPane);
		dashboardTab.setActive(true);
		dashboardTab.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		participantTab = new TabListItem("Participants");
		participantPane = new TabPane();
		participantTab.setDataTargetWidget(participantPane);
		participantTab.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		phoneSensingTab = new TabListItem("Phone Sensor");
		phoneSensingPane = new TabPane();
		phoneSensingTab.setDataTargetWidget(phoneSensingPane);
		phoneSensingTab.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(!isSensingClicked){
					isSensingClicked = true;
					phoneSensingPane.clear();
					phoneSensingPane.add(new SensorEditor());
				}
			}
		});
		
		
		surveyTab = new TabListItem("Survey / Task");
		surveyPane = new TabPane();
		surveyTab.setDataTargetWidget(surveyPane);
		surveyTab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(!isSurveyClicked){
					isSurveyClicked = true;
					surveyPane.add(surveyManagement);
				}
			}
		});
		
		
		settingsTab = new TabListItem("Settings");
		settingsPane = new TabPane();
		settingsTab.setDataTargetWidget(settingsPane);
		settingsTab.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		tabNavigation.add(dashboardTab);
		tabContent.add(dashboadPane);
		tabNavigation.add(participantTab);
		tabContent.add(dashboadPane);
		tabNavigation.add(phoneSensingTab);
		tabContent.add(phoneSensingPane);
		tabNavigation.add(surveyTab);
		tabContent.add(surveyPane);
		tabNavigation.add(settingsTab);
		tabContent.add(settingsPane);
		
		
		detailsContentPanel.add(tabNavigation);
		detailsContentPanel.add(tabContent);
				
		
		//tabContent.add(new Settings());
		homeAnchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Mcsweb.getEventBus().fireEvent(new StudyEvent(new StudyState(null, StudySpecificState.HOME)));
			}
		});
		
	}


}
