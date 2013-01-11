package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class SignupEvent extends GwtEvent<SignupEventHandler> {
	public static Type<SignupEventHandler> TYPE = new Type<SignupEventHandler>();

	@Override
	public Type<SignupEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SignupEventHandler handler) {
		handler.onProceedingForSignup(this);
	}
}