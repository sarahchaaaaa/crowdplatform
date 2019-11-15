package mlab.mcsweb.client.study.survey;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.CheckBoxButton;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Br;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.study.survey.TaskEditorState.EditorSpecificState;
import mlab.mcsweb.shared.Util;

public class MultipleChoiceView extends Composite {
	
	@UiField
	VerticalPanel verticalPanel;
	
	@UiField
	Button addOptionButton;
	
	@UiField
	CheckBox allowMutipleCheckbox;
	

	private static MultipleChoiceViewUiBinder uiBinder = GWT.create(MultipleChoiceViewUiBinder.class);

	interface MultipleChoiceViewUiBinder extends UiBinder<Widget, MultipleChoiceView> {
	}

	public MultipleChoiceView() {
		initWidget(uiBinder.createAndBindUi(this));
		verticalPanel.setWidth("80%");
		verticalPanel.add(new AdditionalOption("", false));
		verticalPanel.add(new AdditionalOption("", false));
	}
	
	public MultipleChoiceView(String possibleInput, boolean isMutipleAllowed) {
		initWidget(uiBinder.createAndBindUi(this));
		verticalPanel.setWidth("80%");
		String[] inputs = JSUtil.split(possibleInput, "|");
		if(inputs.length>0){
			for(int i=0;i<inputs.length;i++){
				if(i==0 || i==1){
					verticalPanel.add(new AdditionalOption(inputs[i], false));
				}else{
					verticalPanel.add(new AdditionalOption(inputs[i], true));
				}
				
			}
		}else {
			verticalPanel.add(new AdditionalOption("", false));
			verticalPanel.add(new AdditionalOption("", false));
		}
		allowMutipleCheckbox.setValue(isMutipleAllowed);
		
	}

	@UiHandler("addOptionButton")
	void addOption(ClickEvent event){
		Mcsweb.getEventBus().fireEvent(new TaskEditorEvent(new TaskEditorState(EditorSpecificState.RESIZE)));
		verticalPanel.add(new AdditionalOption("", true));
	}
	
	public String getPossibleInput(){
		String result="";
		for(int i=0;i<verticalPanel.getWidgetCount();i++){
			Widget widget = verticalPanel.getWidget(i);
			if(widget instanceof AdditionalOption){
				result += (((AdditionalOption) widget).getOptionText() + "|");
			}
		}
		if(result.length()>0){
			result = result.substring(0, result.length()-1);
		}
		return result;
	}
	
	protected String getError() {
		for(int i=0;i<verticalPanel.getWidgetCount();i++){
			Widget widget = verticalPanel.getWidget(i);
			if(widget instanceof AdditionalOption){
				String text = ((AdditionalOption) widget).getOptionText().trim();
				if(Util.isEmptyString(text)){
					return "Missing text in one option";
				}
			}
		}
		return "";
	}
	
	public boolean isMultipleAllowed(){
		return allowMutipleCheckbox.getValue();
	}
}

class AdditionalOption extends Composite{
	HTMLPanel panel;
	TextBox textBox = new TextBox();
	public AdditionalOption(String optionText, boolean closable) {
		// TODO Auto-generated constructor stub
		panel = new HTMLPanel("");
		
		panel.add(new Br());
		Column col1 = new Column("MD_11");
		Column col2 = new Column("MD_1");
		
		InputGroup inputGroup = new InputGroup();
		InputGroupButton groupButton = new InputGroupButton();
		CheckBoxButton ckButton = new CheckBoxButton();
		ckButton.setEnabled(false);
		groupButton.add(ckButton);
		inputGroup.add(groupButton);
		
		if(Util.isEmptyString(optionText)){
			textBox.setPlaceholder("Type Option");
		}else{
			textBox.setText(optionText.trim());
		}
		
		inputGroup.add(textBox);
		
		Anchor anchor = new Anchor();
		anchor.setIcon(IconType.CLOSE);
		
		if(closable){
			anchor.setVisible(true);
		}else {
			anchor.setVisible(false);
		}
		

		anchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				panel.getParent().removeFromParent();
				Mcsweb.getEventBus().fireEvent(new TaskEditorEvent(new TaskEditorState(EditorSpecificState.RESIZE)));
			}
		});
		
		col1.add(inputGroup);
		col2.add(anchor);
		
		panel.add(col1);
		panel.add(col2);
		
		initWidget(panel);
	}
	
	public String getOptionText(){
		return textBox.getText();
	}
}
