package mlab.mcsweb.client.study.participant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FileObjectHistory extends Composite {

	private static FileObjectHistoryUiBinder uiBinder = GWT.create(FileObjectHistoryUiBinder.class);

	interface FileObjectHistoryUiBinder extends UiBinder<Widget, FileObjectHistory> {
	}

	public FileObjectHistory() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
