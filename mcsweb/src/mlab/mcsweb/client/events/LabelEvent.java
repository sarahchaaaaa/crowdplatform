package mlab.mcsweb.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class LabelEvent extends GwtEvent<LabelEventHandler>{
	private LabelState state;

	public LabelEvent(LabelState state) {
		super();
		this.state = state;
	}
	
	public static Type<LabelEventHandler> TYPE = new Type<LabelEventHandler>();
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LabelEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	
	@Override
	protected void dispatch(LabelEventHandler handler) {
		// TODO Auto-generated method stub
		handler.actionAfterLabelEvent(this);
	}
	
	public LabelState getState() {
		return state;
	}
}
