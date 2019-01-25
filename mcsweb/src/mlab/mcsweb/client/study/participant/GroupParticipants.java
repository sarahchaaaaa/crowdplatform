package mlab.mcsweb.client.study.participant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class GroupParticipants extends Composite {

	private static GroupParticipantsUiBinder uiBinder = GWT.create(GroupParticipantsUiBinder.class);

	interface GroupParticipantsUiBinder extends UiBinder<Widget, GroupParticipants> {
	}

	public GroupParticipants() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
	}
}
