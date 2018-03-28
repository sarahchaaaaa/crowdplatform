package mlab.mcsweb.client.study;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PhoneSensor extends Composite {

	private static PhoneSensorUiBinder uiBinder = GWT.create(PhoneSensorUiBinder.class);

	interface PhoneSensorUiBinder extends UiBinder<Widget, PhoneSensor> {
	}

	public PhoneSensor() {
		initWidget(uiBinder.createAndBindUi(this));
	}


}
