package mlab.mcsweb.client.study.labeling;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LabelingList extends Composite {

	private static LabelingListUiBinder uiBinder = GWT.create(LabelingListUiBinder.class);

	interface LabelingListUiBinder extends UiBinder<Widget, LabelingList> {
	}

	public LabelingList() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
