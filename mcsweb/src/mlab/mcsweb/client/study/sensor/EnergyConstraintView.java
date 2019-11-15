package mlab.mcsweb.client.study.sensor;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class EnergyConstraintView extends Composite {
	@UiField
	HTMLPanel constraintPanel;
	
	@UiField
	Anchor closeAnchor;

	private static EnergyConstraintViewUiBinder uiBinder = GWT.create(EnergyConstraintViewUiBinder.class);

	interface EnergyConstraintViewUiBinder extends UiBinder<Widget, EnergyConstraintView> {
	}

	public EnergyConstraintView() {
		initWidget(uiBinder.createAndBindUi(this));
		closeAnchor.setIcon(IconType.CLOSE);
		closeAnchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {
					constraintPanel.getParent().removeFromParent();
					
				} catch (Exception e) {
					// TODO: handle exception
					Window.alert("exception :" + e.getMessage());
				}
			}
		});

	}

}
