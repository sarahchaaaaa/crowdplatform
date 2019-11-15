package mlab.mcsweb.client.study.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DataUpload extends Composite {

	private static DataUploadUiBinder uiBinder = GWT.create(DataUploadUiBinder.class);

	interface DataUploadUiBinder extends UiBinder<Widget, DataUpload> {
	}

	public DataUpload() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
