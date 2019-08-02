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

	private TabListItem dashboardTab, participantTab, phoneSensingTab, surveyTab, taskTab, labelingTab, settingsTab;
	private TabPane dashboardPane, participantPane, phoneSensingPane, surveyPane, taskPane, labelingPane, settingsPane;

	private Dashboard dashboard;
	private ParticipantManagement participantManagement;
	private SensorManagement sensorManagement;
	private SurveyManagement surveyManagement;
	private LabelingManagement labelManagement;
	private Settings settings; 

	// private boolean isParticipantClicked = false, isSurveyClicked = false,
	// isSensingClicked = false, isTaskClicked = false, isLabellingClicked =
	// false;

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

		
		dashboard = new Dashboard(study);
		participantManagement = new ParticipantManagement(study);
		sensorManagement = new SensorManagement(study);
		surveyManagement = new SurveyManagement(study);
		labelManagement = new LabelingManagement(study);
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

		phoneSensingTab = new TabListItem("Phone Sensor");
		phoneSensingPane = new TabPane();
		phoneSensingTab.setDataTargetWidget(phoneSensingPane);
		phoneSensingTab.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				phoneSensingPane.clear();
				phoneSensingPane.add(sensorManagement);
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
		
		tabNavigation.add(phoneSensingTab);
		tabContent.add(phoneSensingPane);
		
		tabNavigation.add(surveyTab);
		tabContent.add(surveyPane);
		
		//tabNavigation.add(taskTab);
		//tabContent.add(taskPane);
		
		tabNavigation.add(labelingTab);
		tabContent.add(labelingPane);
		
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

	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		if (dashboardTab!=null && dashboardPane != null && dashboard != null) {
			dashboardTab.setActive(true);
			dashboardPane.setActive(true);
			dashboardPane.clear();
			dashboardPane.add(dashboard);
			
		}

	}

}
