package mlab.mcsweb.client.study;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.events.SensorEvent;
import mlab.mcsweb.client.events.SensorState;
import mlab.mcsweb.client.events.SensorState.SensorSpecificState;
import mlab.mcsweb.client.services.SensorService;
import mlab.mcsweb.client.services.SensorServiceAsync;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.SensorSummary;
import mlab.mcsweb.shared.Study;

public class SensorManagement extends Composite {
	
	@UiField
	Column listColumn;
	
	@UiField
	Button buttonAdd;
		
	
	private boolean isLoaded = false;
	private ArrayList<SensorOverview> sensorConfigList = new ArrayList<>();
	
	private Study study;

	//private final StudyConfigServiceAsync studyConfigService = GWT.create(StudyConfigService.class);
	private final SensorServiceAsync service = GWT.create(SensorService.class);


	private static SensorManagementUiBinder uiBinder = GWT.create(SensorManagementUiBinder.class);

	interface SensorManagementUiBinder extends UiBinder<Widget, SensorManagement> {
	}

	public SensorManagement(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		
	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		if(isLoaded){
			//do nothing
		}else {
			
			buttonAdd.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					SensorSummary sensorSummary = new SensorSummary();
					sensorSummary.setStudyId(study.getId());
					Mcsweb.getEventBus().fireEvent(new SensorEvent(new SensorState(sensorSummary, SensorSpecificState.NEW)));

				}
			});

			
			service.getSensorConfigList(study.getId(), new AsyncCallback<ArrayList<SensorSummary>>() {
				@Override
				public void onSuccess(ArrayList<SensorSummary> result) {
					for(int i=0;i<result.size();i++){
						if (result.get(i).getLifecycle()>=0) {
							sensorConfigList.add(new SensorOverview(result.get(i), service));
//							Window.alert("survey id :"+ result.get(i).getId() + " study id:"+ result.get(i).getStudyId());	
						}
					}
					
					for(int i=0;i<sensorConfigList.size();i++){
						listColumn.add(sensorConfigList.get(i));
					}					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert("service not available");
				}
			});
						
			
			isLoaded = true;
		}

	}

}

class SensorOverview extends BaseOverview{

	SensorSummary sensorSummary;
	
	public SensorOverview(final SensorSummary sensorSummary, final SensorServiceAsync service) {
		
		this.sensorSummary = sensorSummary;
		
		String name = sensorSummary.getName();
		String modificationTime = sensorSummary.getModificationTime();
		String modificationTimeZone = sensorSummary.getModificationTimeZone();
		String lastSaveTime = "";
		try {
			int unixtime = Integer.parseInt(modificationTime) + Integer.parseInt(modificationTimeZone);
			lastSaveTime = JSUtil.getDatetimeFromUnix(String.valueOf(unixtime));
		} catch (Exception e) {
			// TODO: handle exception
		}
		String description = "Saved at  "+ lastSaveTime;
	
		setOverviewPanel(name, description);
		if (sensorSummary.getLifecycle()==0) {
			activateButton.setEnabled(true);
			deactivateButton.setEnabled(false);
			activateButton.setType(ButtonType.PRIMARY);
			deactivateButton.setType(ButtonType.DEFAULT);
		}else if(sensorSummary.getLifecycle()==1){
			deactivateButton.setEnabled(true);
			activateButton.setEnabled(false);
			activateButton.setType(ButtonType.DEFAULT);
			deactivateButton.setType(ButtonType.PRIMARY);
		}

		detailsButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HTMLPanel htmlPanel = getOverviewPanel();
				htmlPanel.addDomHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						Mcsweb.getEventBus().fireEvent(new SensorEvent(new SensorState(sensorSummary, SensorSpecificState.NEW)));
					}
				}, ClickEvent.getType());				
			}
		});

		
		activateButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				sensorSummary.setModificationTime(JSUtil.getUnixtime());
				sensorSummary.setModificationTimeZone(JSUtil.getTimezoneOffset());
				sensorSummary.setLifecycle(1);//1=active
				service.changeLifecycle(sensorSummary, new AsyncCallback<Response>() {

					@Override
					public void onFailure(Throwable caught) {
						Notify.notify("Service not available. Please try later.", NotifyType.DANGER);		
					}

					@Override
					public void onSuccess(Response result) {
						if (result.getCode()==0) {
							activateButton.setEnabled(false);
							deactivateButton.setEnabled(true);		
							activateButton.setType(ButtonType.DEFAULT);
							deactivateButton.setType(ButtonType.PRIMARY);
							Notify.notify("The sensor configuration is active now.", NotifyType.SUCCESS);		
						}else{
							Notify.notify("Service not available. Please try later.", NotifyType.DANGER);		
						}
					}
					
				});
			}
		});
		
		deactivateButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				sensorSummary.setModificationTime(JSUtil.getUnixtime());
				sensorSummary.setModificationTimeZone(JSUtil.getTimezoneOffset());
				sensorSummary.setLifecycle(0);//0=deactive
				service.changeLifecycle(sensorSummary, new AsyncCallback<Response>() {

					@Override
					public void onFailure(Throwable caught) {
						Notify.notify("Service not available. Please try later.", NotifyType.DANGER);		
					}

					@Override
					public void onSuccess(Response result) {
						if (result.getCode()==0) {
							activateButton.setEnabled(true);
							deactivateButton.setEnabled(false);
							activateButton.setType(ButtonType.PRIMARY);
							deactivateButton.setType(ButtonType.DEFAULT);
							Notify.notify("The sensor configuration is not active anymore.", NotifyType.SUCCESS);		
						}else{
							Notify.notify("Service not available. Please try later.", NotifyType.DANGER);		
						}
					}
					
				});
			}
		});
		
		
		
		deleteButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				sensorSummary.setModificationTime(JSUtil.getUnixtime());
				sensorSummary.setModificationTimeZone(JSUtil.getTimezoneOffset());
				sensorSummary.setLifecycle(-1);//-1=delete
				
				
				service.changeLifecycle(sensorSummary, new AsyncCallback<Response>() {

					@Override
					public void onFailure(Throwable caught) {
						Notify.notify("Service not available. Please try later.", NotifyType.DANGER);		
					}

					@Override
					public void onSuccess(Response result) {
						if (result.getCode()==0) {
							removeFromParent();
							Notify.notify("The sensor configuration has been deleted successfully", NotifyType.SUCCESS);		
						}else{
							Notify.notify("Service not available. Please try later.", NotifyType.DANGER);									
						}
					}
					
				});
			}
		});
		
		
		
	}

}

