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
import edu.columbia.tripninja.client.event.EditPasswordCancelledEvent;
import edu.columbia.tripninja.client.event.LogoutEvent;
import edu.columbia.tripninja.client.event.PasswordUpdatedEvent;
import edu.columbia.tripninja.shared.User;

public class EditUserPresenter implements Presenter {
	public interface Display {
		HasClickHandlers getSaveButton();

		HasClickHandlers getCancelButton();

		HasValue<String> getPassword();

		Widget asWidget();

		HasClickHandlers getLogoutButton();
	}

	private User user;
	private final TripNinjaServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public EditUserPresenter(TripNinjaServiceAsync rpcService,
			HandlerManager eventBus, Display display) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.user = new User();
		this.display = display;
		bind();
	}

	public EditUserPresenter(TripNinjaServiceAsync rpcService,
			HandlerManager eventBus, Display display, String id) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		bind();

		rpcService.getUser(id, new AsyncCallback<User>() {
			public void onSuccess(User result) {
				user = result;
				EditUserPresenter.this.display.getPassword().setValue(
						user.getPassword());
			}

			public void onFailure(Throwable caught) {
				Window.alert("Error retrieving contact");
			}
		});

	}

	public void bind() {
		this.display.getSaveButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doSave();
			}
		});

		this.display.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new EditPasswordCancelledEvent());
			}
		});

		display.getLogoutButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}
		});
	}

	public void go(final HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	private void doSave() {
		
		if (!display
				.getPassword()
				.getValue()
				.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})")) {
			Window.alert("Password should be 6 to 20 characters string with at least one digit, one upper case letter, one lower case letter and one special symbol (@#$%).");
			return;
		}
		user.setPassword(display.getPassword().getValue());

		rpcService.updateUser(user, new AsyncCallback<User>() {
			public void onSuccess(User result) {
				eventBus.fireEvent(new PasswordUpdatedEvent(result));
			}

			public void onFailure(Throwable caught) {
				Window.alert("Error updating contact");
			}
		});
	}

}
