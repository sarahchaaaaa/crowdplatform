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
import mlab.mcsweb.shared.PingInfo;

public class IndividualParticipant extends Composite {
	@UiField
	HTMLPanel pinglistPanel;
	
	@UiField
	TextBox textEmail, textDays;
	
	@UiField
	Button searchButton;
	
	private long studyId;
	Div listDiv;


	private Boolean tableLoaded = false;

	private ArrayList<PingInfo> pingList = new ArrayList<>();
	Table pingTable = new Table();
	DataView dataView  = null;
	private final ParticipantServiceAsync service = GWT.create(ParticipantService.class);

	private static IndividualParticipantUiBinder uiBinder = GWT.create(IndividualParticipantUiBinder.class);

	interface IndividualParticipantUiBinder extends UiBinder<Widget, IndividualParticipant> {
	}

	public IndividualParticipant(long studyId) {
		initWidget(uiBinder.createAndBindUi(this));
		this.studyId = studyId;
		
		listDiv = new Div();
		listDiv.setId("ping_list_div");

		pinglistPanel.add(listDiv);
		pinglistPanel.add(new Br());
	}
	
	@UiHandler("searchButton")
	void getPingHistory(ClickEvent event){
		try {
			String email = textEmail.getText().trim();
			int days = Integer.parseInt(textDays.getText().trim());
			service.getPingHistory(studyId, email, days, new AsyncCallback<ArrayList<PingInfo>>() {
				
				@Override
				public void onSuccess(ArrayList<PingInfo> result) {
					pingList = result;
					listDiv.add(pingTable);
					drawPingTable();					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void drawPingTable() {
		if(pingList.size() == 0){
			pingTable.setVisible(false);
		} else {
			pingTable.setVisible(true);			
			dataView = DataView.create(getDataTableFromList());
			//dataView.hideColumns(new int[]{1});
			pingTable.draw(dataView, getTableOptions());			
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
		dataTable.addColumn(ColumnType.STRING, "Tiemstamp");
		dataTable.addColumn(ColumnType.STRING, "Network");
		dataTable.addColumn(ColumnType.STRING, "OS Type");
		dataTable.addColumn(ColumnType.STRING, "OS Version");
		dataTable.addColumn(ColumnType.STRING, "App Version");
		dataTable.addColumn(ColumnType.STRING, "Sensor Data");

		dataTable.addRows(pingList.size());
		int columnIndex = 0;
		int rowIndex = 0;
		Iterator<PingInfo> it = pingList.iterator();
		while (it.hasNext()) {
			PingInfo pingInfo = (PingInfo) it.next();
			dataTable.setValue(rowIndex, columnIndex++, pingInfo.getTime());
			dataTable.setValue(rowIndex, columnIndex++, pingInfo.getNetwork());
			dataTable.setValue(rowIndex, columnIndex++, pingInfo.getOsType());
			dataTable.setValue(rowIndex, columnIndex++, pingInfo.getOsVersion());
			dataTable.setValue(rowIndex, columnIndex++, pingInfo.getAppVersion());
			dataTable.setValue(rowIndex, columnIndex++, pingInfo.getData());

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
				JsArray<Selection> selections = table.getSelections();
				for (int i = 0; i < selections.length(); i++) {
					if (selections.get(i).isRow()) {
						String id = dataTable.getFormattedValue(
								selections.get(i).getRow(), 1);
						message += (id + " ");
					}

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
