package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.SensorAction;
import mlab.mcsweb.shared.SensorConfiguration;
import mlab.mcsweb.shared.SensorSummary;

/**
 * The async counterpart of <code>SensorService</code>.
 */
public interface SensorServiceAsync {

	void saveSensorConfiguration(SensorConfiguration sensorConfiguration, AsyncCallback<Response> callback);
	void publishSensorConfiguration(SensorConfiguration sensorConfiguration, AsyncCallback<Response> callback);
	void getSensorConfigList(long studyId, AsyncCallback<ArrayList<SensorSummary>> callback);
	void getSensorActionList(long studyId, long sensorConfigId, AsyncCallback<ArrayList<SensorAction>> callback);
		
}
