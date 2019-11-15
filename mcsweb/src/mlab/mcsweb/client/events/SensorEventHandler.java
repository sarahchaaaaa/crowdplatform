package mlab.mcsweb.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface SensorEventHandler extends EventHandler {
	public void actionAfterSensorEvent(SensorEvent sensorEvent);
}
