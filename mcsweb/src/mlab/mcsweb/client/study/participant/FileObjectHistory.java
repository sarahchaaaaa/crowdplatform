package mlab.mcsweb.client.study.participant;

import java.util.ArrayList;
import java.util.Iterator;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Br;
import org.gwtbootstrap3.client.ui.html.Div;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
import com.googlecode.gwt.charts.client.table.Table;
import com.googlecode.gwt.charts.client.table.TableOptions;
import com.googlecode.gwt.charts.client.table.TablePage;
import com.googlecode.gwt.charts.client.table.TableSort;

import mlab.mcsweb.client.services.ParticipantService;
import mlab.mcsweb.client.services.ParticipantServiceAsync;
import mlab.mcsweb.shared.FileObjectInfo;

public class FileObjectHistory extends Composite {
	
	@UiField
	HTMLPanel listPanel;
	
	@UiField
	TextBox textEmail, textIdentifier;
	
	@UiField
	Button searchButton;
	
	private long studyId;
	Div listDiv;

	private Boolean tableLoaded = false;

	private ArrayList<FileObjectInfo> objectList = new ArrayList<>();
	Table objectTable = new Table();
	DataView dataView  = null;
	private final ParticipantServiceAsync service = GWT.create(ParticipantService.class);
	private static FileObjectHistoryUiBinder uiBinder = GWT.create(FileObjectHistoryUiBinder.class);

	interface FileObjectHistoryUiBinder extends UiBinder<Widget, FileObjectHistory> {
	}

	public FileObjectHistory(long studyId) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.studyId = studyId;
		
		listDiv = new Div();
		listDiv.setId("object_list_div");

		listPanel.add(listDiv);
		listPanel.add(new Br());
	}

	
	@UiHandler("searchButton")
	void getPingHistory(ClickEvent event){
		try {
			String email = textEmail.getText().trim();
			String identifier = textIdentifier.getText().trim();
			
			service.getObjectHistory(studyId, email, identifier, new AsyncCallback<ArrayList<FileObjectInfo>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ArrayList<FileObjectInfo> result) {
					objectList = result;
					listDiv.add(objectTable);
					drawPingTable();										
				}
				
			});
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void drawPingTable() {
		if(objectList.size() == 0){
			objectTable.setVisible(false);
		} else {
			objectTable.setVisible(true);			
			dataView = DataView.create(getDataTableFromList());
			//dataView.hideColumns(new int[]{1});
			objectTable.draw(dataView, getTableOptions());			
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
		dataTable.addColumn(ColumnType.STRING, "File Name");
		dataTable.addColumn(ColumnType.STRING, "Ready to Upload");
		dataTable.addColumn(ColumnType.STRING, "Upload Time");
		dataTable.addColumn(ColumnType.STRING, "Delete Time");

		dataTable.addRows(objectList.size());
		int columnIndex = 0;
		int rowIndex = 0;
		Iterator<FileObjectInfo> it = objectList.iterator();
		while (it.hasNext()) {
			FileObjectInfo objectInfo = (FileObjectInfo) it.next();
			dataTable.setValue(rowIndex, columnIndex++, objectInfo.getName());
			dataTable.setValue(rowIndex, columnIndex++, objectInfo.getReadyToUploadTime());
			dataTable.setValue(rowIndex, columnIndex++, objectInfo.getUploadTime());
			dataTable.setValue(rowIndex, columnIndex++, objectInfo.getDeleteTime());
			
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
//					if (selections.get(i).isRow()) {
						String id = dataTable.getFormattedValue(
								selections.get(i).getRow(), 1);
						message += (id + " ");
					}

//				}
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
