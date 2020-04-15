package mlab.mcsweb.client.study.wearable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class StudyWearableStat extends Composite {

	private static StudyWearableStatUiBinder uiBinder = GWT.create(StudyWearableStatUiBinder.class);

	interface StudyWearableStatUiBinder extends UiBinder<Widget, StudyWearableStat> {
	}

	public StudyWearableStat() {
		initWidget(uiBinder.createAndBindUi(this));
	}


}
