package mlab.mcsweb.client.study.dashboard;

import java.util.ArrayList;
import java.util.Iterator;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Br;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.DataView;
import com.googlecode.gwt.charts.client.Selection;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import com.googlecode.gwt.charts.client.options.Options;
import com.googlecode.gwt.charts.client.table.Table;
import com.googlecode.gwt.charts.client.table.TableOptions;
import com.googlecode.gwt.charts.client.table.TablePage;
import com.googlecode.gwt.charts.client.table.TableSort;

import mlab.mcsweb.client.services.DashboardService;
import mlab.mcsweb.client.services.DashboardServiceAsync;
import mlab.mcsweb.client.services.ParticipantService;
import mlab.mcsweb.client.services.ParticipantServiceAsync;
import mlab.mcsweb.client.study.participant.CustomFileReader;
import mlab.mcsweb.client.study.participant.ParticipantUnit;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.Response;

public class SendNotification extends Composite {
	
	@UiField
	Button testButton, buttonSubmit, buttonSelect, buttonDeselect;
	
	@UiField
	HTMLPanel listPanel;
	
	@UiField
	Label errorLabel;
	
	Div listDiv;
	private long studyId;
	
	private String currentTitle;
	private String currentNotif;
	
	private ArrayList<Participant> participantList = new ArrayList<>();
	Table participantTable = new Table();
	DataView dataView  = null;
	
	private Boolean tableLoaded = false;
	
	private final DashboardServiceAsync service = GWT.create(DashboardService.class);

	private final ParticipantServiceAsync partiService = GWT.create(ParticipantService.class);
	
	private static SendNotificationUiBinder uiBinder = GWT.create(SendNotificationUiBinder.class);

	interface SendNotificationUiBinder extends UiBinder<Widget, SendNotification> {
	}

