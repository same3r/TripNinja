package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface AdminEventHandler  extends EventHandler{
	void onAdminAuth(AdminEvent event);
}
