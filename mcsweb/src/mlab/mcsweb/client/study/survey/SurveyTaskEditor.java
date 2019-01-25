package mlab.mcsweb.client.study.survey;

import java.util.ArrayList;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.GreetingService;
import mlab.mcsweb.client.GreetingServiceAsync;
import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.study.survey.TaskEditorState.EditorSpecificState;
import mlab.mcsweb.shared.SurveySummary;
import mlab.mcsweb.shared.SurveyTask;


public class SurveyTaskEditor extends Composite {
	
	@UiField
	HTMLPanel taskEditorPanel;
	
	//private Study study;
	//private String surveyId;
	
	private AbsolutePanel boundaryPanel = new AbsolutePanel();
	private final VerticalPanel targetPanel = new VerticalPanel();

	//private final StudyConfigurationServiceAsync studyConfigService = GWT.create(StudyConfigurationService.class);
	private final GreetingServiceAsync surveyService = GWT.create(GreetingService.class);


	private static SurveyTaskEditorUiBinder uiBinder = GWT.create(SurveyTaskEditorUiBinder.class);

	interface SurveyTaskEditorUiBinder extends UiBinder<Widget, SurveyTaskEditor> {
	}

	public SurveyTaskEditor(SurveySummary surveySummary) {
		initWidget(uiBinder.createAndBindUi(this));
		
		taskEditorPanel.add(boundaryPanel);
		boundaryPanel.add(targetPanel, 0, 0);
		
		
		final PickupDragController dragController = new PickupDragController(boundaryPanel, false);
		dragController.setBehaviorConstrainedToBoundaryPanel(false);

		// Allow multiple widgets to be selected at once using CTRL-click
		dragController.setBehaviorMultipleSelection(false);

		
		DropController dropController = new VerticalPanelDropController(targetPanel);
		// Don't forget to register each DropController with a DragController
		dragController.registerDropController(dropController);
		
		if(surveySummary.getId()<=0){
			TaskComponent taskWidget = new TaskComponent(null);
			
			targetPanel.add(taskWidget);
			dragController.makeDraggable(taskWidget, taskWidget.getDragableChild());
			
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				
				@Override
				public void execute() {
					targetPanel.setWidth("100%");
					boundaryPanel.setPixelSize(taskEditorPanel.getElement().getClientWidth(), (int) (targetPanel.getElement().getClientHeight() * 2));
				}
			});

		} else{
			surveyService.getSurveyTaskList(surveySummary.getStudyId(), surveySummary.getId(), new AsyncCallback<ArrayList<SurveyTask>>() {
				
				@Override
				public void onSuccess(ArrayList<SurveyTask> result) {
					// TODO Auto-generated method stub
					for(int i=0;i<result.size();i++){
						TaskComponent taskWidget = new TaskComponent(result.get(i));
						
						targetPanel.add(taskWidget);
						dragController.makeDraggable(taskWidget, taskWidget.getDragableChild());
						
					}
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						
						@Override
						public void execute() {
							targetPanel.setWidth("100%");
							boundaryPanel.setPixelSize(taskEditorPanel.getElement().getClientWidth(), (int) (targetPanel.getElement().getClientHeight() * 2));
						}
					});
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert("Service not available, please try later.");
				}
			});
		}
		
		
		Mcsweb.getEventBus().addHandler(TaskEditorEvent.TYPE, new TaskEditorEventHandler() {
			
			@Override
			public void actionAfterEditor(TaskEditorEvent editorEvent) {
				
				if(editorEvent.getState().getEditorSpecificState() == EditorSpecificState.ADD){
					TaskComponent taskWidget = new TaskComponent(null);
					targetPanel.add(taskWidget);
					dragController.makeDraggable(taskWidget, taskWidget.getDragableChild());
					resizeBoundaryPanel();
				}else if(editorEvent.getState().getEditorSpecificState() == EditorSpecificState.RESIZE){
					resizeBoundaryPanel();
				}
				

			}
		});

		
	}
	
	protected ArrayList<SurveyTask> getAllSurveyTask() {
		ArrayList<SurveyTask> taskList = new ArrayList<>();
		int taskId = 0;
		for(int i=0;i<targetPanel.getWidgetCount();i++){
			Widget widget = targetPanel.getWidget(i);
			if(widget instanceof TaskComponent){
				ArrayList<SurveyTask> nestedList = ((TaskComponent) widget).getSurveyTasks();
				for(int j=0;j<nestedList.size();j++){
					taskId = taskId + 1;
					try {
						SurveyTask surveyTask = nestedList.get(j);
						//surveyTask.setStudyId(this.study.getId());
						/*if(surveyId!=null && !surveyId.isEmpty()){
							surveyTask.setSurveyId(Integer.parseInt(surveyId));
						}*/
						surveyTask.setTaskId(taskId);
						surveyTask.setOrderId(taskId);
						taskList.add(surveyTask);
						
					} catch (Exception e) {
						// TODO: handle exception
						Window.alert("exception:"+ e.getMessage());
					}
				}
			}
		}
		return taskList;
	}
	
	protected boolean isValidToPublish() {
		boolean atLeaseOneInvalid = false;
		if(targetPanel.getWidgetCount()==0){
			return false;
		}
		for(int i=0;i<targetPanel.getWidgetCount();i++){
			Widget widget = targetPanel.getWidget(i);
			if(widget instanceof TaskComponent){
				boolean valid = ((TaskComponent) widget).isValidToPublish();
				if(!valid){
					atLeaseOneInvalid = true;
				}
			}
		}
		if(atLeaseOneInvalid){
			return false;
		}
		return true;
		
	}
	
	
	private void resizeBoundaryPanel(){
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				boundaryPanel.setPixelSize(taskEditorPanel.getElement().getClientWidth(), (int) (targetPanel.getElement().getClientHeight() * 1.5));
			}
		});

	}

}
