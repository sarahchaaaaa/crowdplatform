package mlab.mcsweb.client.study.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class StudySummary extends Composite {

	private static StudySummaryUiBinder uiBinder = GWT.create(StudySummaryUiBinder.class);

	interface StudySummaryUiBinder extends UiBinder<Widget, StudySummary> {
	}

	public StudySummary() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
