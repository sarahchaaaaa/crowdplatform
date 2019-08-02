package mlab.mcsweb.client.study.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Collaborations extends Composite {

	private static CollaborationsUiBinder uiBinder = GWT.create(CollaborationsUiBinder.class);

	interface CollaborationsUiBinder extends UiBinder<Widget, Collaborations> {
	}

	public Collaborations() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
