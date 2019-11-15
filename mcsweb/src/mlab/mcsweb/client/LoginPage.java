package mlab.mcsweb.client;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.services.GreetingService;
import mlab.mcsweb.client.services.GreetingServiceAsync;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.User;
import mlab.mcsweb.shared.Util;

public class LoginPage extends Composite {

	@UiField
	Form formLogin, formSignup;
	@UiField
	TextBox textLoginEmail, textSignupEmail;
	@UiField
	Input textLoginPassword, textSignupPassword, textConfirmPassword;
	@UiField
	Button buttonLoginAction, buttonSignupAction, buttonAlreadyAccount;
	@UiField
	Label labelLoginEmailFormatError, labelLoginPasswordError, labelLoginError, labelSignupEmailFormatError,
			labelPasswordMatchError, labelSignupError;

	@UiField
	Heading authLabel;

	Button buttonCreateAccount, buttonForgotPass;

	boolean statusLoginEmailFormatError = false, statusLoginPasswordError = false, statusFirstNameError = false,
			statusLastNameError = false, statusSignupEmailError = false, statusPasswordMatchError = false;
	
	private Mcsweb application = null;
	

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private static LoginPageUiBinder uiBinder = GWT.create(LoginPageUiBinder.class);

	interface LoginPageUiBinder extends UiBinder<Widget, LoginPage> {
	}

