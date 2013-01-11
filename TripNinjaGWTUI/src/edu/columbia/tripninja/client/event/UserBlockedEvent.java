package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UserBlockedEvent extends GwtEvent<UserBockedEventHandler>{
  public static Type<UserBockedEventHandler> TYPE = new Type<UserBockedEventHandler>();
  
  @Override
  public Type<UserBockedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(UserBockedEventHandler handler) {
    handler.onUserBockeded(this);
  }
}
