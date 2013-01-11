package edu.columbia.tripninja.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.columbia.tripninja.client.event.AdminEvent;
import edu.columbia.tripninja.client.event.AdminEventHandler;
import edu.columbia.tripninja.client.event.EditPasswordCancelledEvent;
import edu.columbia.tripninja.client.event.EditPasswordCancelledEventHandler;
import edu.columbia.tripninja.client.event.EditPasswordEvent;
import edu.columbia.tripninja.client.event.EditPasswordEventHandler;
import edu.columbia.tripninja.client.event.GetDetailEvent;
import edu.columbia.tripninja.client.event.GetDetailEventHandler;
import edu.columbia.tripninja.client.event.LoginEvent;
import edu.columbia.tripninja.client.event.LoginEventHandler;
import edu.columbia.tripninja.client.event.LoginSuccessfulEvent;
import edu.columbia.tripninja.client.event.LoginSuccessfulEventHandler;
import edu.columbia.tripninja.client.event.LogoutEvent;
import edu.columbia.tripninja.client.event.LogoutEventHandler;
import edu.columbia.tripninja.client.event.MoreDetailsEvent;
import edu.columbia.tripninja.client.event.MoreDetailsEventHandler;
import edu.columbia.tripninja.client.event.PasswordUpdatedEvent;
import edu.columbia.tripninja.client.event.PasswordUpdatedEventHandler;
import edu.columbia.tripninja.client.event.SignupEvent;
import edu.columbia.tripninja.client.event.SignupEventHandler;
import edu.columbia.tripninja.client.presenter.DetailPresenter;
import edu.columbia.tripninja.client.presenter.EditUserPresenter;
import edu.columbia.tripninja.client.presenter.LoginPresenter;
import edu.columbia.tripninja.client.presenter.MoreDetailsPresenter;
import edu.columbia.tripninja.client.presenter.Presenter;
import edu.columbia.tripninja.client.presenter.SearchPresenter;
import edu.columbia.tripninja.client.presenter.SignupPresenter;
import edu.columbia.tripninja.client.presenter.UsersPresenter;
import edu.columbia.tripninja.client.view.DetailView;
import edu.columbia.tripninja.client.view.EditUserView;
import edu.columbia.tripninja.client.view.LoginView;
import edu.columbia.tripninja.client.view.MoreDetailsView;
import edu.columbia.tripninja.client.view.SearchView;
import edu.columbia.tripninja.client.view.SignupView;
import edu.columbia.tripninja.client.view.UsersView;

public class AppController implements Presenter, ValueChangeHandler<String> {
	private final HandlerManager eventBus;
	private final TripNinjaServiceAsync rpcService;
	private HasWidgets container;
	private String sessionID;
	private String location;
	private String username;

	public AppController(TripNinjaServiceAsync rpcService,
			HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);

		eventBus.addHandler(EditPasswordEvent.TYPE,
				new EditPasswordEventHandler() {
					public void onEditPassword(EditPasswordEvent event) {
						doEditUser(event.getUsername());
					}
				});

		eventBus.addHandler(EditPasswordCancelledEvent.TYPE,
				new EditPasswordCancelledEventHandler() {
					public void onEditPasswordCancelled(
							EditPasswordCancelledEvent event) {
						doEditUserCancelled();
					}
				});

		eventBus.addHandler(PasswordUpdatedEvent.TYPE,
				new PasswordUpdatedEventHandler() {
					public void onPasswordUpdated(PasswordUpdatedEvent event) {
						doContactUpdated();
					}
				});

		eventBus.addHandler(LoginSuccessfulEvent.TYPE,
				new LoginSuccessfulEventHandler() {
					@Override
					public void onLoginSuccessful(LoginSuccessfulEvent event) {
						sessionID = event.getSessionId();
						username = event.getUsername();
						doWelcome();

					}
				});

		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
			@Override
			public void onSignup(LoginEvent event) {
				doLoginScreenRedirect();

			}
		});

		eventBus.addHandler(SignupEvent.TYPE, new SignupEventHandler() {

			@Override
			public void onProceedingForSignup(SignupEvent event) {
				doProceedForSignup();

			}
		});

		eventBus.addHandler(AdminEvent.TYPE, new AdminEventHandler() {

			@Override
			public void onAdminAuth(AdminEvent event) {
				sessionID = event.getSessionId();
				username = event.getUsername();
				doAdminFunct();
			}
		});

		eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

			@Override
			public void onLogout(LogoutEvent event) {
				sessionID = null;
				username = null;
				doLogout();
			}

		});

		eventBus.addHandler(GetDetailEvent.TYPE, new GetDetailEventHandler() {
			@Override
			public void getDetail(GetDetailEvent event) {
				location = event.getLocation();
				doDetailScreenRedirect(location);

			}
		});
		
		eventBus.addHandler(MoreDetailsEvent.TYPE, new MoreDetailsEventHandler() {
			@Override
			public void onMoreDetails(MoreDetailsEvent event) {
				doFreeBaseRedirect();

			}
		});
	}

	private void doDetailScreenRedirect(String location) {
		History.newItem("detail");
	}

	private void doEditUser(String username) {
		History.newItem("edit", false);
		Presenter presenter = new EditUserPresenter(rpcService, eventBus,
				new EditUserView(), username);
		presenter.go(container);
	}

	private void doEditUserCancelled() {
		History.newItem("list");
	}

	private void doContactUpdated() {
		History.newItem("list");
	}

	private void doWelcome() {
		History.newItem("search");
	}
	
	private void doFreeBaseRedirect() {
		System.out.println(location);
		History.newItem("moreDetails");
	}

	private void doLoginScreenRedirect() {
		History.newItem("login");
	}

	private void doLogout() {
		History.newItem("logout");
	}

	private void doProceedForSignup() {
		History.newItem("signup");
	}

	private void doAdminFunct() {
		History.newItem("list");
	}

	public void go(final HasWidgets container) {
		this.container = container;

		if ("".equals(History.getToken())) {
			History.newItem("login");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();

		if (sessionID == null && !token.equals("signup")) {
			doLoginScreenRedirect();
			token = "login";
		}
		if (token != null) {
			Presenter presenter = null;

			if (token.equals("search")) {
				presenter = new SearchPresenter(rpcService, eventBus,
						new SearchView(), username);
			} else if (token.equals("add")) {
				presenter = new EditUserPresenter(rpcService, eventBus,
						new EditUserView());
			} else if (token.equals("edit")) {
				presenter = new EditUserPresenter(rpcService, eventBus,
						new EditUserView());
			} else if (token.equals("welcome")) {
				presenter = new UsersPresenter(rpcService, eventBus,
						new UsersView());
			} else if (token.equals("login")) {
				presenter = new LoginPresenter(rpcService, eventBus,
						new LoginView());
			} else if (token.equals("signup")) {
				presenter = new SignupPresenter(rpcService, eventBus,
						new SignupView());
			} else if (token.equals("list")) {
				presenter = new UsersPresenter(rpcService, eventBus,
						new UsersView());
			} else if (token.equals("detail")) {
				presenter = new DetailPresenter(rpcService, eventBus,
						new DetailView(), location, username);
			} else if (token.equals("logout")) {
				presenter = new LoginPresenter(rpcService, eventBus,
						new LoginView());
			}
			else if (token.equals("moreDetails")) {
				presenter = new MoreDetailsPresenter(rpcService, eventBus,
						new MoreDetailsView(),location);
			}
			if (presenter != null) {
				presenter.go(container);
			}
		}
	}
}
