package mlab.mcsweb.client.study.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SendNotification extends Composite {

	private static SendNotificationUiBinder uiBinder = GWT.create(SendNotificationUiBinder.class);

	interface SendNotificationUiBinder extends UiBinder<Widget, SendNotification> {
	}

	public SendNotification() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
