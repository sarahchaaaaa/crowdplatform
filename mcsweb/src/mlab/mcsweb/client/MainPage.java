package mlab.mcsweb.client;

import org.gwtbootstrap3.client.ui.AnchorListItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.events.StudyEvent;
import mlab.mcsweb.client.events.StudyEventHandler;
import mlab.mcsweb.client.events.StudyState.StudySpecificState;
import mlab.mcsweb.client.events.SurveyEvent;
import mlab.mcsweb.client.events.SurveyEventHandler;
import mlab.mcsweb.client.events.SurveyState.SurveySpecificState;
import mlab.mcsweb.client.study.StudyDetails;
import mlab.mcsweb.client.study.survey.SurveyEditor;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.SurveySummary;

public class MainPage extends Composite {
	
	@UiField
	HTMLPanel mainPageContentPanel;
	
	@UiField
	AnchorListItem logoutAnchor;
	
	private UserHomePage userHomePage = null;
	
	private static String userId;
	
	private Mcsweb application;

	private static MainPageUiBinder uiBinder = GWT.create(MainPageUiBinder.class);

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	public MainPage(Mcsweb application, String email) {
		initWidget(uiBinder.createAndBindUi(this));
		userId = email;
		this.application = application;
		
		Mcsweb.getEventBus().addHandler(StudyEvent.TYPE, new StudyEventHandler() {
			
			@Override
			public void actionAfterStudyEvent(StudyEvent event) {
				// TODO Auto-generated method stub
				if(event.getState().getStudySpecificState() == StudySpecificState.GETDETAILS){
					loadStudyDetails(event.getState().getStudy());
				}else if(event.getState().getStudySpecificState() == StudySpecificState.HOME){
					loadUserHome();
				}

			}
		});
		
		
		Mcsweb.getEventBus().addHandler(SurveyEvent.TYPE, new SurveyEventHandler() {
			
			@Override
			public void actionAfterSurveyEvent(SurveyEvent surveyEvent) {
				// TODO Auto-generated method stub
				if(surveyEvent.getState().getSurveySpecificState() == SurveySpecificState.NEW){
					loadSurveyEditor(surveyEvent.getState().getSurveySummary());
				}else if (surveyEvent.getState().getSurveySpecificState() == SurveySpecificState.EXIT) {
					Study study = new Study();
					study.setId(surveyEvent.getState().getSurveySummary().getStudyId());
					loadStudyDetails(study);
				}
			}
		});
		
		loadUserHome();
	}
	
	public void loadUserHome(){
		mainPageContentPanel.clear();
		if(userHomePage == null){
			userHomePage = new UserHomePage(userId);
		}
		mainPageContentPanel.add(userHomePage);
	}
	
	public void loadStudyDetails(Study study){
		mainPageContentPanel.clear();
		mainPageContentPanel.add(new StudyDetails(study));
	}
	
	
	protected void loadSurveyEditor(SurveySummary surveySummary) {
		mainPageContentPanel.clear();
		mainPageContentPanel.add(new SurveyEditor(surveySummary));
	}
	
	@UiHandler("logoutAnchor")
	public void logout(ClickEvent event){
		userId = "";
		this.application.logout();
	}
	
	public static String getLoggedinUser() {
		return userId;
	}


}
