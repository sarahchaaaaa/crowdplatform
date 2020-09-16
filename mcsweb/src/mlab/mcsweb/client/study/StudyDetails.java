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

	private TabListItem dashboardTab, participantTab, sensingTab, surveyTab, taskTab, labelingTab, wearableTab, settingsTab;
	private TabPane dashboardPane, participantPane, sensingPane, surveyPane, taskPane, labelingPane, wearablePane, settingsPane;

	private Dashboard dashboard;
	private ParticipantManagement participantManagement;
	private SensorManagement sensorManagement;
	private SurveyManagement surveyManagement;
	private LabelingManagement labelManagement;
	private WearableManagement wearableManagement;
	private Settings settings;
	
	public enum DetailsTab{DASHBOARD, PARTICIPANT, SENSOR, SURVEY, TASK, LABELING, WEARABLE, SETTING};

	// private boolean isParticipantClicked = false, isSurveyClicked = false,
	// isSensingClicked = false, isTaskClicked = false, isLabellingClicked =
	// false;

	private Study study;
	private static StudyDetailsUiBinder uiBinder = GWT.create(StudyDetailsUiBinder.class);

	interface StudyDetailsUiBinder extends UiBinder<Widget, StudyDetails> {
	}

	public StudyDetails(Study study, DetailsTab tabIndex) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;

		nameHeading.setText(study.getName());

		tabNavigation = new NavTabs();
		tabContent = new TabContent();

		
		dashboard = new Dashboard(study);
		participantManagement = new ParticipantManagement(study);
		sensorManagement = new SensorManagement(study);
		surveyManagement = new SurveyManagement(study);
		labelManagement = new LabelingManagement(study);
		wearableManagement = new WearableManagement();
		settings = new Settings(study);

		dashboardTab = new TabListItem("Dashboard");
		dashboardPane = new TabPane();
		dashboardTab.setDataTargetWidget(dashboardPane);
		dashboardTab.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				dashboardPane.clear();
				dashboardPane.add(dashboard);
			}
		});

		participantTab = new TabListItem("Participants");
		participantPane = new TabPane();
		participantTab.setDataTargetWidget(participantPane);
		participantTab.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				participantPane.clear();
				participantPane.add(participantManagement);
			}
		});

		sensingTab = new TabListItem("Phone Sensor");
		sensingPane = new TabPane();
		sensingTab.setDataTargetWidget(sensingPane);
		sensingTab.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				sensingPane.clear();
				sensingPane.add(sensorManagement);
			}
		});

		surveyTab = new TabListItem("Survey");
		surveyPane = new TabPane();
		surveyTab.setDataTargetWidget(surveyPane);
		surveyTab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				surveyPane.clear();
				surveyPane.add(surveyManagement);
			}
		});

		taskTab = new TabListItem("Task");
		taskPane = new TabPane();
		taskTab.setDataTargetWidget(taskPane);
		taskTab.setEnabled(false);
		/*taskTab.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				taskPane.clear();
				//taskPane.add();
			}
		});*/

		labelingTab = new TabListItem("Labelling");
		labelingPane = new TabPane();
		labelingTab.setDataTargetWidget(labelingPane);
		labelingTab.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				labelingPane.add(labelManagement);
			}
		});
		
		wearableTab = new TabListItem("Wearable");
		wearablePane = new TabPane();
		wearableTab.setDataTargetWidget(wearablePane);
		wearableTab.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				wearablePane.add(wearableManagement);
			}
		});

		settingsTab = new TabListItem("Settings");
		settingsPane = new TabPane();
		settingsTab.setDataTargetWidget(settingsPane);
		settingsTab.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				settingsPane.clear();
				settingsPane.add(settings);
			}
		});

		tabNavigation.add(dashboardTab);
		tabContent.add(dashboardPane);
		
		tabNavigation.add(participantTab);
		tabContent.add(participantPane);
		
		tabNavigation.add(sensingTab);
		tabContent.add(sensingPane);
		
		tabNavigation.add(surveyTab);
		tabContent.add(surveyPane);
		
		//tabNavigation.add(taskTab);
		//tabContent.add(taskPane);
		
		tabNavigation.add(labelingTab);
		tabContent.add(labelingPane);
		
		tabNavigation.add(wearableTab);
		tabContent.add(wearablePane);
		
		tabNavigation.add(settingsTab);
		tabContent.add(settingsPane);

		detailsContentPanel.add(tabNavigation);
		detailsContentPanel.add(tabContent);
		
		
		// tabContent.add(new Settings());
		homeAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Mcsweb.getEventBus().fireEvent(new StudyEvent(new StudyState(null, StudySpecificState.HOME)));
			}
		});
		
		if (tabIndex==DetailsTab.DASHBOARD && dashboardTab!=null && dashboardPane != null && dashboard != null) {
			dashboardTab.setActive(true);
			dashboardPane.setActive(true);
			dashboardPane.clear();
			dashboardPane.add(dashboard);			
		}else if(tabIndex==DetailsTab.PARTICIPANT && participantTab!=null && participantPane!=null && participantManagement!=null){
			participantTab.setActive(true);
			participantPane.setActive(true);
			participantPane.clear();
			participantPane.add(participantManagement);
		}else if(tabIndex==DetailsTab.SENSOR && sensingTab!=null && sensingPane!=null && sensorManagement!=null){
			sensingTab.setActive(true);
			sensingPane.setActive(true);
			sensingPane.clear();
			sensingPane.add(sensorManagement);
		}else if(tabIndex==DetailsTab.SURVEY && surveyTab!=null && surveyPane!=null && surveyManagement!=null){
			surveyTab.setActive(true);
			surveyPane.setActive(true);
			surveyPane.clear();
			surveyPane.add(surveyManagement);
		}else if(tabIndex==DetailsTab.LABELING && labelingTab!=null && labelingPane!=null && labelManagement!=null){
			labelingTab.setActive(true);
			labelingPane.setActive(true);
			labelingPane.clear();
			labelingPane.add(labelManagement);
		}else if(tabIndex==DetailsTab.WEARABLE && labelingTab!=null && labelingPane!=null && labelManagement!=null){
			labelingTab.setActive(true);
			labelingPane.setActive(true);
			labelingPane.clear();
			labelingPane.add(labelManagement);
		}else if(tabIndex==DetailsTab.SETTING && settingsTab!=null && settingsPane!=null && settings!=null){
			settingsTab.setActive(true);
			settingsPane.setActive(true);
			settingsPane.clear();
			settingsPane.add(settings);
		}else{
			dashboardTab.setActive(true);
			dashboardPane.setActive(true);
			dashboardPane.clear();
			dashboardPane.add(dashboard);						
		}

	}


}
