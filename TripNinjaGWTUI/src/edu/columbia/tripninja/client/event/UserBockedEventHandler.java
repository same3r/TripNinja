package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UserBockedEventHandler extends EventHandler {
  void onUserBockeded(UserBlockedEvent event);
}

