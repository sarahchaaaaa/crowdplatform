package mlab.mcsweb.client.study.participant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CustomFileReader extends Composite {

	private static CustomFileReaderUiBinder uiBinder = GWT.create(CustomFileReaderUiBinder.class);

	interface CustomFileReaderUiBinder extends UiBinder<Widget, CustomFileReader> {
	}

	public CustomFileReader() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		loadFile();
	}
	
	public native void loadFile()/*-{
		//alert('this is a test from jnsi');
		var fileInput = $wnd.document.getElementById("fileInput");
		var fileDisplayArea = $wnd.document.getElementById("fileDisplayArea");
		fileInput.addEventListener("change", function(e) {
			var file = fileInput.files[0];
			var textType = "text.*";
			if (file.type.match(textType)) {
				var reader = new FileReader();
				reader.onload = function(e) {
					fileDisplayArea.innerText = reader.result;
					fileDisplayArea.innerText.color = "red";
				};
				reader.readAsText(file);
		    } else {
		      fileDisplayArea.innerText = "File not supported!" + new Date() + textType + "," + file.type;
		    }
			//fileDisplayArea.innerText = "File not supported!" + new Date() + textType;
		});
	}-*/;

}
