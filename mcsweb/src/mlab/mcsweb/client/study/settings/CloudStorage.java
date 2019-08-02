package mlab.mcsweb.client.study.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CloudStorage extends Composite {

	private static CloudStorageUiBinder uiBinder = GWT.create(CloudStorageUiBinder.class);

	interface CloudStorageUiBinder extends UiBinder<Widget, CloudStorage> {
	}

	public CloudStorage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
