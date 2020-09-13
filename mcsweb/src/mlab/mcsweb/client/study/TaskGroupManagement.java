package mlab.mcsweb.client.study;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.ImageAnchor;
import org.gwtbootstrap3.client.ui.html.Br;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.StudyConfigService;
import mlab.mcsweb.client.StudyConfigServiceAsync;
import mlab.mcsweb.client.events.SurveyEvent;
import mlab.mcsweb.client.events.SurveyState;
import mlab.mcsweb.client.events.SurveyState.SurveySpecificState;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.SurveySummary;

public class TaskGroupManagement extends Composite {
	
	@UiField
	Column listColumn;
	
	@UiField
	HTMLPanel leftColumn;
	
	ImageAnchor imageAnchor = new ImageAnchor();
	
	private boolean isLoaded = false;
	private ArrayList<TaskGroupOverview> taskGroupList = new ArrayList<>();
	
	private Study study;

	private final StudyConfigServiceAsync studyConfigService = GWT.create(StudyConfigService.class);

	private static TaskGroupManagementUiBinder uiBinder = GWT.create(TaskGroupManagementUiBinder.class);

	interface TaskGroupManagementUiBinder extends UiBinder<Widget, TaskGroupManagement> {
	}

	public TaskGroupManagement(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		if(isLoaded){
			//do nothing
		}else {
			// TODO load survey for the first time
			imageAnchor.setUrl("images/create_new_256.png");
			imageAnchor.setResponsive(true);
			imageAnchor.setAsMediaObject(true);
			imageAnchor.setAlt("Create New Survey");
			//imageAnchor.setType(ImageType.THUMBNAIL);
			imageAnchor.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					SurveySummary surveySummary = new SurveySummary();
					surveySummary.setStudyId(study.getId());
					Mcsweb.getEventBus().fireEvent(new SurveyEvent(new SurveyState(surveySummary, SurveySpecificState.NEW)));
				}
			});
			
			leftColumn.add(new Br());
			leftColumn.add(new Br());
			leftColumn.add(imageAnchor);
			/*
			studyConfigService.getSurveyList(study.getId(), new AsyncCallback<ArrayList<SurveySummary>>() {
				
				@Override
				public void onSuccess(ArrayList<SurveySummary> result) {
					// TODO Auto-generated method stub
					for(int i=0;i<result.size();i++){
						surveyList.add(new SurveyOverview(result.get(i)));
						//Window.alert("survey id :"+ result.get(i).getId() + " study id:"+ result.get(i).getStudyId());

					}
					
					for(int i=0;i<surveyList.size();i++){
						listColumn.add(surveyList.get(i));
					}

				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert("service not available");
				}
			});
						
			*/
			isLoaded = true;

		}
	}

}


class TaskGroupOverview extends BaseOverview{

	SurveySummary surveySummary;
	
	public TaskGroupOverview(final SurveySummary surveySummary) {
		
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
		
	}
	
}
