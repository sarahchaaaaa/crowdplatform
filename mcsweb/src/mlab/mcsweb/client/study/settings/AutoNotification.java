package mlab.mcsweb.client.study.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AutoNotification extends Composite {

	private static AutoNotificationUiBinder uiBinder = GWT.create(AutoNotificationUiBinder.class);

	interface AutoNotificationUiBinder extends UiBinder<Widget, AutoNotification> {
	}

	public AutoNotification() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	}
