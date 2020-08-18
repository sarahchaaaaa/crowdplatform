package mlab.mcsweb.client.study.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Heading;
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
import com.googlecode.gwt.charts.client.table.TableOptions;
import com.googlecode.gwt.charts.client.table.TablePage;
import com.googlecode.gwt.charts.client.table.TableSort;

import mlab.mcsweb.client.services.DashboardService;
import mlab.mcsweb.client.services.DashboardServiceAsync;
import mlab.mcsweb.shared.DaywiseCount;
import mlab.mcsweb.shared.Participant;

public class StudySummary extends Composite {

	@UiField
	HTMLPanel pingListPanel, objectListPanel, labelListPanel, pingGraphPanel, uniquePingGraphPanel, objectGraphPanel, labelGraphPanel;

	@UiField
	TextBox textboxDaysForPing, textboxDaysForObject, textboxDaysForLabel;

	@UiField
	Button buttonSearchPing, buttonSearchObject, buttonSearchLabel;
	
	@UiField
	Heading headingTotal, headingInstalled;

	long studyId;
	Div pingListDiv, objectListDiv, labelListDiv;

	private Boolean pingTableLoaded = false;
	private Boolean objectTableLoaded = false;
	private Boolean labelTableLoaded = false;
	private ArrayList<Participant> pingList = new ArrayList<>();
	private ArrayList<Participant> objectList = new ArrayList<>();
	private ArrayList<Participant> labelList = new ArrayList<>();
	Table pingListTable = new Table();
	Table objectListTable = new Table();
	Table labelListTable = new Table();
	DataView pingDataView = null;
	DataView objectDataView = null;
	DataView labelDataView = null;

	private LineChart pingChart, uniquePingChart, objectChart, uniqueObjectChart, labelChart, uniqueLabelChart;

	static final int DEFAULT_DAYS = 15;

	private final DashboardServiceAsync service = GWT.create(DashboardService.class);

	private static StudySummaryUiBinder uiBinder = GWT.create(StudySummaryUiBinder.class);

	interface StudySummaryUiBinder extends UiBinder<Widget, StudySummary> {
	}

