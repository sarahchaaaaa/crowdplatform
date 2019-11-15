package mlab.mcsweb.client.study.survey;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DateView extends Composite {

	private static DateViewUiBinder uiBinder = GWT.create(DateViewUiBinder.class);

	interface DateViewUiBinder extends UiBinder<Widget, DateView> {
	}

	public DateView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
