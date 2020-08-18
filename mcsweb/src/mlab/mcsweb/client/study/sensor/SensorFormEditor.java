package mlab.mcsweb.client.study.sensor;

import java.util.ArrayList;

import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.services.SensorService;
import mlab.mcsweb.client.services.SensorServiceAsync;
import mlab.mcsweb.shared.SensorAction;
import mlab.mcsweb.shared.SensorSummary;

public class SensorFormEditor extends Composite {

	@UiField
	HTMLPanel phoneSensorPanel, phoneEventPanel;

	private PhoneSensorEditor sensorEditor;
	private PhoneEventEditor eventEditor;
	private boolean isLoaded = false;
	
	private SensorSummary sensorSummary;
	
	private final SensorServiceAsync service = GWT.create(SensorService.class);


	private static SensorFormEditorUiBinder uiBinder = GWT.create(SensorFormEditorUiBinder.class);

	interface SensorFormEditorUiBinder extends UiBinder<Widget, SensorFormEditor> {
	}

	public SensorFormEditor(SensorSummary sensorSummary) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.sensorSummary = sensorSummary;
		
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();

		if (!isLoaded) {
			
			service.getSensorActionList(this.sensorSummary.getStudyId(), this.sensorSummary.getId(), new AsyncCallback<ArrayList<SensorAction>>() {
				
				@Override
				public void onSuccess(ArrayList<SensorAction> result) {
					
					sensorEditor = new PhoneSensorEditor(result);
					eventEditor = new PhoneEventEditor(result);
					phoneSensorPanel.add(sensorEditor);
					phoneEventPanel.add(eventEditor);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Notify.notify("Service not available, please try later!", NotifyType.DANGER);
				}
			});

			isLoaded = true;
		}
	}

	boolean isValid() {
		return sensorEditor.isValid();
	}

	ArrayList<SensorAction> getActionList() {
		ArrayList<SensorAction> actionList = new ArrayList<>(sensorEditor.getSensorActionList());
		actionList.addAll(eventEditor.getEventActionList());

		return actionList;
	}

}
