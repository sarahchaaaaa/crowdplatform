package mlab.mcsweb.client.study.wearable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class IndividualWearableStat extends Composite {

	private static IndividualWearableStatUiBinder uiBinder = GWT.create(IndividualWearableStatUiBinder.class);

	interface IndividualWearableStatUiBinder extends UiBinder<Widget, IndividualWearableStat> {
	}

	public IndividualWearableStat() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