	public LoginPage(Mcsweb application) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.application = application;
		formLogin.setVisible(true);
		formSignup.setVisible(false);
	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		authLabel.setText("");
		enableLoginForm(null);
		resetLogin();
		resetSignup();
	}

	@UiHandler("buttonCreateAccount")
	void enableSignupForm(ClickEvent event) {
		formSignup.setVisible(true);
		resetSignup();
		formLogin.setVisible(false);
	}

	@UiHandler("buttonAlreadyAccount")
	void enableLoginForm(ClickEvent event) {
		formLogin.setVisible(true);
		resetLogin();
		formSignup.setVisible(false);
	}

	private void resetLogin() {
		textLoginEmail.setText("");
		textLoginPassword.setText("");
		labelLoginEmailFormatError.setText("");
		labelLoginPasswordError.setText("");
		labelLoginError.setText("");
	}

	private void resetSignup() {
		textSignupEmail.setText("");
		textSignupPassword.setText("");
		textConfirmPassword.setText("");
		labelSignupEmailFormatError.setText("");
		labelPasswordMatchError.setText("");
		labelSignupError.setText("");
	}

	@UiHandler("textLoginEmail")
	void loginEmailValueChangedAction(ValueChangeEvent<String> event) {
		if (Util.isEmailFOrmatValid(textLoginEmail.getText())) {
			labelLoginEmailFormatError.setText("");
			statusLoginEmailFormatError = false;
		} else {
			labelLoginEmailFormatError.setText("Invalid Email");
			statusLoginEmailFormatError = true;
		}
	}

	@UiHandler("textLoginPassword")
	void loginPasswordValueChangedAction(ValueChangeEvent<String> event) {
		if (textLoginPassword.getText().isEmpty()) {
			labelLoginPasswordError.setText("Password can't be empty");
			statusLoginPasswordError = true;
		} else {
			labelLoginPasswordError.setText("");
			statusLoginPasswordError = false;
		}
	}

	private boolean validationBeforeReset() {
		if (textLoginEmail.getText().isEmpty()) {
			labelLoginEmailFormatError.setText("Email can't be empty");
			statusLoginEmailFormatError = true;
		}

		if (statusLoginEmailFormatError) {
			return false;
		}
		return true;
	}

	private boolean validationBeforeLogin() {
		if (textLoginEmail.getText().isEmpty()) {
			labelLoginEmailFormatError.setText("Email can't be empty");
			statusLoginEmailFormatError = true;
		}

		if (textLoginPassword.getText().isEmpty()) {
			labelLoginPasswordError.setText("Password can't be empty");
			statusLoginPasswordError = true;
		}

		if (statusLoginEmailFormatError || statusLoginPasswordError) {
			return false;
		}
		return true;
	}

	@UiHandler("textSignupEmail")
	void signupEmailChnagedAction(ValueChangeEvent<String> event) {
		if (Util.isEmailFOrmatValid(textSignupEmail.getText())) {
			labelSignupEmailFormatError.setText("");
			statusSignupEmailError = false;
		} else {
			labelSignupEmailFormatError.setText("Invalid Email");
			statusSignupEmailError = true;
		}
	}

	@UiHandler({ "textSignupPassword", "textConfirmPassword" })
	void passwordMatchAction(ValueChangeEvent<String> event) {
		if (textSignupPassword.getText().equals(textConfirmPassword.getText())) {
			labelPasswordMatchError.setText("");
			statusPasswordMatchError = false;
		} else {
			labelPasswordMatchError.setText("Password doesn't match");
			statusPasswordMatchError = true;
		}
	}

	private boolean validationBeforeSignup() {

		if (textSignupEmail.getText().isEmpty()) {
			labelSignupEmailFormatError.setText("Invalid Email");
			statusSignupEmailError = true;
		}
		if (textSignupPassword.getText().isEmpty() || textConfirmPassword.getText().isEmpty()) {
			labelPasswordMatchError.setText("Password can't be empty");
			statusPasswordMatchError = true;
		}

		if (statusFirstNameError || statusLastNameError || statusSignupEmailError || statusPasswordMatchError) {
			return false;
		}
		return true;
	}

	@UiHandler("buttonSignupAction")
	void signupAction(ClickEvent event) {
		if (this.validationBeforeSignup()) {
			buttonSignupAction.setEnabled(false);
			final String email = textSignupEmail.getText().trim();
			final String password = textSignupPassword.getText().trim();

			final User user = new User();
			user.setEmail(email);
			try {
				user.setPassword(Util.getMD5String(password));
			} catch (Exception e) {
				// TODO: handle exception
				// Window.alert("have caught an exception" + e.getMessage());
				return;
			}

			try {

				greetingService.signup(user, new AsyncCallback<Response>() {
					public void onFailure(Throwable caught) {
						labelSignupError.setText("Service is not available, please try again later!");
						buttonSignupAction.setEnabled(true);
					};

					public void onSuccess(Response result) {
						buttonSignupAction.setEnabled(true);
						if (result.getCode() == 0) {
							// application.signedUP(email);
							resetSignup();
							enableLoginForm(null);

							String signupMessage = "Thank you for registering, please check your mailbox for a confirmation email to activate your account.";
							authLabel.setText(signupMessage);
						} else {
							labelSignupError.setText(result.getMessage());
						}
					};
				});
			} catch (Exception e) {
				// TODO: handle exception
				Window.alert("Service is not available, please try again later!");
				buttonSignupAction.setEnabled(true);
			}

		}
	}

	@UiHandler("buttonLoginAction")
	void loginAction(ClickEvent event) {
		/*
		 * if (textLoginEmail.getText().isEmpty() &&
		 * textLoginPassword.getText().isEmpty()) {
		 * application.loggedIn("z@gmail.com", "test");
		 * 
		 * } else {
		 */
		if (this.validationBeforeLogin()) {
			buttonLoginAction.setEnabled(false);
			User user = new User();
			user.setEmail(textLoginEmail.getText().trim());
			try {
				user.setPassword(Util.getMD5String(textLoginPassword.getText().trim()));
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}

			greetingService.login(user, new AsyncCallback<Response>() {

				@Override
				public void onSuccess(Response response) {
					// TODO Auto-generated method stub
					buttonLoginAction.setEnabled(true);
					if (response.getCode() == 0) {
						application.loggedIn(textLoginEmail.getText().trim());
					} else {
						labelLoginError.setText(response.getMessage());
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					buttonLoginAction.setEnabled(true);
					labelLoginError.setText("Service is not available, please try later!");
				}
			});
		}

		// }

	}

}
