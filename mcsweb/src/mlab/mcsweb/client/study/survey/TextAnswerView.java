package mlab.mcsweb.client.study.survey;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TextAnswerView extends Composite {

	private static TextAnswerViewUiBinder uiBinder = GWT.create(TextAnswerViewUiBinder.class);

	interface TextAnswerViewUiBinder extends UiBinder<Widget, TextAnswerView> {
	}

	public TextAnswerView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
