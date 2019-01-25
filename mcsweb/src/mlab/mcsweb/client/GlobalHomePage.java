package mlab.mcsweb.client;

import org.gwtbootstrap3.client.ui.NavbarBrand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class GlobalHomePage extends Composite {
	
	//@UiField
	//AnchorListItem /*globalHomeAnchor,*/ loginAnchor;
	@UiField
	NavbarBrand koiosBrand;
	
	@UiField
	HTMLPanel globalHomeContentPanel;
	
	private LoginPage loginPage = null;
	private Mcsweb application = null;
	

	private static GlobalHomePageUiBinder uiBinder = GWT.create(GlobalHomePageUiBinder.class);

	interface GlobalHomePageUiBinder extends UiBinder<Widget, GlobalHomePage> {
	}

	public GlobalHomePage(Mcsweb application) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.application = application;
		if(loginPage == null){
			loginPage = new LoginPage(this.application);
		}
		globalHomeContentPanel.clear();
		globalHomeContentPanel.add(loginPage);

		//globalHomeAnchor.setActive(false);
		//loginAnchor.setActive(true);

	}


	/*
	@UiHandler("globalHomeAnchor")
	void homeAnchorClicked(ClickEvent event){
		globalHomeAnchor.setActive(true);
		loginAnchor.setActive(false);
		globalHomeContentPanel.clear();
	}
	*/
	/*
	@UiHandler("loginAnchor")
	void loginClicked(ClickEvent event){
		if(loginPage == null){
			loginPage = new LoginPage(this.application);
		}
		globalHomeContentPanel.clear();
		globalHomeContentPanel.add(loginPage);

		//globalHomeAnchor.setActive(false);
		loginAnchor.setActive(true);
		
		
	}*/
	
	@UiHandler("koiosBrand")
	void clickBrand(ClickEvent event){
		if(!application.isLoggedIn()){
			Window.Location.replace("https://koiosplatform.com/");			
		}
	}
}
