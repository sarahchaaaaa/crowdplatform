package mlab.mcsweb.client.study.sensor;

import java.util.Date;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.MainPage;
import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.events.SensorEvent;
import mlab.mcsweb.client.events.SensorState;
import mlab.mcsweb.client.events.SensorState.SensorSpecificState;
import mlab.mcsweb.client.services.SensorService;
import mlab.mcsweb.client.services.SensorServiceAsync;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.SensorConfiguration;
import mlab.mcsweb.shared.SensorSummary;

public class SensorEditor extends Composite {
	
	@UiField
	Button saveButton, publishButton, backButton;
	
	@UiField
	LinkedGroupItem triggerLink, formLink;
	
	@UiField
	HTMLPanel contentPanel;
	
	@UiField
	Heading editorError;
	
	
	private SensorSummary sensorSummary;
	private SensorFormEditor formEditor;
	private TriggerActionEditor triggerActionEditor; 
	
	private final SensorServiceAsync service = GWT.create(SensorService.class);


	private static SensorEditorUiBinder uiBinder = GWT.create(SensorEditorUiBinder.class);

	interface SensorEditorUiBinder extends UiBinder<Widget, SensorEditor> {
	}

	public SensorEditor(SensorSummary sensorSummary) {
		initWidget(uiBinder.createAndBindUi(this));
		this.sensorSummary = sensorSummary;
		
		saveButton.setSize(ButtonSize.LARGE);
		saveButton.getElement().getStyle().setBorderStyle(BorderStyle.HIDDEN);
		saveButton.setIcon(IconType.SAVE);
		saveButton.setIconPosition(IconPosition.RIGHT);
		
		publishButton.setSize(ButtonSize.LARGE);
		publishButton.getElement().getStyle().setBorderStyle(BorderStyle.HIDDEN);
		publishButton.setIcon(IconType.CLOUD_UPLOAD);
		publishButton.setIconPosition(IconPosition.RIGHT);
		
		
		loadFormEditor(null);

	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		editorError.setText("");
	}
	
	@UiHandler("backButton")
	void backToStudyDetails(ClickEvent event){
		Mcsweb.getEventBus().fireEvent(new SensorEvent(new SensorState(sensorSummary, SensorSpecificState.EXIT)));

	}
	
	@UiHandler("saveButton")
	void saveSensorConfig(ClickEvent event){
		
		editorError.setText("");
		
		if(formEditor!=null && formEditor.isValid()){
			final SensorSummary finalSensorSummary = this.sensorSummary == null ? new SensorSummary() : this.sensorSummary;
			if(finalSensorSummary.getId()<=0){
				//temporary give name
				finalSensorSummary.setName("Sensor_Config_" + new Date());
				
				finalSensorSummary.setCreatedBy(MainPage.getLoggedinUser());
				finalSensorSummary.setCreationTime(JSUtil.getUnixtime());
				finalSensorSummary.setCreationTimeZone(JSUtil.getTimezoneOffset());
			}
			
			finalSensorSummary.setModificationTime(JSUtil.getUnixtime());
			finalSensorSummary.setModificationTimeZone(JSUtil.getTimezoneOffset());
			
			SensorConfiguration sensorConfiguration = new SensorConfiguration(finalSensorSummary, formEditor.getActionList());
			service.saveSensorConfiguration(sensorConfiguration, new AsyncCallback<Response>() {
				
				@Override
				public void onSuccess(Response result) {
					Notify.notify("Configuration has been saved", NotifyType.SUCCESS);		
					Mcsweb.getEventBus().fireEvent(new SensorEvent(new SensorState(finalSensorSummary, SensorSpecificState.EXIT)));					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					editorError.setText("Service not available, please try later!");
				}
			});

		}
	}
	
	@UiHandler("publishButton")
	void publishSensorConfig(ClickEvent event){
		boolean atLeastOneError = false;
		editorError.setText("");
		if(formEditor != null && formEditor.isValid()){
			
		}else{
			editorError.setText("Error in configuration, please review!");
			atLeastOneError = true;
		}
		if(atLeastOneError){
			return;
		}
		final SensorSummary finalSensorSummary = this.sensorSummary == null ? new SensorSummary() : this.sensorSummary;
		if(finalSensorSummary.getId()<=0){
			//temporary give name
			finalSensorSummary.setName("Sensor_Config_" + new Date());
			
			finalSensorSummary.setCreatedBy(MainPage.getLoggedinUser());
			finalSensorSummary.setCreationTime(JSUtil.getUnixtime());
			finalSensorSummary.setCreationTimeZone(JSUtil.getTimezoneOffset());
		}
		
		finalSensorSummary.setModificationTime(JSUtil.getUnixtime());
		finalSensorSummary.setModificationTimeZone(JSUtil.getTimezoneOffset());
		finalSensorSummary.setPublishTime(JSUtil.getUnixtime());
		finalSensorSummary.setPublishTimeZone(JSUtil.getTimezoneOffset());
		int publishedVersion = finalSensorSummary.getPublishedVersion();
		finalSensorSummary.setPublishedVersion(publishedVersion+1);
		//0-created, 1-saved, 2-published
		finalSensorSummary.setState(2);
		
		//Window.alert("save clicked "+ JSUtil.getUnixtime());
		SensorConfiguration sensorConfig = new SensorConfiguration(finalSensorSummary, formEditor.getActionList());
		service.publishSensorConfiguration(sensorConfig, new AsyncCallback<Response>() {
			@Override
			public void onSuccess(Response result) {
				Notify.notify("Configuration has been published", NotifyType.SUCCESS);		
				Mcsweb.getEventBus().fireEvent(new SensorEvent(new SensorState(finalSensorSummary, SensorSpecificState.EXIT)));					
			}
			@Override
			public void onFailure(Throwable caught) {
				editorError.setText("Service not available, please try later!");
			}
		});


	}
	
	@UiHandler("triggerLink")
	void laodTriggerEditor(ClickEvent event){
		triggerLink.setActive(true);
		formLink.setActive(false);
		contentPanel.clear();
		
		if (triggerActionEditor == null) {
			triggerActionEditor = new TriggerActionEditor();
		}
		contentPanel.add(triggerActionEditor);		
	}
	
	@UiHandler("formLink")
	void loadFormEditor(ClickEvent event){
		triggerLink.setActive(false);
		formLink.setActive(true);
		
		contentPanel.clear();
		
		if(formEditor == null){
			formEditor = new SensorFormEditor(this.sensorSummary);
		}
		contentPanel.add(formEditor);
	}

}
