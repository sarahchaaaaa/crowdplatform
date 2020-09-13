package mlab.mcsweb.client.study.survey;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.study.survey.TaskEditorState.EditorSpecificState;
import mlab.mcsweb.shared.SurveyTask;
import mlab.mcsweb.shared.Util;

public class TaskComponent extends Composite {
	
	@UiField
	HTMLPanel taskPanel, dynamicPanel;

	@UiField
	Button dragObject, dropButton;
	
	@UiField
	TextArea questionBox;
	
	@UiField
	Select optionSelect;
	
	@UiField
	CheckBox requiredCheckbox;
	
	@UiField
	Label errorLabel;
	
	private boolean isClicked = false;

	private static TaskComponentUiBinder uiBinder = GWT.create(TaskComponentUiBinder.class);

	interface TaskComponentUiBinder extends UiBinder<Widget, TaskComponent> {
	}

	public TaskComponent(final SurveyTask surveyTask) {
		initWidget(uiBinder.createAndBindUi(this));
		questionBox.getElement().getStyle().setBorderStyle(BorderStyle.HIDDEN);
		dragObject.getElement().getStyle().setBorderStyle(BorderStyle.HIDDEN);
		dragObject.setIcon(IconType.ARROWS);
		dragObject.setWidth("100%");
		dragObject.setSize(ButtonSize.SMALL);;
		
		dropButton.setIcon(IconType.TRASH);
		
		optionSelect.setDropupAuto(false);
		
		if(surveyTask != null){
			
			try {
				questionBox.setText(surveyTask.getTaskText());
				final boolean isRequired = surveyTask.getIsRequired()==0 ? false : true;
				requiredCheckbox.setValue(isRequired);
				final String option = surveyTask.getType().toLowerCase();
				final boolean isCommentEnabled = surveyTask.getHasComment()==0 ? false : true;
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					
					@Override
					public void execute() {
						if(option.equals("text")){
							//Window.alert("this is a text");
							optionSelect.setValue("text", true);
						}else if(option.equals("textarea")){
							optionSelect.setValue("textarea", true);
						}else if(option.equals("choice")){
							optionSelect.setValue("choice", false);
							dynamicPanel.add(new MultipleChoiceView(surveyTask.getPossibleInput(), true, isCommentEnabled));
						}else if(option.equals("selection")){
							optionSelect.setValue("choice", false);
							dynamicPanel.add(new MultipleChoiceView(surveyTask.getPossibleInput(), false, isCommentEnabled));							
						}else if(option.equals("date")){
							optionSelect.setValue("date", true);
						}/*else if(option.startsWith("mt0")){
							
							optionSelect.setValue("motortask", false);
							questionBox.setVisible(false);
							dynamicPanel.add(new MotorTaskView(surveyTask.getType()));
						}*/
						optionSelect.render();
					}
				});

				
			} catch (Exception e) {
				// TODO: handle exception
				Window.alert("exception "+ e.getLocalizedMessage());
			}
		}
		
	}
	
	protected Widget getDragableChild() {
		return dragObject;
	}
	
	protected ArrayList<SurveyTask> getSurveyTasks() {
		ArrayList<SurveyTask> taskList = new ArrayList<>();
		
		SurveyTask surveyTask = new SurveyTask();
		if(questionBox.isVisible()){
			surveyTask.setTaskText(questionBox.getText());
		}
		String type = "";
		String selection = optionSelect.getValue().toLowerCase();
		if(selection.equals("text")){
			type = "text";
		}else if(selection.equals("textarea")){
			type = "textarea";
		}else if(selection.equals("choice")){
			
			if(dynamicPanel.getWidgetCount()>0 && dynamicPanel.getWidget(0) instanceof MultipleChoiceView){
				MultipleChoiceView choiceView = (MultipleChoiceView) dynamicPanel.getWidget(0);
				surveyTask.setPossibleInput(choiceView.getPossibleInput());
				if(choiceView.isMultipleAllowed()){
					type = "choice";
				}else {
					type = "selection";
				}
				
				if (choiceView.isCommentEnabled()) {
					surveyTask.setHasComment(1);
				}
			}
		}else if(selection.equals("date")){
			type = "date";
		}/*else if(selection.equals("motortask")){
			if(dynamicPanel.getWidgetCount()>0 && dynamicPanel.getWidget(0) instanceof MotorTaskView){
				type = ((MotorTaskView)dynamicPanel.getWidget(0)).getMotorTaskCode();
			}			
		}*/
		surveyTask.setType(type);
		if (requiredCheckbox.getValue()) {
			surveyTask.setIsRequired(1);
		}else{
			surveyTask.setIsRequired(0);
		}
		taskList.add(surveyTask);
		
		return taskList;
	}
	
	@UiHandler("dropButton")
	void dropTask(ClickEvent event){
		this.removeFromParent();
		Mcsweb.getEventBus().fireEvent(new TaskEditorEvent(new TaskEditorState(EditorSpecificState.RESIZE)));
	}
	
	@UiHandler("optionSelect")
	void selectionChanged(ValueChangeEvent<String> event){
		String value = event.getValue().toLowerCase();
		
		questionBox.setVisible(true);
		dynamicPanel.clear();
		
		if(value.contains("text")){
			dynamicPanel.add(new TextAnswerView());
		}else if(value.contains("textarea")){
			dynamicPanel.add(new TextAnswerView());
		}else if(value.contains("choice")){
			dynamicPanel.add(new MultipleChoiceView());
		}else if(value.contains("date")){
			dynamicPanel.add(new DateView());
		}/*else if(value.contains("motortask")){
			questionBox.setVisible(false);
			dynamicPanel.add(new MotorTaskView());
		}*/

	}
	
	protected boolean isValidToPublish() {
		errorLabel.setText("");
		if(questionBox.isVisible() && questionBox.getText().trim().length()==0){
			errorLabel.setText("Type the question");
			return false;
		}
		String option = optionSelect.getValue();
		if(Util.isEmptyString(option)){
			errorLabel.setText("Select one type");
			return false;
		}
		if(dynamicPanel.getWidgetCount()>0){
			String errorFromView = "";
			Widget widget = dynamicPanel.getWidget(0);
			if(widget instanceof MultipleChoiceView){
				errorFromView = ((MultipleChoiceView)widget).getError();
			}/*else if (widget instanceof MotorTaskView) {
				errorFromView = ((MotorTaskView) widget).getError();
			}*/
			if(Util.isEmptyString(errorFromView)){
				//do nothing
			}else {
				errorLabel.setText(errorFromView);
				return false;
			}
		}
		return true;
	}
}
