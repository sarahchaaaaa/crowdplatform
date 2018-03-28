package mlab.mcsweb.client.study.sensor;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class LocationConfiguration extends Composite {
	
	@UiField
	Button collapseButton, timeButton, batteryButton;
	
	@UiField
	Row headerRow;
	
	@UiField
	HTMLPanel configDetailsPanel;
	
	private boolean isCollapsed = true;

	private static LocationConfigurationUiBinder uiBinder = GWT.create(LocationConfigurationUiBinder.class);

	interface LocationConfigurationUiBinder extends UiBinder<Widget, LocationConfiguration> {
	}

	public LocationConfiguration() {
		initWidget(uiBinder.createAndBindUi(this));
		configDetailsPanel.setVisible(false);
		collapseButton.setText("");
		if(configDetailsPanel.isVisible()){
			collapseButton.setIcon(IconType.ANGLE_DOUBLE_UP);
			isCollapsed = false;
		}else {
			collapseButton.setIcon(IconType.ANGLE_DOUBLE_DOWN);
			isCollapsed = true;
		}
		headerRow.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				editConfig(null);
			}
		}, ClickEvent.getType());
		
	}
	
	@UiHandler("collapseButton")
	void editConfig(ClickEvent event){
		if(isCollapsed){
			configDetailsPanel.setVisible(true);
			collapseButton.setIcon(IconType.ANGLE_DOUBLE_UP);
			isCollapsed = false;
		}else {
			configDetailsPanel.setVisible(false);
			collapseButton.setIcon(IconType.ANGLE_DOUBLE_DOWN);
			isCollapsed = true;
		}
	}
	

}
