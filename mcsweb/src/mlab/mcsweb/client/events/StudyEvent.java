package mlab.mcsweb.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class StudyEvent extends GwtEvent<StudyEventHandler>{
	private StudyState state;

	public StudyEvent(StudyState state) {
		super();
		this.state = state;
	}
	
	public static Type<StudyEventHandler> TYPE = new Type<StudyEventHandler>();
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StudyEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	
	@Override
	protected void dispatch(StudyEventHandler handler) {
		// TODO Auto-generated method stub
		handler.actionAfterStudyEvent(this);
	}
	
	public StudyState getState() {
		return state;
	}
}
