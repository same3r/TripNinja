package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface SignupEventHandler extends EventHandler {
	void onProceedingForSignup(SignupEvent event);
}
