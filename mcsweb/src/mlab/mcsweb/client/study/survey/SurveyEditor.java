package mlab.mcsweb.client.study.survey;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.MainPage;
import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.events.SurveyEvent;
import mlab.mcsweb.client.events.SurveyState;
import mlab.mcsweb.client.events.SurveyState.SurveySpecificState;
import mlab.mcsweb.client.services.SurveyService;
import mlab.mcsweb.client.services.SurveyServiceAsync;
import mlab.mcsweb.client.study.survey.TaskEditorState.EditorSpecificState;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.SurveyConfiguration;
import mlab.mcsweb.shared.SurveySummary;



public class SurveyEditor extends Composite {
	
	@UiField
	Button backButton, saveButton, publishButton, addTaskButton;
	
	@UiField
	HTMLPanel taskPanel, schedulePanel;
	
	@UiField
	Heading taskEditorHeading;
	
	@UiField
	Column rightPanel;
	
	private SurveySummary surveySummary;
	
	private SurveyConfigView surveyConfigView;
	private SurveyTaskEditor surveyTaskEditor;
	
	//private final StudyConfigServiceAsync studyConfigService = GWT.create(StudyConfigService.class);
	private final SurveyServiceAsync service = GWT.create(SurveyService.class);


	private static SurveyEditorUiBinder uiBinder = GWT.create(SurveyEditorUiBinder.class);

	interface SurveyEditorUiBinder extends UiBinder<Widget, SurveyEditor> {
	}

	public SurveyEditor(SurveySummary surveySummary) {
		initWidget(uiBinder.createAndBindUi(this));
		

		this.surveySummary = surveySummary;
		
		backButton.setSize(ButtonSize.LARGE);
		backButton.getElement().getStyle().setBorderStyle(BorderStyle.HIDDEN);
		backButton.setIcon(IconType.ARROW_LEFT);
		backButton.setIconPosition(IconPosition.LEFT);
		
		saveButton.setSize(ButtonSize.LARGE);
		saveButton.getElement().getStyle().setBorderStyle(BorderStyle.HIDDEN);
		saveButton.setIcon(IconType.SAVE);
		saveButton.setIconPosition(IconPosition.RIGHT);
		
		publishButton.setSize(ButtonSize.LARGE);
		publishButton.getElement().getStyle().setBorderStyle(BorderStyle.HIDDEN);
		publishButton.setIcon(IconType.CLOUD_UPLOAD);
		publishButton.setIconPosition(IconPosition.RIGHT);
		
		surveyConfigView = new SurveyConfigView(surveySummary);
		schedulePanel.clear();
		schedulePanel.add(surveyConfigView);

		
		surveyTaskEditor = new SurveyTaskEditor(surveySummary);
		taskPanel.clear();
		taskPanel.add(surveyTaskEditor);
	}
	

	@UiHandler("backButton")
	void backToSurveyManagement(ClickEvent event){
		Mcsweb.getEventBus().fireEvent(new SurveyEvent(new SurveyState(surveySummary, SurveySpecificState.EXIT)));
	}
	
	@UiHandler("saveButton")
	void saveSurvey(ClickEvent event){
		if(surveyTaskEditor!=null && surveyConfigView.isValidForSaving()){
			
			final SurveySummary finalSurveySummary = surveyConfigView.getSurveySummary();
			//Window.alert("published version "+ finalSurveySummary.getPublishedVersion());			
			if(finalSurveySummary.getId()<=0){
				//first time
				finalSurveySummary.setCreatedBy(MainPage.getLoggedinUser());
				finalSurveySummary.setCreationTime(JSUtil.getUnixtime());
				finalSurveySummary.setCreationTimeZone(JSUtil.getTimezoneOffset());
			}
			
			finalSurveySummary.setModificationTime(JSUtil.getUnixtime());
			finalSurveySummary.setModificationTimeZone(JSUtil.getTimezoneOffset());
			SurveyConfiguration surveyConfiguration = new SurveyConfiguration(finalSurveySummary, surveyTaskEditor.getAllSurveyTask());
			
			service.saveSurveyConfiguration(surveyConfiguration, new AsyncCallback<Response>() {
				
				@Override
				public void onSuccess(Response result) {
					// TODO Auto-generated method stub
					Notify.notify("Survey has been saved", NotifyType.SUCCESS);		
					Mcsweb.getEventBus().fireEvent(new SurveyEvent(new SurveyState(finalSurveySummary, SurveySpecificState.EXIT)));
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert("Service Not Available");
				}
			});
		}
	}
	
	@UiHandler("publishButton")
	void publishSurvey(ClickEvent event){
		boolean atLeastOneError = false;
		taskEditorHeading.setSubText("");
		if(surveyTaskEditor!=null){
			if(!surveyTaskEditor.isValidToPublish()){
				taskEditorHeading.setSubText("Error in one of the component");
//				Window.alert("sub text "+ taskEditorHeading.getWidgetCount());
				taskEditorHeading.getWidget(1).getElement().getStyle().setColor("red");
				atLeastOneError = true;
			}
		}
		
		if(surveyConfigView!=null){
			if(!surveyConfigView.isValidToPublish()){
				atLeastOneError = true;
			}
		}
		if(atLeastOneError){
			return;
		}
		
		final SurveySummary finalSurveySummary = surveyConfigView.getSurveySummary();
		
		if(finalSurveySummary.getId()<=0){
			//first time
			finalSurveySummary.setCreatedBy(MainPage.getLoggedinUser());
			finalSurveySummary.setCreationTime(JSUtil.getUnixtime());
			finalSurveySummary.setCreationTimeZone(JSUtil.getTimezoneOffset());
		}
		
		finalSurveySummary.setModificationTime(JSUtil.getUnixtime());
		finalSurveySummary.setModificationTimeZone(JSUtil.getTimezoneOffset());
		finalSurveySummary.setPublishTime(JSUtil.getUnixtime());
		finalSurveySummary.setPublishTimeZone(JSUtil.getTimezoneOffset());
		int publishedVersion = finalSurveySummary.getPublishedVersion();
		finalSurveySummary.setPublishedVersion(publishedVersion+1);
		//0-created, 1-saved, 2-published
		finalSurveySummary.setState(2);
		
		//Window.alert("save clicked "+ JSUtil.getUnixtime());
		SurveyConfiguration surveyConfiguration = new SurveyConfiguration(finalSurveySummary, surveyTaskEditor.getAllSurveyTask());
		
		service.publishSurveyConfiguration(surveyConfiguration, new AsyncCallback<Response>() {
			@Override
			public void onSuccess(Response result) {
				Notify.notify("Survey will be pushed", NotifyType.SUCCESS);
				Mcsweb.getEventBus().fireEvent(new SurveyEvent(new SurveyState(finalSurveySummary, SurveySpecificState.EXIT)));
			}
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("Service Not Available");
			}
		});
		
	}
	
	@UiHandler("addTaskButton")
	void addTask(ClickEvent event){
		Mcsweb.getEventBus().fireEvent(new TaskEditorEvent(new TaskEditorState(EditorSpecificState.ADD)));
	}
}
