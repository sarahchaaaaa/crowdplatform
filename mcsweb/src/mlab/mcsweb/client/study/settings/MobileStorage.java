package mlab.mcsweb.client.study.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MobileStorage extends Composite {

	private static MobileStorageUiBinder uiBinder = GWT.create(MobileStorageUiBinder.class);

	interface MobileStorageUiBinder extends UiBinder<Widget, MobileStorage> {
	}

	public MobileStorage() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
