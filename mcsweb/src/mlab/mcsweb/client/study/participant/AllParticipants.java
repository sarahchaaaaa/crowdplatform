package mlab.mcsweb.client.study.participant;

import java.lang.reflect.Array;
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
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.DataView;
import com.googlecode.gwt.charts.client.Selection;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import com.googlecode.gwt.charts.client.options.Options;
//import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
//import com.google.gwt.visualization.client.DataTable;
//import com.google.gwt.visualization.client.DataView;
//import com.google.gwt.visualization.client.Selection;
//import com.google.gwt.visualization.client.events.SelectHandler;
//import com.google.gwt.visualization.client.visualizations.Table;
//import com.google.gwt.visualization.client.visualizations.Table.Options;
//import com.google.gwt.visualization.client.visualizations.Table.Options.Policy;
import com.googlecode.gwt.charts.client.table.Table;
import com.googlecode.gwt.charts.client.table.TableOptions;
import com.googlecode.gwt.charts.client.table.TablePage;
import com.googlecode.gwt.charts.client.table.TableSort;

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
		ArrayList<MyPair<String, String>> checkedUsers = getCheckedUsers();
		if (checkedUsers.size() == 0) {
			Notify.notify("Select a participant to edit", NotifyType.WARNING);
		} else if(checkedUsers.size() > 1){
			Notify.notify("Select a participant to edit", NotifyType.WARNING);
		} else {
			Participant editParticipant = getCheckedParticipant(checkedUsers.get(0).left, checkedUsers.get(0).right);
			subPanel.add(new ParticipantUnit(this, editParticipant));
		}
	}
	
	@UiHandler("buttonDelete")
	void deleteParticipants(ClickEvent event){
		// for instant let's say a string appended with |
		String list = "";
		// get id's of selected list
		ArrayList<MyPair<String, String>> checkedUsers = getCheckedUsers();
		if (checkedUsers.size() == 0) {
			Notify.notify("Select a participant to delete", NotifyType.WARNING);
		} else {
			ArrayList<Participant> participantsToDelete = new ArrayList<>();
			for (int i = 0; i < checkedUsers.size(); i++) {
				list += (checkedUsers.get(i) + "|");
				Participant toRemove = getCheckedParticipant(checkedUsers.get(0).left, checkedUsers.get(0).right);
				participantList.remove(toRemove);
				participantsToDelete.add(toRemove);
			}
			list = list.substring(0, list.length() - 1);
//			Window.alert("checked users "+ list);
			service.deleteParticipants(participantsToDelete, new AsyncCallback<Response>() {
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
					Window.alert("Service failure, please try later");
				}
			});

		}

	}
	
	private ArrayList<MyPair<String, String>> getCheckedUsers() {
		NodeList<Element> nodelist = querySelector("[id^=allcheck]");
		ArrayList<MyPair<String, String>> checkedUsers = new ArrayList<>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			boolean checked = nodelist.getItem(i).getPropertyBoolean("checked");
			if (checked) {
				String [] splits = nodelist.getItem(i).getId().split("--");
				MyPair<String, String> myPair = new MyPair<String, String>(splits[1], splits[2]==null?"":splits[2]);
				checkedUsers.add(myPair);
			}
		}
		return checkedUsers;
	}
	
	private Participant getCheckedParticipant(String email, String identifier){
		for(int i=0;i<participantList.size();i++){
			if(email.equalsIgnoreCase(participantList.get(i).getUserEmail()) && identifier.equalsIgnoreCase(participantList.get(i).getIdentifier())){
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

	public native void getColumnSelection(String checkboxId)/*-{
															
	}-*/;

	public final native NodeList<Element> querySelector(String selector)/*-{
		return $wnd.document.querySelectorAll(selector);
	}-*/;

	public static final native JsArrayString split(String string, String separator) /*-{
		return string.split(separator);
	}-*/;

}


class MyPair<T1, T2>{
	T1 left;
	T2 right;
	
	public MyPair(T1 left, T2 right){
		this.left = left;
		this.right = right;
	}
	
	public T1 getLeft() {
		return left;
	}
	public T2 getRight() {
		return right;
	}
}

