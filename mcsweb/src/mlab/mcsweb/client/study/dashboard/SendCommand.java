package mlab.mcsweb.client.study.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SendCommand extends Composite {

	private static SendCommandUiBinder uiBinder = GWT.create(SendCommandUiBinder.class);

	interface SendCommandUiBinder extends UiBinder<Widget, SendCommand> {
	}

	public SendCommand() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