	public SendNotification(long studyId) {
		initWidget(uiBinder.createAndBindUi(this));
		this.studyId = studyId;
		currentTitle = "";
		currentNotif = "";
		errorLabel.setText("");
		try {
			listDiv = new Div();
			listDiv.setId("all_list_div");
			
		} catch (Exception e) {
			// TODO: handle exception
			Window.alert("exception in constructor" + e.getMessage());
		}
	}

	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		if(!tableLoaded){
			listPanel.add(listDiv);
			listPanel.add(new Br());
			partiService.getAllParticipants(this.studyId, new AsyncCallback<ArrayList<Participant>>() {
				@Override
				public void onSuccess(ArrayList<Participant> result) {
					participantList = result;
					listDiv.add(participantTable);
					drawParticipantsTable();
					tableLoaded = true;
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
			//listPanel.add(new CustomFileReader());
		}

	}
	
	protected void reloadTable() {
		partiService.getAllParticipants(this.studyId, new AsyncCallback<ArrayList<Participant>>() {
			@Override
			public void onSuccess(ArrayList<Participant> result) {
				// TODO Auto-generated method stub
				participantList = result;
				listDiv.add(participantTable);
				drawParticipantsTable();
				tableLoaded = true;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	public void drawParticipantsTable() {
		if(participantList.size() == 0){
			participantTable.setVisible(false);
//			buttonEdit.setVisible(false);
//			buttonDelete.setVisible(false);
		} else {
			participantTable.setVisible(true);			
//			buttonEdit.setVisible(true);
//			buttonDelete.setVisible(true);
			dataView = DataView.create(getDataTableFromList());
			//dataView.hideColumns(new int[]{1});
			participantTable.draw(dataView, getTableOptions());			
		}
	}
	
	protected DataTable getDataTableFromList() {
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "");
		dataTable.addColumn(ColumnType.STRING, "Email");
		dataTable.addColumn(ColumnType.STRING, "Identifier");
		dataTable.addColumn(ColumnType.STRING, "First Name");
		dataTable.addColumn(ColumnType.STRING, "Last Name");
//		dataTable.addColumn(ColumnType.STRING, "Organization");
		dataTable.addColumn(ColumnType.STRING, "Status");

		dataTable.addRows(participantList.size());
		int columnIndex = 0;
		int rowIndex = 0;
		Iterator<Participant> it = participantList.iterator();
		while (it.hasNext()) {
			Participant participant = (Participant) it.next();
			String checkbox = "<input type=\"checkbox\" text-align=\"center\"id=\"allcheck--"
					+ participant.getUserEmail() + "--" + participant.getIdentifier() + "\">";
			dataTable.setValue(rowIndex, columnIndex++, checkbox);
			dataTable.setValue(rowIndex, columnIndex++, participant.getUserEmail());
			dataTable.setValue(rowIndex, columnIndex++, participant.getIdentifier());
			dataTable.setValue(rowIndex, columnIndex++, participant.getFirstName());
			dataTable.setValue(rowIndex, columnIndex++, participant.getLastName());
//			dataTable.setValue(rowIndex, columnIndex++, participant.getOrganization());
			dataTable.setValue(rowIndex, columnIndex++, participant.getStatus());

			rowIndex += 1;
			columnIndex = 0;
		}
		return dataTable;
	}
	
	protected TableOptions getTableOptions() {
		TableOptions options = (TableOptions) Options.create();
	
		options.setAllowHtml(true);
		options.setSort(TableSort.ENABLE);
		options.setPage(TablePage.ENABLE);
		options.setPageSize(100);
		options.setStartPage(0);
		options.setWidth(getParent().getOffsetWidth());

//		options.setSort(Policy.ENABLE);
//		options.setPage(Policy.ENABLE);
//		//options.setWidth(width)
//		//options.setStartPage(0);
//		//options.setWidth("100%");
//		//options.setHeight("100%");
//		options.setOption("startPage", 0);
//		options.setOption("width", "100%");
		return options;
	}
	
	private SelectHandler createSelectHandler(final Table table, final DataTable dataTable) {
		return new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				String message = "";
				JsArray<Selection> selections = table.getSelection();
				for (int i = 0; i < selections.length(); i++) {
//					if (selections.get(i).) {
						String id = dataTable.getFormattedValue(
								selections.get(i).getRow(), 1);
						message += (id + " ");
//					}

				}

			}
		};
	}
	
	private Participant getCheckedParticipant(String email, String identifier){
		for(int i=0;i<participantList.size();i++){
			if(email.equalsIgnoreCase(participantList.get(i).getUserEmail()) && identifier.equalsIgnoreCase(participantList.get(i).getIdentifier())){
				return participantList.get(i);
			}
		}
		return null;
	}
	
	private ArrayList<Participant> getCheckedUsers() {
		String list = "";
		NodeList<Element> nodelist = querySelector("[id^=allcheck]");
		ArrayList<MyPair<String, String>> checkedUsers = new ArrayList<>();
		ArrayList<Participant> participants = new ArrayList<>(); 
		for (int i = 0; i < nodelist.getLength(); i++) {
			boolean checked = nodelist.getItem(i).getPropertyBoolean("checked");
			if (checked) {
				String [] splits = nodelist.getItem(i).getId().split("--");
				MyPair<String, String> myPair = new MyPair<String, String>(splits[1], splits[2]==null?"":splits[2]);
				checkedUsers.add(myPair);
			}
		}
		if (checkedUsers.size() == 0) {
			Notify.notify("Select a participant to send a notification to", NotifyType.WARNING);
		} else {
			for (int i = 0; i < checkedUsers.size(); i++) {
				list += (checkedUsers.get(i) + "|");
				Participant toAdd = getCheckedParticipant(checkedUsers.get(0).left, checkedUsers.get(0).right);
				participants.add(toAdd);
			}
		}
		return participants;
	}
	
	
	@UiHandler("buttonSelect")
	void selectAll(ClickEvent event){
		NodeList<Element> nodelist = querySelector("[id^=allcheck]");
		ArrayList<MyPair<String, String>> checkedUsers = new ArrayList<>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			nodelist.getItem(i).setPropertyBoolean("checked", true);
		}
	}
	
	@UiHandler("buttonDeselect")
	void deselectAll(ClickEvent event){
		NodeList<Element> nodelist = querySelector("[id^=allcheck]");
		ArrayList<MyPair<String, String>> checkedUsers = new ArrayList<>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			nodelist.getItem(i).setPropertyBoolean("checked", false);
		}
	}

	@UiHandler("buttonSubmit")
	void submitNotif(ClickEvent event){
		
		ArrayList<Participant> participants = new ArrayList<>(); 
		participants = getCheckedUsers();
		
		service.sendNotification(participants, currentTitle, currentNotif, new AsyncCallback<Response>() {
			@Override
			public void onSuccess(Response result) {
				if (result.getCode() == 0) {
					// do something here
					Notify.notify("Test service call successful", NotifyType.SUCCESS);
//					allParticipants.reloadTable();
//					removeFromParent();
				} else {
					errorLabel.setText("Service not available, please try later.");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText("Service not available, please try later.");
			}
			});
		
	}
	
	@UiHandler("buttonCancel")
	void cancelAction(ClickEvent event) {
		errorLabel.setText("");
		currentTitle = "";
		currentNotif = "";
//		this.removeFromParent();
	}
	
	@UiHandler("testButton")
	void testService(ClickEvent event){
		ArrayList<Participant> participants = new ArrayList<>();
		service.sendNotification(participants, "Test Title", "Test Message", new AsyncCallback<Response>() {
			
			@Override
			public void onSuccess(Response result) {
				if(result.getCode()==0){
					Notify.notify("Test service call successful", NotifyType.SUCCESS);
				}else{
					Notify.notify("Service is not available, please try later.", NotifyType.DANGER);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Notify.notify("Service is not available, please try later.", NotifyType.DANGER);
			}
		});
	}
	public final native NodeList<Element> querySelector(String selector)/*-{
	return $wnd.document.querySelectorAll(selector);
}-*/;
}
