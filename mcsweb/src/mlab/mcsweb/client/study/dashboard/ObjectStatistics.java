package mlab.mcsweb.client.study.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ObjectStatistics extends Composite {

	private static ObjectStatisticsUiBinder uiBinder = GWT.create(ObjectStatisticsUiBinder.class);

	interface ObjectStatisticsUiBinder extends UiBinder<Widget, ObjectStatistics> {
	}

	public ObjectStatistics() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
