package mlab.mcsweb.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.shared.Study;

public class UserHomePage extends Composite {

	
	private final String userId;
	
	private boolean loaded = false;
	
	
	private StudyCreator studyCreator = null;
	private StudyListManager studyListManager = null;
	
	@UiField
	HTMLPanel createStudyPanel, studyListPanel, userHomeContentPanel;
	
	
	private static UserHomePageUiBinder uiBinder = GWT.create(UserHomePageUiBinder.class);

	interface UserHomePageUiBinder extends UiBinder<Widget, UserHomePage> {
	}

	public UserHomePage(String userId) {
		initWidget(uiBinder.createAndBindUi(this));
		this.userId = userId;
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		
		
		if(!loaded){
			//Window.alert("loading user home page");
			
			studyCreator = new StudyCreator(this.userId, this);
			studyListManager = new StudyListManager(this.userId);
			
			createStudyPanel.clear();
			createStudyPanel.add(studyCreator);
			
			studyListPanel.clear();
			studyListPanel.add(studyListManager);
			
			loaded = true;
		}else {
			//Window.alert("already loaded user home page, that's the problem...");

		}
	}

	
	protected void addCreatedStudyToList(Study study) {
		studyListManager.insertStudy(study);
	}
	
}
