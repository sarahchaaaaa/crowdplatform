package mlab.mcsweb.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class SurveyEvent extends GwtEvent<SurveyEventHandler>{
	private SurveyState state;

	public SurveyEvent(SurveyState state) {
		super();
		this.state = state;
	}
	
	public static Type<SurveyEventHandler> TYPE = new Type<SurveyEventHandler>();
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SurveyEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	
	@Override
	protected void dispatch(SurveyEventHandler handler) {
		// TODO Auto-generated method stub
		handler.actionAfterSurveyEvent(this);
	}
	
	public SurveyState getState() {
		return state;
	}
}
