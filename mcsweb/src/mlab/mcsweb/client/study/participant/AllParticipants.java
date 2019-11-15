package mlab.mcsweb.client.study.participant;

import java.util.ArrayList;
import java.util.Iterator;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Br;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;
import com.google.gwt.visualization.client.visualizations.Table.Options.Policy;

import mlab.mcsweb.client.services.ParticipantService;
import mlab.mcsweb.client.services.ParticipantServiceAsync;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.Response;

public class AllParticipants extends Composite {
	
	
	@UiField
	HTMLPanel listPanel, subPanel;
	
	@UiField
	Button buttonAdd, buttonBatchImport, buttonEdit, buttonDelete;
	
	
	private long studyId;
	Div listDiv;


	private Boolean tableLoaded = false;

	private ArrayList<Participant> participantList = new ArrayList<>();
	Table participantTable = new Table();
	DataView dataView  = null;

	private final ParticipantServiceAsync service = GWT.create(ParticipantService.class);

	private static AllParticipantsUiBinder uiBinder = GWT.create(AllParticipantsUiBinder.class);

	interface AllParticipantsUiBinder extends UiBinder<Widget, AllParticipants> {
	}

	public AllParticipants(long studyId) {
		initWidget(uiBinder.createAndBindUi(this));
		this.studyId = studyId;
		try {
			listDiv = new Div();
			listDiv.setId("all_list_div");
			
			buttonAdd.setIcon(IconType.PLUS);
			buttonEdit.setIcon(IconType.EDIT);
			buttonDelete.setIcon(IconType.REMOVE);
			
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
			service.getAllParticipants(this.studyId, new AsyncCallback<ArrayList<Participant>>() {
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
		service.getAllParticipants(this.studyId, new AsyncCallback<ArrayList<Participant>>() {
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
	
	@UiHandler("buttonAdd")
	void addParticipant(ClickEvent event){
		subPanel.clear();
		subPanel.add(new ParticipantUnit(this, this.studyId));
	}
	
	@UiHandler("buttonBatchImport")
	void importFile(ClickEvent event){
		subPanel.clear();
		subPanel.add(new CustomFileReader());
	}
	
	@UiHandler("buttonEdit")
	void editParticipant(ClickEvent event){
		subPanel.clear();
		ArrayList<String> checkedUsers = getCheckedUsers();
		if (checkedUsers.size() == 0) {
			Notify.notify("Select a participant to edit", NotifyType.WARNING);
		} else if(checkedUsers.size() > 1){
			Notify.notify("Select a participant to edit", NotifyType.WARNING);
		} else {
			Participant editParticipant = getCheckedParticipant(checkedUsers.get(0));
			subPanel.add(new ParticipantUnit(this, editParticipant));
		}
	}
	
	@UiHandler("buttonDelete")
	void deleteParticipants(ClickEvent event){
		// for instant let's say a string appended with |
		String list = "";
		// get id's of selected list
		ArrayList<String> checkedUsers = getCheckedUsers();
		if (checkedUsers.size() == 0) {
			Notify.notify("Select a participant to delete", NotifyType.WARNING);
		} else {
			for (int i = 0; i < checkedUsers.size(); i++) {
				list += (checkedUsers.get(i) + "|");
				Participant toRemove = getCheckedParticipant(checkedUsers.get(i));
				participantList.remove(toRemove);
			}
			list = list.substring(0, list.length() - 1);
//			Window.alert("checked users "+ list);
			service.deleteParticipants(studyId, list, new AsyncCallback<Response>() {
				@Override
				public void onSuccess(Response result) {
					if(result.getCode() == 0){
						//drawParticipantsTable();
						//TODO: temporary, need to think what will happen if update fails
					}
					drawParticipantsTable();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});

		}

	}
	
	private ArrayList<String> getCheckedUsers() {
		NodeList<Element> nodelist = querySelector("[id^=allcheck]");
		ArrayList<String> checkedUsers = new ArrayList<>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			boolean checked = nodelist.getItem(i).getPropertyBoolean("checked");
			if (checked) {
				checkedUsers.add(nodelist.getItem(i).getId().split("-")[1]);
			}
		}
		return checkedUsers;
	}
	
	private Participant getCheckedParticipant(String email){
		for(int i=0;i<participantList.size();i++){
			if(email.equalsIgnoreCase(participantList.get(i).getUserEmail())){
				return participantList.get(i);
			}
		}
		return null;
	}
	
	

	
	public void drawParticipantsTable() {
		if(participantList.size() == 0){
			participantTable.setVisible(false);
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
		} else {
			participantTable.setVisible(true);			
			buttonEdit.setVisible(true);
			buttonDelete.setVisible(true);
			dataView = DataView.create(getDataTableFromList());
			//dataView.hideColumns(new int[]{1});
			participantTable.draw(dataView, getTableOptions());			
		}
	}

	protected Options getTableOptions() {
		Options options = Options.create();
		options.setAllowHtml(true);
		options.setSort(Policy.ENABLE);
		options.setPage(Policy.ENABLE);
		options.setPageSize(100);
		//options.setStartPage(0);
		//options.setWidth("100%");
		//options.setHeight("100%");
		options.setOption("startPage", 0);
		options.setOption("width", "100%");
		return options;
	}

	protected DataTable getDataTableFromList() {
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "");
		dataTable.addColumn(ColumnType.STRING, "Email");
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
			String checkbox = "<input type=\"checkbox\" text-align=\"center\"id=\"allcheck-"
					+ participant.getUserEmail() + "\">";
			dataTable.setValue(rowIndex, columnIndex++, checkbox);
			dataTable.setValue(rowIndex, columnIndex++, participant.getUserEmail());
			dataTable.setValue(rowIndex, columnIndex++, participant.getFirstName());
			dataTable.setValue(rowIndex, columnIndex++, participant.getLastName());
//			dataTable.setValue(rowIndex, columnIndex++, participant.getOrganization());
			dataTable.setValue(rowIndex, columnIndex++, participant.getStatus());

			rowIndex += 1;
			columnIndex = 0;
		}
		return dataTable;
	}

	private SelectHandler createSelectHandler(final Table table, final DataTable dataTable) {
		return new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				// TODO Auto-generated method stub
				// Window.alert("selected....");
				// getColumnSelection();
				String message = "";
				JsArray<Selection> selections = table.getSelections();
				for (int i = 0; i < selections.length(); i++) {
					if (selections.get(i).isRow()) {
						String id = dataTable.getFormattedValue(
								selections.get(i).getRow(), 1);
						// Window.alert("row with id " + id +
						// "has been selected:");
						message += (id + " ");
					}

				}
				// Window.alert("message: "+ message);
				/*
				 * if(selections.length() == 1){ Selection selection =
				 * selections.get(0); if(selection.isRow()){ String id =
				 * dataTable.getFormattedValue(selection.getRow(), 1);
				 * //Window.alert("row with id " + id + "has been selected:");
				 * 
				 * 
				 * } }
				 */

			}
		};
	}

	public native void getColumnSelection(String checkboxId)/*-{
															
	}-*/;

	public final native NodeList<Element> querySelector(String selector)/*-{
		return $wnd.document.querySelectorAll(selector);
	}-*/;

	public static final native JsArrayString split(String string, String separator) /*-{
		return string.split(separator);
	}-*/;

}
