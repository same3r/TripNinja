package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditPasswordEvent extends GwtEvent<EditPasswordEventHandler> {
	public static Type<EditPasswordEventHandler> TYPE = new Type<EditPasswordEventHandler>();
	private final String id;

	public EditPasswordEvent(String id) {
		this.id = id;
	}

	public String getUsername() {
		return id;
	}

	@Override
	public Type<EditPasswordEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditPasswordEventHandler handler) {
		handler.onEditPassword(this);
	}
}
