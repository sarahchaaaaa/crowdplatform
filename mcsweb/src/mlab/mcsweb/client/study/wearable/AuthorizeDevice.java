package mlab.mcsweb.client.study.wearable;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.oAuth.Auth;
import mlab.mcsweb.client.oAuth.AuthRequest;



public class AuthorizeDevice extends Composite {
	
	@UiField
	Button buttonAdd;

	private static AuthorizeDeviceUiBinder uiBinder = GWT.create(AuthorizeDeviceUiBinder.class);

	interface AuthorizeDeviceUiBinder extends UiBinder<Widget, AuthorizeDevice> {
	}

	public AuthorizeDevice() {
		initWidget(uiBinder.createAndBindUi(this));
		Window.alert("urL:"+ GWT.getModuleBaseURL());
	}
	
	
	@UiHandler("buttonAdd")
	void addDeviceToStudy(ClickEvent event){
		
		AuthRequest request = new AuthRequest("https://www.fitbit.com/oauth2/authorize", "22B9QK").withScopeDelimiter(" ").withScopes("sleep profile");
	
		
//		AuthRequest request = 
//				new AuthRequest("https", "www.fitbit.com", "oauth2/authorize", "22B9QK")
//				//new AuthRequest("https", "accounts.google.com", "o/oauth2/auth", "388464073161-eens9i27kv63ileak5jdek0s9ff0k0f2.apps.googleusercontent.com")
//				.setParameter("response_type", "code")
//				.setParameter("scope", "activity heartrate profile sleep")
//				//.setParameter("scope", "profile email")
//				//.setParameter("grant_type", "authorization_code")
//				//.setParameter("state", "adding_device_to_study")
//				//.setParameter("redirect_uri", GWT.getModuleBaseURL() + "oauthWindow.html");
//				//.setParameter("expires_in", "604800")
//				.setParameter("state", "adding_device_to_study")
//				//.setParameter("code_challenge", "MChCW5vD-3h03HMGFZYskOSTir7II_MMTb8a9rJNhnI")
//				//.setParameter("code_challenge_method", "s256")
//				;
		
		//Window.alert("urL:" + request.buildString());
	
		Auth.get().login(request, new Callback<String, Throwable>() {

			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub
				Window.alert("Failure "+ reason.getCause());
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				Window.alert("In the success blocks " + result);
			}
			
		});
//		Auth.get().doLogin(request, new Callback<Map<String,String>, Throwable>() {
//			
//			@Override
//			public void onSuccess(Map<String, String> result) {
//				Window.alert("In the success blocks " + result);
//				// TODO Auto-generated method stub
//				String access_token = result.get("access_token");
//				String expires_in = result.get("expires_in");
//				String refresh_token = result.get("refresh_token");
//				String token_type = result.get("token_type");
//				String user_id = result.get("user_id");
//				
//				Window.alert("response: access_token " + access_token + ", expires in:" + expires_in + ", refresh_token:"+ refresh_token
//						+ ", token_type:" + token_type + ", user_id:"+ user_id);
//			}
//			
//			@Override
//			public void onFailure(Throwable reason) {
//				// TODO Auto-generated method stub
//				Window.alert("Fail to get reponse from where...." + reason.fillInStackTrace());
//			}
//		});
	}

	
//	@UiHandler("buttonAdd")
//	void addDeviceToStudy(ClickEvent event){
//		AuthRequest request = 
//				//new AuthRequest("https", "www.fitbit.com", "oauth2/authorize", "22B9QK")
//				new AuthRequest("https", "accounts.google.com", "o/oauth2/auth", "388464073161-eens9i27kv63ileak5jdek0s9ff0k0f2.apps.googleusercontent.com")
//				//.setParameter("response_type", "code")
//				//.setParameter("scope", "activity heartrate location nutrition profile sleep social weight")
//				.setParameter("scope", "profile email")
//				//.setParameter("grant_type", "authorization_code")
//				//.setParameter("state", "adding_device_to_study")
//				//.setParameter("redirect_uri", GWT.getModuleBaseURL() + "oauthWindow.html");
//				//.setParameter("state", "adding_device_to_study")
//				;
//		Auth.get().login(request, new Callback<Map<String,String>, Throwable>() {
//			
//			@Override
//			public void onSuccess(Map<String, String> result) {
//				Window.alert("In the success blocks ");
//				// TODO Auto-generated method stub
//				String access_token = result.get("access_token");
//				String expires_in = result.get("expires_in");
//				String refresh_token = result.get("refresh_token");
//				String token_type = result.get("token_type");
//				String user_id = result.get("user_id");
//				
//				Window.alert("response: access_token " + access_token + ", expires in:" + expires_in + ", refresh_token:"+ refresh_token
//						+ ", token_type:" + token_type + ", user_id:"+ user_id);
//			}
//			
//			@Override
//			public void onFailure(Throwable reason) {
//				// TODO Auto-generated method stub
//				Window.alert("Fail to get reponse from where...." + reason.getMessage());
//			}
//		}, "access_token");
//	}

}
