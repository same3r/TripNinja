package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface PasswordUpdatedEventHandler extends EventHandler{
  void onPasswordUpdated(PasswordUpdatedEvent event);
}