	public StudySummary(long studyId) {
		initWidget(uiBinder.createAndBindUi(this));
		this.studyId = studyId;
		
		
		service.getTotalParticipant(studyId, new AsyncCallback<Integer>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Integer result) {
				String text = headingTotal.getText();
				headingTotal.setText(text + " " + result);
			}
			
		});
		
		service.getInstalledDevicesCount(studyId, new AsyncCallback<Integer>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Integer result) {
				String text = headingInstalled.getText();
				headingInstalled.setText(text + " " + result);
			}
			
		});
		

		pingListDiv = new Div();
		pingListDiv.setId("no_ping_list_div");
		
		objectListDiv = new Div();
		objectListDiv.setId("no_object_list_div");
		
		labelListDiv = new Div();
		labelListDiv.setId("no_label_list_div");

		ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
		chartLoader.loadApi(new Runnable() {
			public void run() {
				pingChart = new LineChart();
				uniquePingChart = new LineChart();
				
				objectChart = new LineChart();
				uniqueObjectChart = new LineChart();

				labelChart = new LineChart();
				uniqueLabelChart = new LineChart();
				
				pingGraphPanel.add(pingChart);
				pingGraphPanel.add(new Br());
				pingGraphPanel.add(new Br());
				uniquePingGraphPanel.add(uniquePingChart);

				loadPingGraph();
				loadUniquePingGraph();

				objectGraphPanel.add(objectChart);
				objectGraphPanel.add(new Br());
				objectGraphPanel.add(new Br());
				objectGraphPanel.add(uniqueObjectChart);
				
				loadObjectGraph();
				loadUniqueObjectGraph();
				
				
				labelGraphPanel.add(labelChart);
				labelGraphPanel.add(new Br());
				labelGraphPanel.add(new Br());
				labelGraphPanel.add(uniqueLabelChart);
				
				loadLabelGraph();
				loadUniqueLabelGraph();
				
			}
		});

	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();

		if (!pingTableLoaded) {
			pingListPanel.add(pingListDiv);
			pingListPanel.add(new Br());

			loadNoPingTable(null);
		}
		
		if (!objectTableLoaded) {
			objectListPanel.add(objectListDiv);
			objectListPanel.add(new Br());
			
			loadNoObjectTable(null);
		}
		
		if (!labelTableLoaded) {
			labelListPanel.add(labelListDiv);
			labelListPanel.add(new Br());
			
			loadNoLabelTable(null);
		}
		// loadPingGraph();
		// loadUniquePingGraph();
	}

	void loadPingGraph() {

		service.getDailyPingCount(studyId, DEFAULT_DAYS, new AsyncCallback<ArrayList<DaywiseCount>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ArrayList<DaywiseCount> result) {
				DataTable data = DataTable.create();
				data.addColumn(ColumnType.STRING, "Date");
				data.addColumn(ColumnType.NUMBER, "Count");

				ArrayList<MyPair<String, Integer>> dailyMap = new ArrayList<>();
				DateTimeFormat fm = DateTimeFormat.getFormat("MMM dd");
				Date today = new Date();
				CalendarUtil.addDaysToDate(today, -DEFAULT_DAYS+1);

				for (int i = 0; i < DEFAULT_DAYS; i++) {
					int pingCount = 0;// (int) (Math.random() * (1000 - 0) + 0);
					String stringDate = fm.format(today);
					for (int j = 0; j < result.size(); j++) {
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

	void loadUniquePingGraph() {
		service.getDailyPingCountFromUniqueDevices(studyId, DEFAULT_DAYS, new AsyncCallback<ArrayList<DaywiseCount>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ArrayList<DaywiseCount> result) {
				DataTable data = DataTable.create();
				data.addColumn(ColumnType.STRING, "Date");
				data.addColumn(ColumnType.NUMBER, "Count");

				ArrayList<MyPair<String, Integer>> dailyMap = new ArrayList<>();
				DateTimeFormat fm = DateTimeFormat.getFormat("MMM dd");
				Date today = new Date();
				CalendarUtil.addDaysToDate(today, -DEFAULT_DAYS+1);

				for (int i = 0; i < DEFAULT_DAYS; i++) {
					int pingCount = 0;// (int) (Math.random() * (1000 - 0) + 0);
					String stringDate = fm.format(today);
					for (int j = 0; j < result.size(); j++) {
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
				options.setTitle("Daily Ping from Unique Devices");
				options.setHAxis(HAxis.create("Date"));
				VAxis vAxis = VAxis.create("Total Ping");
				vAxis.setMinValue(0);
				// vAxis.se
				options.setVAxis(vAxis);
				options.setPointShape(PointShapeType.CIRCLE);
				options.setPointSize(4);
				options.setAxisTitlesPosition(AxisTitlesPosition.NONE);
				options.setLegend(Legend.create(LegendPosition.NONE));

				// Draw the chart
				uniquePingChart.draw(data, options);
				uniquePingChart.setWidth("100%");
				// pingChart.setHeight("400px");

			}

		});
	}
	
	void loadObjectGraph() {
		

		service.getDailyObjectCount(studyId, DEFAULT_DAYS, new AsyncCallback<ArrayList<DaywiseCount>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ArrayList<DaywiseCount> result) {
				DataTable data = DataTable.create();
				data.addColumn(ColumnType.STRING, "Date");
				data.addColumn(ColumnType.NUMBER, "Count");

				DateTimeFormat fm = DateTimeFormat.getFormat("MMM dd");
				Date today = new Date();
				CalendarUtil.addDaysToDate(today, -DEFAULT_DAYS+1);

				for (int i = 0; i < DEFAULT_DAYS; i++) {
					int objectCount = 0;// (int) (Math.random() * (1000 - 0) + 0);
					String stringDate = fm.format(today);
					for (int j = 0; j < result.size(); j++) {
						if (result.get(j).getDate().equalsIgnoreCase(stringDate)) {
							// data.addRow(stringDate,
							// counts.get(i).getCount());
							objectCount = result.get(j).getCount();
							break;
						}
					}
					data.addRow(stringDate, objectCount);
					// Window.alert("date :" + fm.format(today) + "," +
					// stringDate+ "," + pingCount);
					CalendarUtil.addDaysToDate(today, 1);

				}

				// Set options
				LineChartOptions options = LineChartOptions.create();
				options.setTitle("Total Objects Per Day");
				options.setHAxis(HAxis.create("Date"));
				VAxis vAxis = VAxis.create("Total Objects");
				vAxis.setMinValue(0);
				// vAxis.se
				options.setVAxis(vAxis);
				options.setPointShape(PointShapeType.TRIANGLE);
				options.setPointSize(4);
				options.setAxisTitlesPosition(AxisTitlesPosition.NONE);
				options.setLegend(Legend.create(LegendPosition.NONE));

				// Draw the chart
				objectChart.draw(data, options);
				objectChart.setWidth("100%");
				// pingChart.setHeight("400px");

			}

		});

	}

	void loadUniqueObjectGraph() {
		
		service.getDailyObjectCountFromUniqueDevices(studyId, DEFAULT_DAYS, new AsyncCallback<ArrayList<DaywiseCount>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ArrayList<DaywiseCount> result) {
				DataTable data = DataTable.create();
				data.addColumn(ColumnType.STRING, "Date");
				data.addColumn(ColumnType.NUMBER, "Count");

				DateTimeFormat fm = DateTimeFormat.getFormat("MMM dd");
				Date today = new Date();
				CalendarUtil.addDaysToDate(today, -DEFAULT_DAYS+1);

				for (int i = 0; i < DEFAULT_DAYS; i++) {
					int objectCount = 0;// (int) (Math.random() * (1000 - 0) + 0);
					String stringDate = fm.format(today);
					for (int j = 0; j < result.size(); j++) {
						if (result.get(j).getDate().equalsIgnoreCase(stringDate)) {
							// data.addRow(stringDate,
							// counts.get(i).getCount());
							objectCount = result.get(j).getCount();
							break;
						}
					}
					data.addRow(stringDate, objectCount);
					// Window.alert("date :" + fm.format(today) + "," +
					// stringDate+ "," + pingCount);
					CalendarUtil.addDaysToDate(today, 1);

				}

				// Set options
				LineChartOptions options = LineChartOptions.create();
				options.setTitle("Daily Objects from Unique Devices");
				options.setHAxis(HAxis.create("Date"));
				VAxis vAxis = VAxis.create("Total Objects");
				vAxis.setMinValue(0);
				// vAxis.se
				options.setVAxis(vAxis);
				options.setPointShape(PointShapeType.TRIANGLE);
				options.setPointSize(4);
				options.setAxisTitlesPosition(AxisTitlesPosition.NONE);
				options.setLegend(Legend.create(LegendPosition.NONE));

				// Draw the chart
				uniqueObjectChart.draw(data, options);
				uniqueObjectChart.setWidth("100%");
				// pingChart.setHeight("400px");

			}

		});
	}

	void loadLabelGraph() {
		

		service.getDailyLabelCount(studyId, DEFAULT_DAYS, new AsyncCallback<ArrayList<DaywiseCount>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ArrayList<DaywiseCount> result) {
				DataTable data = DataTable.create();
				data.addColumn(ColumnType.STRING, "Date");
				data.addColumn(ColumnType.NUMBER, "Count");

				DateTimeFormat fm = DateTimeFormat.getFormat("MMM dd");
				Date today = new Date();
				CalendarUtil.addDaysToDate(today, -DEFAULT_DAYS+1);

				for (int i = 0; i < DEFAULT_DAYS; i++) {
					int labelCount = 0;// (int) (Math.random() * (1000 - 0) + 0);
					String stringDate = fm.format(today);
					for (int j = 0; j < result.size(); j++) {
						if (result.get(j).getDate().equalsIgnoreCase(stringDate)) {
							labelCount = result.get(j).getCount();
							break;
						}
					}
					data.addRow(stringDate, labelCount);
					CalendarUtil.addDaysToDate(today, 1);

				}

				// Set options
				LineChartOptions options = LineChartOptions.create();
				options.setTitle("Total Label Per Day");
				options.setHAxis(HAxis.create("Date"));
				VAxis vAxis = VAxis.create("Total Labels");
				vAxis.setMinValue(0);
				// vAxis.se
				options.setVAxis(vAxis);
				options.setPointShape(PointShapeType.SQUARE);
				options.setPointSize(4);
				options.setAxisTitlesPosition(AxisTitlesPosition.NONE);
				options.setLegend(Legend.create(LegendPosition.NONE));

				// Draw the chart
				labelChart.draw(data, options);
				labelChart.setWidth("100%");
				// pingChart.setHeight("400px");

			}

		});

	}

	void loadUniqueLabelGraph() {
		
		service.getDailyLabelCountFromUniqueDevices(studyId, DEFAULT_DAYS, new AsyncCallback<ArrayList<DaywiseCount>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ArrayList<DaywiseCount> result) {
				DataTable data = DataTable.create();
				data.addColumn(ColumnType.STRING, "Date");
				data.addColumn(ColumnType.NUMBER, "Count");

				DateTimeFormat fm = DateTimeFormat.getFormat("MMM dd");
				Date today = new Date();
				CalendarUtil.addDaysToDate(today, -DEFAULT_DAYS+1);

				for (int i = 0; i < DEFAULT_DAYS; i++) {
					int labelCount = 0;// (int) (Math.random() * (1000 - 0) + 0);
					String stringDate = fm.format(today);
					for (int j = 0; j < result.size(); j++) {
						if (result.get(j).getDate().equalsIgnoreCase(stringDate)) {
							labelCount = result.get(j).getCount();
							break;
						}
					}
					data.addRow(stringDate, labelCount);
					CalendarUtil.addDaysToDate(today, 1);
				}

				// Set options
				LineChartOptions options = LineChartOptions.create();
				options.setTitle("Daily Labels from Unique Devices");
				options.setHAxis(HAxis.create("Date"));
				VAxis vAxis = VAxis.create("Total Labels");
				vAxis.setMinValue(0);
				// vAxis.se
				options.setVAxis(vAxis);
				options.setPointShape(PointShapeType.SQUARE);
				options.setPointSize(4);
				options.setAxisTitlesPosition(AxisTitlesPosition.NONE);
				options.setLegend(Legend.create(LegendPosition.NONE));

				// Draw the chart
				uniqueLabelChart.draw(data, options);
				uniqueLabelChart.setWidth("100%");
				// pingChart.setHeight("400px");

			}

		});
	}

	@UiHandler("buttonSearchPing")
	void loadNoPingTable(ClickEvent event) {
		
		loadPingGraph();
		loadUniquePingGraph();
		
		int days = DEFAULT_DAYS;
		try {
			if (!textboxDaysForPing.getText().isEmpty()) {
				days = Integer.parseInt(textboxDaysForPing.getText().trim());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		service.getParticipantsWithNoPing(studyId, days, new AsyncCallback<ArrayList<Participant>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ArrayList<Participant> result) {
				pingList = result;
				pingListDiv.add(pingListTable);
				drawPingListTable();
				pingTableLoaded = true;
			}

		});

	}
	
	@UiHandler("buttonSearchObject")
	void loadNoObjectTable(ClickEvent event){
		
		loadObjectGraph();
		loadUniqueObjectGraph();
		
		int days = DEFAULT_DAYS;
		try {
			if (!textboxDaysForObject.getText().isEmpty()) {
				days = Integer.parseInt(textboxDaysForObject.getText().trim());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		service.getParticipantsWithNoData(studyId, days, new AsyncCallback<ArrayList<Participant>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ArrayList<Participant> result) {
				objectList = result;
				objectListDiv.add(objectListTable);
				drawObjectListTable();
				objectTableLoaded = true;
			}

		});

	}

	@UiHandler("buttonSearchLabel")
	void loadNoLabelTable(ClickEvent event){
		
		loadLabelGraph();
		loadUniqueLabelGraph();
		
		int days = DEFAULT_DAYS;
		try {
			if (!textboxDaysForLabel.getText().isEmpty()) {
				days = Integer.parseInt(textboxDaysForLabel.getText().trim());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		service.getParticipantsWithNoLabel(studyId, days, new AsyncCallback<ArrayList<Participant>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ArrayList<Participant> result) {
				labelList = result;
				labelListDiv.add(labelListTable);
				drawLabelListTable();
				labelTableLoaded = true;
			}

		});

	}

	
	public void drawPingListTable() {
		if (pingList.size() == 0) {
			pingListTable.setVisible(false);
		} else {
			pingListTable.setVisible(true);
			pingDataView = DataView.create(getDataTableFromList(pingList));
			// dataView.hideColumns(new int[]{1});
			pingListTable.draw(pingDataView, getTableOptions());
		}

	}

	public void drawObjectListTable() {
		if (objectList.size() == 0) {
			objectListTable.setVisible(false);
		} else {
			objectListTable.setVisible(true);
			objectDataView = DataView.create(getDataTableFromList(objectList));
			// dataView.hideColumns(new int[]{1});
			objectListTable.draw(objectDataView, getTableOptions());
		}
	}
	
	public void drawLabelListTable() {
		if (labelList.size() == 0) {
			labelListTable.setVisible(false);
		} else {
			labelListTable.setVisible(true);
			labelDataView = DataView.create(getDataTableFromList(labelList));
			// dataView.hideColumns(new int[]{1});
			labelListTable.draw(labelDataView, getTableOptions());
		}
	}

	protected TableOptions getTableOptions() {
		TableOptions options = (TableOptions) Options.create();

		options.setAllowHtml(true);
		options.setSort(TableSort.ENABLE);
		options.setPage(TablePage.ENABLE);
		options.setPageSize(15);
		options.setStartPage(0);
		options.setWidth(getParent().getOffsetWidth());
		// options.setSort(Policy.ENABLE);
		// options.setPage(Policy.ENABLE);
		// //options.setWidth(width)
		// //options.setStartPage(0);
		// //options.setWidth("100%");
		// //options.setHeight("100%");
		// options.setOption("startPage", 0);
		// options.setOption("width", "100%");
		return options;
	}

	protected DataTable getDataTableFromList(ArrayList<Participant> list) {
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "");
		dataTable.addColumn(ColumnType.STRING, "Email");
		dataTable.addColumn(ColumnType.STRING, "Identifier");

		dataTable.addRows(list.size());
		int columnIndex = 0;
		int rowIndex = 0;
		Iterator<Participant> it = list.iterator();
		while (it.hasNext()) {
			Participant participant = (Participant) it.next();
			dataTable.setValue(rowIndex, columnIndex++, String.valueOf(rowIndex + 1));
			dataTable.setValue(rowIndex, columnIndex++, participant.getUserEmail());
			dataTable.setValue(rowIndex, columnIndex++, participant.getIdentifier());

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
					// if (selections.get(i).) {
					String id = dataTable.getFormattedValue(selections.get(i).getRow(), 1);
					message += (id + " ");
					// }

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

class MyPair<T1, T2> {
	T1 left;
	T2 right;

	public MyPair(T1 left, T2 right) {
		this.left = left;
		this.right = right;
	}

	public T1 getLeft() {
		return left;
	}

	public T2 getRight() {
		return right;
	}

	public void setLeft(T1 left) {
		this.left = left;
	}

	public void setRight(T2 right) {
		this.right = right;
	}
}
