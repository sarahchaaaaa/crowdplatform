package mlab.mcsweb.client.study.dashboard;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
//All of these imports are not working so I can't use them below
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.LineChart;
import com.googlecode.gwt.charts.client.corechart.LineChartOptions;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.VAxis;

public class PingStatistics extends Composite {

	@UiField
	Button searchButton;

	@UiField
	TextBox emailText, startText, endText;

	@UiField
	Label userDataErrorLabel, CompErrorLabel, SleepErrorLabel, HRErrorLabel;

	@UiField
	Row CompChartRow, SleepChartRow, HRChartRow;

	private LineChart compChart, sleepChart, hrChart;

	private static PingStatisticsUiBinder uiBinder = GWT.create(PingStatisticsUiBinder.class);

	interface PingStatisticsUiBinder extends UiBinder<Widget, PingStatistics> {
	}

	public PingStatistics() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		initialize();
	}

	private void initialize() {
		ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
		chartLoader.loadApi(new Runnable() {
			public void run() {
				// Create and attach the chart
				compChart = new LineChart();
				sleepChart = new LineChart();
				hrChart = new LineChart();
				CompChartRow.add(compChart);
				SleepChartRow.add(sleepChart);
				HRChartRow.add(hrChart);
				Window.alert("chart loaded, now going to draw....");
				compDraw();
				// sleepDraw();
				// hrDraw();
			}
		});
	}

	private void compDraw() {
		// Prepare the data
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Date");
		data.addColumn(ColumnType.NUMBER, "Percentage");
		// data.addColumn(ColumnType.NUMBER, "Tokyo");


		data.addRow("Jan 12", 23);
		data.addRow("Feb 1", 47);
		

		// Set options
		LineChartOptions options = LineChartOptions.create();
		options.setTitle("Compliance Statistics");
		options.setHAxis(HAxis.create("Date"));
		options.setVAxis(VAxis.create("Percentage"));

		// Draw the chart
		compChart.draw(data, options);
		compChart.setWidth("400px");
		compChart.setHeight("400px");
	}

	private void sleepDraw() {
		// Prepare the data
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Date");
		data.addColumn(ColumnType.STRING, "Time");
		// data.addColumn(ColumnType.NUMBER, "Tokyo");

		// data.addRow("Jan", 7.0, -0.2, -0.9, 3.9);
		data.addRow("7", 6.9, 0.8, 0.6, 4.2);

		// Set options
		LineChartOptions options = LineChartOptions.create();
		options.setTitle("Sleep Statistics");
		options.setHAxis(HAxis.create("Date"));
		options.setVAxis(VAxis.create("Total Sleep Time"));

		// Draw the chart
		sleepChart.draw(data, options);
		sleepChart.setWidth("400px");
		sleepChart.setHeight("400px");
	}

	private void hrDraw() {
		// Prepare the data
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Rate");
		// data.addColumn(ColumnType.NUMBER, "Tokyo");

		// data.addRow("Jan", 7.0, -0.2, -0.9, 3.9);
		data.addRow("100", 6.9, 0.8, 0.6, 4.2);

		// Set options
		LineChartOptions options = LineChartOptions.create();
		options.setTitle("Heartrate Statistics");
		options.setHAxis(HAxis.create("Date"));
		options.setVAxis(VAxis.create("Average Heartrate"));

		// LineChart.Options options2 = com.googlecode.gwt.charts
		// Draw the chart
		hrChart.draw(data, options);
		hrChart.setWidth("400px");
		hrChart.setHeight("400px");
	}

}
