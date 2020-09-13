package mlab.mcsweb.client.study;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.events.SurveyEvent;
import mlab.mcsweb.client.events.SurveyState;
import mlab.mcsweb.client.events.SurveyState.SurveySpecificState;
import mlab.mcsweb.client.services.SurveyService;
import mlab.mcsweb.client.services.SurveyServiceAsync;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.SurveySummary;

public class SurveyManagement extends Composite {
	
	@UiField
	Column listColumn;
	@UiField
	Button buttonCreate;
	
	//@UiField
	//HTMLPanel leftColumn;
	
	//ImageAnchor imageAnchor = new ImageAnchor();
	
	private boolean isLoaded = false;
	private ArrayList<SurveyOverview> surveyList = new ArrayList<>();
	
	private Study study;

	//private final StudyConfigServiceAsync studyConfigService = GWT.create(StudyConfigService.class);
	private final SurveyServiceAsync surveyService = GWT.create(SurveyService.class);


	private static SurveyUiBinder uiBinder = GWT.create(SurveyUiBinder.class);

	interface SurveyUiBinder extends UiBinder<Widget, SurveyManagement> {
	}

	public SurveyManagement(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
	}
	
	
	@Override
	protected void onLoad() {

		super.onLoad();
		if(isLoaded){
			//do nothing
		}else {			
			buttonCreate.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					SurveySummary surveySummary = new SurveySummary();
					surveySummary.setStudyId(study.getId());
					Mcsweb.getEventBus().fireEvent(new SurveyEvent(new SurveyState(surveySummary, SurveySpecificState.NEW)));
				}
			});
			
			surveyService.getSurveyList(study.getId(), new AsyncCallback<ArrayList<SurveySummary>>() {
				
				@Override
				public void onSuccess(ArrayList<SurveySummary> result) {
					// TODO Auto-generated method stub
					for(int i=0;i<result.size();i++){
						if (result.get(i).getLifecycle()>=0) {
							surveyList.add(new SurveyOverview(result.get(i), surveyService));
							//Window.alert("survey id :"+ result.get(i).getId() + " study id:"+ result.get(i).getStudyId());		
						}

					}
					
					for(int i=0;i<surveyList.size();i++){
						listColumn.add(surveyList.get(i));
					}

				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
//					Window.alert("service not available");
				}
			});
						
			
			isLoaded = true;
		}
	}
	

}


class SurveyOverview extends BaseOverview{

	SurveySummary surveySummary;
	
//	Button detailsButton, activateButton, deactivateButton, deleteButton;
	
	public SurveyOverview(final SurveySummary surveySummary, final SurveyServiceAsync service) {
		
		this.surveySummary = surveySummary;
		
		String name = surveySummary.getName();
		String modificationTime = surveySummary.getModificationTime();
		String modificationTimeZone = surveySummary.getModificationTimeZone();
		String lastSaveTime = "";
		try {
			int unixtime = Integer.parseInt(modificationTime) + Integer.parseInt(modificationTimeZone);
			lastSaveTime = JSUtil.getDatetimeFromUnix(String.valueOf(unixtime));
		} catch (Exception e) {
			// TODO: handle exception
		}
		String description = "Saved at  "+ lastSaveTime;
	
		setOverviewPanel(name, description);
		
		if (surveySummary.getLifecycle()==0) {
			activateButton.setEnabled(true);
			deactivateButton.setEnabled(false);
			activateButton.setType(ButtonType.PRIMARY);
			deactivateButton.setType(ButtonType.DEFAULT);
		}else if(surveySummary.getLifecycle()==1){
			deactivateButton.setEnabled(true);
			activateButton.setEnabled(false);
			activateButton.setType(ButtonType.DEFAULT);
			deactivateButton.setType(ButtonType.PRIMARY);
		}
		
		detailsButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HTMLPanel htmlPanel = getOverviewPanel();
				htmlPanel.addDomHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						Mcsweb.getEventBus().fireEvent(new SurveyEvent(new SurveyState(surveySummary, SurveySpecificState.NEW)));
					}
				}, ClickEvent.getType());
			}
		});
		
		activateButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				surveySummary.setModificationTime(JSUtil.getUnixtime());
				surveySummary.setModificationTimeZone(JSUtil.getTimezoneOffset());
				surveySummary.setLifecycle(1);//1=active
				service.changeLifecycle(surveySummary, new AsyncCallback<Response>() {

					@Override
					public void onFailure(Throwable caught) {
						Notify.notify("Service not available. Please try later.", NotifyType.DANGER);		
					}

					@Override
					public void onSuccess(Response result) {
						if (result.getCode()==0) {
							activateButton.setEnabled(false);
							deactivateButton.setEnabled(true);
							activateButton.setType(ButtonType.DEFAULT);
							deactivateButton.setType(ButtonType.PRIMARY);

							Notify.notify("The survey is active now.", NotifyType.SUCCESS);		
						}else{
							Notify.notify("Service not available. Please try later.", NotifyType.DANGER);		
						}
					}
					
				});
			}
		});
		
		deactivateButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				surveySummary.setModificationTime(JSUtil.getUnixtime());
				surveySummary.setModificationTimeZone(JSUtil.getTimezoneOffset());
				surveySummary.setLifecycle(0);//0=deactive
				service.changeLifecycle(surveySummary, new AsyncCallback<Response>() {

					@Override
					public void onFailure(Throwable caught) {
						Notify.notify("Service not available. Please try later.", NotifyType.DANGER);		
					}

					@Override
					public void onSuccess(Response result) {
						if (result.getCode()==0) {
							activateButton.setEnabled(true);
							deactivateButton.setEnabled(false);
							activateButton.setType(ButtonType.PRIMARY);
							deactivateButton.setType(ButtonType.DEFAULT);
							Notify.notify("The survey is not active anymore.", NotifyType.SUCCESS);		
						}else{
							Notify.notify("Service not available. Please try later.", NotifyType.DANGER);		
						}
					}
					
				});
			}
		});
		
		
		
		deleteButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				surveySummary.setModificationTime(JSUtil.getUnixtime());
				surveySummary.setModificationTimeZone(JSUtil.getTimezoneOffset());
				surveySummary.setLifecycle(-1);//-1=delete
				
				
				service.changeLifecycle(surveySummary, new AsyncCallback<Response>() {

					@Override
					public void onFailure(Throwable caught) {
						Notify.notify("Service not available. Please try later.", NotifyType.DANGER);		
					}

					@Override
					public void onSuccess(Response result) {
						if (result.getCode()==0) {
							removeFromParent();
							Notify.notify("The survey has been deleted successfully", NotifyType.SUCCESS);		
						}else{
							Notify.notify("Service not available. Please try later.", NotifyType.DANGER);									
						}
					}
					
				});
			}
		});
		
		
		
	}
	
}
