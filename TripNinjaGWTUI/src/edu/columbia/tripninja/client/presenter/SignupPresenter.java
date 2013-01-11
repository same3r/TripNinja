package edu.columbia.tripninja.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import edu.columbia.tripninja.client.TripNinjaServiceAsync;
import edu.columbia.tripninja.client.event.LoginEvent;
import edu.columbia.tripninja.shared.Constants;
import edu.columbia.tripninja.shared.User;

public class SignupPresenter implements Presenter {

	public interface Display {
		HasClickHandlers getSignupButton();

		HasValue<String> getUsername();

		HasValue<String> getFirstName();

		HasValue<String> getLastName();

		HasValue<String> getEmailAddress();

		void setLabel(String str);

		Widget asWidget();

		HasValue<String> getPassword();
	}

	private final TripNinjaServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	private final User user;

	public SignupPresenter(TripNinjaServiceAsync rpcService,
			HandlerManager eventBus, Display display) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		this.user = new User();
		bind();
	}

	public void bind() {
		this.display.getSignupButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doSignup();
			}
		});
	}

	public void go(final HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	private void doSignup() {

		// Username should be 3 to 15 characters with any lower case character,
		// digit only.
		if (!display.getUsername().getValue().matches("^[a-z0-9]{3,15}$")) {
			Window.alert("Username should be  3 to 15 characters with any lower case character, digit only.");
			return;
		}

		if (!display.getFirstName().getValue().matches("^[a-zA-Z]{1,30}$")) {
			Window.alert("Firstname should be  1 to 30 characters with any lower case character and upper case letters only.");
			return;
		}

		if (!display.getLastName().getValue().matches("^[a-zA-Z]{1,30}$")) {
			Window.alert("Lastname should be  1 to 30 characters with any lower case character and upper case letters only.");
			return;
		}

		if (!display
				.getEmailAddress()
				.getValue()
				.matches(
						"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
								+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			Window.alert("Not a valid email address.");
			return;
		}

		if (!display
				.getPassword()
				.getValue()
				.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})")) {
			Window.alert("Password should be 6 to 20 characters string with at least one digit, one upper case letter, one lower case letter and one special symbol (@#$%).");
			return;
		}
		user.setUsername(display.getUsername().getValue());
		user.setFirstName(display.getFirstName().getValue());
		user.setLastName(display.getLastName().getValue());
		user.setEmailAddress(display.getEmailAddress().getValue());
		user.setPassword(display.getPassword().getValue());

		rpcService.addUser(user, new AsyncCallback<String>() {
			public void onSuccess(String result) {
				if (result.equals(Constants.USERNAME_NOT_EXISTS)) {
					eventBus.fireEvent(new LoginEvent("Signup Successful!"));
					Window.alert("Signup Successful!");
				} else if (result.equals(Constants.USERNAME_EXISTS)) {
					Window.alert("Please select a diffrent username!");
					display.setLabel("Username aready in use!");
				} else {
					Window.alert(Constants.DB_EXCEPTION);
				}
			}

			public void onFailure(Throwable caught) {
				Window.alert(Constants.RPC_EXCEPTION);
			}

		});
	}
}
