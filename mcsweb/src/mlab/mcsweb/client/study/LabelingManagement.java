package mlab.mcsweb.client.study;

import org.gwtbootstrap3.client.ui.LinkedGroupItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.events.LabelEvent;
import mlab.mcsweb.client.events.LabelState;
import mlab.mcsweb.client.events.LabelState.LabelSpecificState;
import mlab.mcsweb.client.services.GreetingService;
import mlab.mcsweb.client.services.GreetingServiceAsync;
import mlab.mcsweb.client.study.labeling.LabelHistory;
import mlab.mcsweb.client.study.labeling.LabelingList;
import mlab.mcsweb.shared.LabelingSummary;
import mlab.mcsweb.shared.Study;

public class LabelingManagement extends Composite {

	@UiField
	HTMLPanel contentPanel;
	
	@UiField
	LinkedGroupItem historyLink, listLink;

	
	private Study study;
	private LabelHistory labelHistory;
	private LabelingList labelingList;

	
	private final GreetingServiceAsync service = GWT.create(GreetingService.class);

	private static LabellingManagementUiBinder uiBinder = GWT.create(LabellingManagementUiBinder.class);

	interface LabellingManagementUiBinder extends UiBinder<Widget, LabelingManagement> {
	}

	public LabelingManagement(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		getHistory(null);
	}

	@UiHandler("historyLink")
	void getHistory(ClickEvent event){
		historyLink.setActive(true);
		listLink.setActive(false);
		
		contentPanel.clear();
		if (labelHistory == null) {
			labelHistory = new LabelHistory(this.study.getId());
		}
		contentPanel.add(labelHistory);
	}
	
	@UiHandler("listLink")
	void getList(ClickEvent event){
		historyLink.setActive(false);
		listLink.setActive(true);
		
		contentPanel.clear();
		if (labelingList==null) {
			labelingList = new LabelingList();
		}
		contentPanel.add(labelingList);
	}
}


class labelingOverview extends BaseOverview{

	LabelingSummary labelingSummary;
	
	public labelingOverview(final LabelingSummary labelingSummary) {
		
		this.labelingSummary = labelingSummary;
		
		String name = labelingSummary.getName();
		String modificationTime = labelingSummary.getModificationTime();
		String modificationTimeZone = labelingSummary.getModificationTimeZone();
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
				Mcsweb.getEventBus().fireEvent(new LabelEvent(new LabelState(labelingSummary, LabelSpecificState.NEW)));
			}
		}, ClickEvent.getType());

	}

}


