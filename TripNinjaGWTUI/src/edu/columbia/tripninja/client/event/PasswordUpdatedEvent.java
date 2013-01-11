package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.GwtEvent;

import edu.columbia.tripninja.shared.User;

public class PasswordUpdatedEvent extends GwtEvent<PasswordUpdatedEventHandler>{
  public static Type<PasswordUpdatedEventHandler> TYPE = new Type<PasswordUpdatedEventHandler>();
  private final User updatedUser;
  
  public PasswordUpdatedEvent(User updatedUser) {
    this.updatedUser = updatedUser;
  }
  
  public User getUpdatedContact() { return updatedUser; }
  

  @Override
  public Type<PasswordUpdatedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(PasswordUpdatedEventHandler handler) {
    handler.onPasswordUpdated(this);
  }
}
