package mlab.mcsweb.client.study.participant;

import java.util.ArrayList;
import java.util.Date;
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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.DataView;
import com.googlecode.gwt.charts.client.Selection;
import com.googlecode.gwt.charts.client.corechart.LineChart;
import com.googlecode.gwt.charts.client.corechart.LineChartOptions;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import com.googlecode.gwt.charts.client.options.AxisTitlesPosition;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.Options;
import com.googlecode.gwt.charts.client.options.PointShapeType;
import com.googlecode.gwt.charts.client.options.VAxis;
import com.googlecode.gwt.charts.client.table.Table;
//import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
//import com.google.gwt.visualization.client.DataTable;
//import com.google.gwt.visualization.client.DataView;
//import com.google.gwt.visualization.client.Selection;
//import com.google.gwt.visualization.client.events.SelectHandler;
//import com.google.gwt.visualization.client.visualizations.Table;
//import com.google.gwt.visualization.client.visualizations.Table.Options;
//import com.google.gwt.visualization.client.visualizations.Table.Options.Policy;
import com.googlecode.gwt.charts.client.table.TableOptions;
import com.googlecode.gwt.charts.client.table.TablePage;
import com.googlecode.gwt.charts.client.table.TableSort;

import mlab.mcsweb.client.services.ParticipantService;
import mlab.mcsweb.client.services.ParticipantServiceAsync;
import mlab.mcsweb.shared.DaywiseCount;
import mlab.mcsweb.shared.PingInfo;

public class IndividualParticipant extends Composite {
	@UiField
	HTMLPanel pinglistPanel, graphPanel;
	
	@UiField
	TextBox textEmail, textIdentifier, textDays;
	
	@UiField
	Button searchButton;
	
	private long studyId;
	Div listDiv;


	private Boolean tableLoaded = false;

	private ArrayList<PingInfo> pingList = new ArrayList<>();
	Table pingTable = new Table();
	DataView dataView  = null;
	private LineChart pingChart;
	static final int DEFAULT_DAYS = 15;
	ChartLoader chartLoader;
	
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
		
		chartLoader = new ChartLoader(ChartPackage.CORECHART);
	}
	
	void loadPingGraph(String email, String identifier, final int days) {

		service.getDaywisePingHistory(studyId, email, identifier, days, new AsyncCallback<ArrayList<DaywiseCount>>() {

			@Override
			public void onFailure(Throwable caught) {
//				Window.alert("on failure");
			}

			@Override
			public void onSuccess(ArrayList<DaywiseCount> result) {
//				Window.alert("on success: "+ result.size());
				DataTable data = DataTable.create();
				data.addColumn(ColumnType.STRING, "Date");
				data.addColumn(ColumnType.NUMBER, "Count");

//				ArrayList<MyPair<String, Integer>> dailyMap = new ArrayList<>();
				DateTimeFormat fm = DateTimeFormat.getFormat("MMM dd");
				Date today = new Date();
//				CalendarUtil.addDaysToDate(today, -DEFAULT_DAYS+1);
				CalendarUtil.addDaysToDate(today, -days+1);

//				for (int i = 0; i < DEFAULT_DAYS; i++) {
				for (int i = 0; i < days; i++) {
					int pingCount = 0;// (int) (Math.random() * (1000 - 0) + 0);
					String stringDate = fm.format(today);
					for (int j = 0; j < result.size(); j++) {
//						Window.alert("ping count:" + result.get(j).getCount() + ", date:" + stringDate);
						if (result.get(j).getDate().equalsIgnoreCase(stringDate)) {
							// data.addRow(stringDate,
							// counts.get(i).getCount());
							pingCount = result.get(j).getCount();
							break;
						}
					}
					data.addRow(stringDate, pingCount);
					// Window.alert("date :" + fm.format(today) + "," +
					// stringDate+ "," + pingCount);
					CalendarUtil.addDaysToDate(today, 1);

				}

				// Set options
				LineChartOptions options = LineChartOptions.create();
				options.setTitle("Total Ping Per Day");
				options.setHAxis(HAxis.create("Date"));
				VAxis vAxis = VAxis.create("Total Ping");
				vAxis.setMinValue(0);
				vAxis.setMaxValue(25);
				// vAxis.se
				options.setVAxis(vAxis);
				options.setPointShape(PointShapeType.CIRCLE);
				options.setPointSize(4);
				options.setAxisTitlesPosition(AxisTitlesPosition.NONE);
				options.setLegend(Legend.create(LegendPosition.NONE));

				// Draw the chart
				pingChart.draw(data, options);
				pingChart.setWidth("100%");
				// pingChart.setHeight("400px");

			}

		});

	}

	
	@UiHandler("searchButton")
	void getPingHistory(ClickEvent event){
		try {
			final String email = textEmail.getText().trim();
			final String identifier = textIdentifier.getText().trim();
			final int days = Integer.parseInt(textDays.getText().trim());
			if (days > 0) {
				service.getPingHistory(studyId, email, identifier, days, new AsyncCallback<ArrayList<PingInfo>>() {
					
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
				
				chartLoader.loadApi(new Runnable() {
					public void run() {
						pingChart = new LineChart();
						graphPanel.clear();
						graphPanel.add(pingChart);

						loadPingGraph(email, identifier, days);
					}
				});
				
			}

			
			
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
