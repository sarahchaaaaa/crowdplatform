package mlab.mcsweb.client.study.survey;

import com.google.gwt.event.shared.GwtEvent;

public class TaskEditorEvent extends GwtEvent<TaskEditorEventHandler>{
	private TaskEditorState state;

	public TaskEditorEvent(TaskEditorState state) {
		super();
		this.state = state;
	}
	
	public static Type<TaskEditorEventHandler> TYPE = new Type<TaskEditorEventHandler>();
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<TaskEditorEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	
	@Override
	protected void dispatch(TaskEditorEventHandler handler) {
		// TODO Auto-generated method stub
		handler.actionAfterEditor(this);
	}
	
	public TaskEditorState getState() {
		return state;
	}
}
