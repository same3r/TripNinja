package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoginSuccessfulEvent extends GwtEvent<LoginSuccessfulEventHandler> {
	private final String sessionId;
	private final String username;
	public static Type<LoginSuccessfulEventHandler> TYPE = new Type<LoginSuccessfulEventHandler>();

	public LoginSuccessfulEvent(String sessionId, String username) {
		this.sessionId = sessionId;
		this.username = username;
	}

	@Override
	public Type<LoginSuccessfulEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginSuccessfulEventHandler handler) {
		handler.onLoginSuccessful(this);
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getUsername() {
		return username;
	}
}
