package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LoginEventHandler extends EventHandler {
	void onSignup(LoginEvent event);

}
