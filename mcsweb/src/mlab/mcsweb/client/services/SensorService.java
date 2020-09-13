package mlab.mcsweb.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.SensorAction;
import mlab.mcsweb.shared.SensorConfiguration;
import mlab.mcsweb.shared.SensorSummary;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("sensor")
public interface SensorService extends RemoteService {

	Response saveSensorConfiguration(SensorConfiguration sensorConfiguration);
	Response publishSensorConfiguration(SensorConfiguration sensorConfiguration);
	ArrayList<SensorSummary> getSensorConfigList(long studyId);
	ArrayList<SensorAction> getSensorActionList(long studyId, long sensorConfigId);
	Response changeLifecycle(SensorSummary sensorSummary);
}
