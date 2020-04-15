package mlab.mcsweb.client.study.dashboard;

import java.util.ArrayList;
import java.util.Iterator;

import org.gwtbootstrap3.client.ui.html.Br;
import org.gwtbootstrap3.client.ui.html.Div;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ColumnType;
//import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
//import com.google.gwt.visualization.client.DataTable;
//import com.google.gwt.visualization.client.DataView;
//import com.google.gwt.visualization.client.Selection;
//import com.google.gwt.visualization.client.events.SelectHandler;
//import com.google.gwt.visualization.client.visualizations.Table;
//import com.google.gwt.visualization.client.visualizations.Table.Options;
//import com.google.gwt.visualization.client.visualizations.Table.Options.Policy;
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
import mlab.mcsweb.shared.FileIdentifier;

public class EmailUUIDMappingList extends Composite {

	@UiField
	HTMLPanel listPanel;
	
	private long studyId;
	Div listDiv;

	private Boolean tableLoaded = false;
	private ArrayList<FileIdentifier> list = new ArrayList<>();
	Table listTable = new Table();
	DataView dataView  = null;
	
	private final DashboardServiceAsync service = GWT.create(DashboardService.class);


	private static EmailUUIDMappingListUiBinder uiBinder = GWT.create(EmailUUIDMappingListUiBinder.class);

	interface EmailUUIDMappingListUiBinder extends UiBinder<Widget, EmailUUIDMappingList> {
	}

	public EmailUUIDMappingList(long studyId) {
		initWidget(uiBinder.createAndBindUi(this));
		this.studyId = studyId;
		try {
			listDiv = new Div();
			listDiv.setId("file_map_list_div");
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		if(!tableLoaded){
			listPanel.add(listDiv);
			listPanel.add(new Br());
			service.getFileIdentifiers(studyId, new AsyncCallback<ArrayList<FileIdentifier>>() {
				
				@Override
				public void onSuccess(ArrayList<FileIdentifier> result) {
					list = result;
					listDiv.add(listTable);
					drawListTable();
					tableLoaded = true;
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

	
	

	
	public void drawListTable() {
		if(list.size() == 0){
			listTable.setVisible(false);
		} else {
			listTable.setVisible(true);			
			dataView = DataView.create(getDataTableFromList());
			//dataView.hideColumns(new int[]{1});
			listTable.draw(dataView, getTableOptions());			
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
		dataTable.addColumn(ColumnType.STRING, "File Identifier");

		dataTable.addRows(list.size());
		int columnIndex = 0;
		int rowIndex = 0;
		Iterator<FileIdentifier> it = list.iterator();
		while (it.hasNext()) {
			FileIdentifier identifier = (FileIdentifier) it.next();
			dataTable.setValue(rowIndex, columnIndex++, String.valueOf(rowIndex + 1));
			dataTable.setValue(rowIndex, columnIndex++, identifier.getEmail());
			dataTable.setValue(rowIndex, columnIndex++, identifier.getUuid());

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
