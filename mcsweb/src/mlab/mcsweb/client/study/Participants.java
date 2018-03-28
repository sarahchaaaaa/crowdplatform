package mlab.mcsweb.client.study;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Participants extends Composite {

	private static ParticipantsUiBinder uiBinder = GWT.create(ParticipantsUiBinder.class);

	interface ParticipantsUiBinder extends UiBinder<Widget, Participants> {
	}

	public Participants() {
		initWidget(uiBinder.createAndBindUi(this));
	}


}
