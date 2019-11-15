package mlab.mcsweb.client.study;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WearableManagement extends Composite {

	private static WearableManagementUiBinder uiBinder = GWT.create(WearableManagementUiBinder.class);

	interface WearableManagementUiBinder extends UiBinder<Widget, WearableManagement> {
	}

	public WearableManagement() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
