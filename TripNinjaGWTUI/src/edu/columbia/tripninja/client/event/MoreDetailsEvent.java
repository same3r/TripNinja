package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class MoreDetailsEvent extends GwtEvent<MoreDetailsEventHandler> {
	public static Type<MoreDetailsEventHandler> TYPE = new Type<MoreDetailsEventHandler>();

	@Override
	public Type<MoreDetailsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MoreDetailsEventHandler handler) {
		handler.onMoreDetails(this);
	}

}
