package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoginEvent extends GwtEvent<LoginEventHandler> {
	public static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();
	private final String msg;

	public LoginEvent(String msg) {
		this.msg = msg;
	}

	@Override
	public Type<LoginEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginEventHandler handler) {
		handler.onSignup(this);
	}

	public String getMsg() {
		return msg;
	}

}
