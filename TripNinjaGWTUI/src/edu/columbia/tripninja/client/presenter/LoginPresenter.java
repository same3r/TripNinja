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
import edu.columbia.tripninja.client.event.AdminEvent;
import edu.columbia.tripninja.client.event.LoginSuccessfulEvent;
import edu.columbia.tripninja.client.event.LogoutEvent;
import edu.columbia.tripninja.client.event.SignupEvent;
import edu.columbia.tripninja.shared.Auth;

public class LoginPresenter implements Presenter {

	public interface Display {

		HasClickHandlers getLoginButton();

		HasClickHandlers getSignupButton();

		HasValue<String> getUsername();

		HasValue<String> getPassword();

		Widget asWidget();
	}

	private final TripNinjaServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public LoginPresenter(TripNinjaServiceAsync rpcService,
			HandlerManager eventBus, Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
	}

	public void bind() {
		display.getLoginButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doLogin(display.getUsername().getValue(), display.getPassword()
						.getValue());
			}
		});

		display.getSignupButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new SignupEvent());
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
	}

	private void doLogin(final String username, String password) {

		// Username should be 3 to 15 characters with any lower case character,
		// digit or special symbol “_-” only.
		if (!username.matches("^[a-z0-9]{3,15}$")) {
			Window.alert("Username should be  3 to 15 characters with any lower case character, digit or special symbol.");
			return;
		}


		rpcService.authenticate(username, password, new AsyncCallback<Auth>() {
			public void onSuccess(Auth result) {

				if (result != null) {
					String sessionId = result.getSessionId();
					boolean isAdmin = result.isAdmin();
					boolean isBlocked = result.isBlocked();
					if (isAdmin)
						eventBus.fireEvent(new AdminEvent(sessionId, username));
					else if (isBlocked)
						Window.alert("Your account has been blocked. Please contact TripNinja admin.");
					else
						eventBus.fireEvent(new LoginSuccessfulEvent(sessionId,
								username));
				} else {
					Window.alert("Invaid username or password!");
				}

			}

			public void onFailure(Throwable caught) {
				Window.alert("Error encountered while logging you in!");
			}
		});
	}
}
