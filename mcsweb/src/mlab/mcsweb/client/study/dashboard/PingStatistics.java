package mlab.mcsweb.client.study.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PingStatistics extends Composite {

	private static PingStatisticsUiBinder uiBinder = GWT.create(PingStatisticsUiBinder.class);

	interface PingStatisticsUiBinder extends UiBinder<Widget, PingStatistics> {
	}

	public PingStatistics() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
