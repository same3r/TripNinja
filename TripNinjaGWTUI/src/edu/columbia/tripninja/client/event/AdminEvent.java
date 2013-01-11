package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class AdminEvent extends GwtEvent<AdminEventHandler> {
	public static Type<AdminEventHandler> TYPE = new Type<AdminEventHandler>();

	private String sessionId;
	private String username;

	public AdminEvent(String sessionId, String username) {
		this.sessionId = sessionId;
	}

	@Override
	public Type<AdminEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AdminEventHandler handler) {
		handler.onAdminAuth(this);

	}

	public String getSessionId() {
		return sessionId;
	}

	public String getUsername() {
		return username;
	}

}