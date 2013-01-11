package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditPasswordCancelledEvent extends GwtEvent<EditPasswordCancelledEventHandler>{
  public static Type<EditPasswordCancelledEventHandler> TYPE = new Type<EditPasswordCancelledEventHandler>();
  
  @Override
  public Type<EditPasswordCancelledEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(EditPasswordCancelledEventHandler handler) {
    handler.onEditPasswordCancelled(this);
  }
}
