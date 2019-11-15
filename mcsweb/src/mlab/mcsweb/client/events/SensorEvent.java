package mlab.mcsweb.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class SensorEvent extends GwtEvent<SensorEventHandler>{
	private SensorState state;

	public SensorEvent(SensorState state) {
		super();
		this.state = state;
	}
	
	public static Type<SensorEventHandler> TYPE = new Type<SensorEventHandler>();
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SensorEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	
	@Override
	protected void dispatch(SensorEventHandler handler) {
		// TODO Auto-generated method stub
		handler.actionAfterSensorEvent(this);
	}
	
	public SensorState getState() {
		return state;
	}
}
