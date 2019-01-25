package mlab.mcsweb.client;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.html.Br;
import org.gwtbootstrap3.client.ui.html.Hr;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.events.StudyEvent;
import mlab.mcsweb.client.events.StudyState;
import mlab.mcsweb.client.events.StudyState.StudySpecificState;
import mlab.mcsweb.shared.Study;

public class StudyListManager extends Composite {
	
	@UiField
	Column listPanel;
	
	private String userId;
	private ArrayList<Study> studyList;

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private static StudyListManagerUiBinder uiBinder = GWT.create(StudyListManagerUiBinder.class);

	interface StudyListManagerUiBinder extends UiBinder<Widget, StudyListManager> {
	}

	public StudyListManager(String userId) {
		initWidget(uiBinder.createAndBindUi(this));
		this.userId = userId;
		
		greetingService.getStudyList(userId, new AsyncCallback<ArrayList<Study>>() {
			
			@Override
			public void onSuccess(ArrayList<Study> result) {
				// TODO Auto-generated method stub
				studyList = result;
				populateStudies(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("fail to get study " + caught.getMessage());
			}
		});
	}
	
	void insertStudy(Study study){
		studyList.add(0, study);
		listPanel.insert(new Br(), 0);
		listPanel.insert(new Br(), 0);			
		listPanel.insert(getStudyPanel(study), 0);
		
	}
	
	private Study getStudy(String studyTag){
		
		Study myStudy = null;
		try {
			int id = Integer.parseInt(studyTag.substring(6));
			for(int i=0;i<this.studyList.size();i++){
				if(id==this.studyList.get(i).getId()){
					myStudy = this.studyList.get(i);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return myStudy;
	}
	
	private HTMLPanel getStudyPanel(Study study){
		final HTMLPanel studyPanel = new HTMLPanel("");
		studyPanel.getElement().setId("study-"+ study.getId());

		
		studyPanel.add(new Hr());
		Heading heading = new Heading(HeadingSize.H3, study.getName());
		studyPanel.add(heading);
		//studyPanel.add(new Br());
		Label descriptionLabel = new Label(study.getDescription());
		studyPanel.add(descriptionLabel);
		//studyPanel.add(new Br());
		Button detailsButton = new Button("View Details");
		detailsButton.setPull(Pull.RIGHT);
		detailsButton.setType(ButtonType.INFO);
		detailsButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Study myStudy = getStudy(studyPanel.getElement().getId());
				Mcsweb.getEventBus().fireEvent(new StudyEvent(new StudyState(myStudy, StudySpecificState.GETDETAILS)));
			}
		});
		studyPanel.add(detailsButton);
		studyPanel.add(new Br());
		studyPanel.add(new Hr());
		return studyPanel;
	}
	
	void populateStudies(ArrayList<Study> studyList){
		
		for(int i=0;i<studyList.size();i++){
			
			try {
				listPanel.add(getStudyPanel(studyList.get(i)));
				listPanel.add(new Br());
				listPanel.add(new Br());			
				
			} catch (Exception e) {
				// TODO: handle exception
				Window.alert("exception :" + e.getMessage());
			}
			
		}
	}


}
