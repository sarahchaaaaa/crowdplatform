package mlab.mcsweb.client.study;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;

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

import mlab.mcsweb.client.GreetingService;
import mlab.mcsweb.client.GreetingServiceAsync;
import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.events.SensorEvent;
import mlab.mcsweb.client.events.SensorState;
import mlab.mcsweb.client.events.SurveyEvent;
import mlab.mcsweb.client.events.SurveyState;
import mlab.mcsweb.client.events.SensorState.SensorSpecificState;
import mlab.mcsweb.client.events.SurveyState.SurveySpecificState;
import mlab.mcsweb.shared.SensorSummary;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.SurveySummary;

public class SensorManagement extends Composite {
	
	@UiField
	Column listColumn;
	
	@UiField
	Button buttonAdd;
		
	
	private boolean isLoaded = false;
	private ArrayList<SensorOverview> sensorConfigList = new ArrayList<>();
	
	private Study study;

	//private final StudyConfigurationServiceAsync studyConfigService = GWT.create(StudyConfigurationService.class);
	private final GreetingServiceAsync service = GWT.create(GreetingService.class);


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
						sensorConfigList.add(new SensorOverview(result.get(i)));
						//Window.alert("survey id :"+ result.get(i).getId() + " study id:"+ result.get(i).getStudyId());

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
			
			/*service.getSensorConfigList(study.getId(), new AsyncCallback<ArrayList<SensorSummary>>() {
				
				@Override
				public void onSuccess(ArrayList<SensorSummary> result) {
					// TODO Auto-generated method stub
					for(int i=0;i<result.size();i++){
						sensorConfigList.add(new SensorOverview(result.get(i)));
						//Window.alert("survey id :"+ result.get(i).getId() + " study id:"+ result.get(i).getStudyId());

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
			});*/
						
			
			isLoaded = true;
		}

	}

}

class SensorOverview extends BaseOverview{

	SensorSummary sensorSummary;
	
	public SensorOverview(final SensorSummary sensorSummary) {
		
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
	
		super.setOverviewPanel(name, description);
		addClickAction();
		
	}
	
	@Override
	void addClickAction() {
		// TODO Auto-generated method stub
		HTMLPanel htmlPanel = super.getOverviewPanel();
		htmlPanel.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Mcsweb.getEventBus().fireEvent(new SensorEvent(new SensorState(sensorSummary, SensorSpecificState.NEW)));
			}
		}, ClickEvent.getType());

	}

}

