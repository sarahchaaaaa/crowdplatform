package mlab.mcsweb.client.study.survey;

import org.gwtbootstrap3.extras.select.client.ui.OptGroup;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.shared.Util;

public class MotorTaskView extends Composite {

	@UiField
	HTMLPanel listPanel;
	
	//@UiField
	//Column selectColumn;

	Select select;

	private static MotorTaskViewUiBinder uiBinder = GWT.create(MotorTaskViewUiBinder.class);

	interface MotorTaskViewUiBinder extends UiBinder<Widget, MotorTaskView> {
	}

	public MotorTaskView() {
		initWidget(uiBinder.createAndBindUi(this));
		populateList();
	}
	
	public MotorTaskView(final String code){
		initWidget(uiBinder.createAndBindUi(this));
		populateList();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				select.setValue(code);
				select.render();
			}
		});

	}
	
	public String getMotorTaskCode(){
		return select.getValue();
	}
	
	void populateList(){
		MotorTask[] list = MotorTaskLibrary.getInstance().getAllMotorTask();
		// Window.alert("len:"+ list.length);
		select = new Select();
		select.setTitle("Select One...");
		for (int i = 0; i < list.length; i++) {
			OptGroup group = new OptGroup();
			Option option = new Option();
			option.setText(list[i].getName());
			option.setValue(list[i].getCode());
			group.add(option);
			select.add(group);
		}
		
		select.setFixedMenuSize(7);
		select.setDropupAuto(false);
		select.show();
		listPanel.add(select);
		
	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		//select.toggle();
	}
	
	protected String getError() {
		if(Util.isEmptyString(select.getValue())){
			return "Select one motor task";
		}
		return "";
	}

}
