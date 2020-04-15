package mlab.mcsweb.client.study.settings;

import java.util.ArrayList;
import java.util.Iterator;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;
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
//import com.googlecode.gwt.charts.client.options.Options;
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

import mlab.mcsweb.client.services.SettingsService;
import mlab.mcsweb.client.services.SettingsServiceAsync;
import mlab.mcsweb.client.study.participant.CustomFileReader;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;


public class Collaborators extends Composite {
	
	@UiField
	HTMLPanel listPanel, subPanel;
	
	@UiField
	Button buttonBatchImport, buttonAdd, buttonEdit, buttonDelete;
	
	@UiField
	TextBox textEmail;
	
	private Study study;
	Div listDiv;
	
	private Boolean tableLoaded = false;
	
	private ArrayList<String> collaboratorList = new ArrayList<>();
	Table collaboratorTable = new Table();
	DataView dataView  = null;


	private final SettingsServiceAsync service = GWT.create(SettingsService.class);

	private static CollaboratorsUiBinder uiBinder = GWT.create(CollaboratorsUiBinder.class);

	interface CollaboratorsUiBinder extends UiBinder<Widget, Collaborators> {
	}

	public Collaborators(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		
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
		super.onLoad();

		if(!tableLoaded){
			listPanel.add(listDiv);
			listPanel.add(new Br());
			
			service.getAllCollaborators(this.study.getId(), new AsyncCallback<ArrayList<String>>() {
				@Override
				public void onSuccess(ArrayList<String> result) {
					collaboratorList = result;
					listDiv.add(collaboratorTable);
					drawCollaboratorTable();
					tableLoaded = true;
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					//Window.alert("Get list error......");
					
				}
			});

			//listPanel.add(new CustomFileReader());
		}

		
	}
	
	protected void reloadTable() {
		service.getAllCollaborators(this.study.getId(), new AsyncCallback<ArrayList<String>>() {
			@Override
			public void onSuccess(ArrayList<String> result) {
				// TODO Auto-generated method stub
				collaboratorList = result;
				listDiv.add(collaboratorTable);
				drawCollaboratorTable();
				tableLoaded = true;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("reloadTable onFailure");
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@UiHandler("buttonAdd")
	void addCollaborator(ClickEvent event){
		subPanel.clear();
		subPanel.add(new CollaboratorUnit(this, study));
		
	} 
	
	@UiHandler("buttonBatchImport")
	void importFile(ClickEvent event){
		subPanel.clear();
		subPanel.add(new CustomFileReader());
	} 
	
	@UiHandler("buttonEdit")
	void editCollaborator(ClickEvent event){
		subPanel.clear();
		ArrayList<String> checkedUsers = getCheckedUsers();
		if (checkedUsers.size() == 0) {
			Notify.notify("Select a collaborator to edit", NotifyType.WARNING);
		} else if(checkedUsers.size() > 1){
			Notify.notify("Select a collaborator to edit", NotifyType.WARNING);
		} else {
			//Not sure what to put here yet...
			String editCollaborator = getCheckedCollaborator(checkedUsers.get(0));
			subPanel.add(new CollaboratorUnit(this, study, editCollaborator));  //fill in method
		} 
	}
	
	
	@UiHandler("buttonDelete")
	void deleteCollaborators(ClickEvent event){
		// for instant let's say a string appended with |
		String list = "";
		// get id's of selected list
		ArrayList<String> checkedUsers = getCheckedUsers();
		if (checkedUsers.size() == 0) {
			Notify.notify("Select a collaborator to delete", NotifyType.WARNING);
		} else {
			for (int i = 0; i < checkedUsers.size(); i++) {
				list += (checkedUsers.get(i) + "|");
				String toRemove = getCheckedCollaborator(checkedUsers.get(i));
				collaboratorList.remove(toRemove);
			}
			list = list.substring(0, list.length() - 1);
//			Window.alert("checked users "+ list);
			service.deleteCollaborators(study.getId(), list, new AsyncCallback<Response>() {
				@Override
				public void onSuccess(Response result) {
					if(result.getCode() == 0){
						//drawParticipantsTable();
						//TODO: temporary, need to think what will happen if update fails
					}
					drawCollaboratorTable();
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
	
	private String getCheckedCollaborator(String email){
		for(int i=0;i<collaboratorList.size();i++){
			if(email.equalsIgnoreCase(collaboratorList.get(i))){
				return collaboratorList.get(i);
			}
		}
		return null;
	}
	
	
	public void drawCollaboratorTable() {
		if(collaboratorList.size() == 0){
			collaboratorTable.setVisible(false);
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
		} else {
			collaboratorTable.setVisible(true);			
			buttonEdit.setVisible(true);
			buttonDelete.setVisible(true);
			dataView = DataView.create(getDataTableFromList());
			//dataView.hideColumns(new int[]{1});
			collaboratorTable.draw(dataView, getTableOptions());			
		}
	}
	
	
	protected TableOptions getTableOptions() {
		TableOptions options = (TableOptions) Options.create();
	
		options.setAllowHtml(true);
		options.setPage(TablePage.ENABLE);
		options.setSort(TableSort.ENABLE);
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

		dataTable.addRows(collaboratorList.size());
		int columnIndex = 0;
		int rowIndex = 0;
		Iterator<String> it = collaboratorList.iterator();
		while (it.hasNext()) {
			String email = (String) it.next();
			String checkbox = "<input type=\"checkbox\" text-align=\"center\"id=\"allcheck-"
					+ email + "\">";
			dataTable.setValue(rowIndex, columnIndex++, checkbox);
			dataTable.setValue(rowIndex, columnIndex++, email);


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
