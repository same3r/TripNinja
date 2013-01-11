package edu.columbia.tripninja.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import edu.columbia.tripninja.client.TripNinjaServiceAsync;
import edu.columbia.tripninja.client.event.LogoutEvent;

public class MoreDetailsPresenter implements Presenter {

	public interface Display {
		
		void setPlaceId(String placeId);
		Widget asWidget();

		HasClickHandlers getLogoutButton();
	}

	private final TripNinjaServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	private String location;

	public MoreDetailsPresenter(TripNinjaServiceAsync rpcService,
			HandlerManager eventBus, Display view, String location) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
		this.location = location;
	}

	public void bind() {
		
		display.getLogoutButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		getPlaceId();
		container.add(display.asWidget());
	}

	private void getPlaceId() {
		rpcService.getPlaceIdAsync(location, new AsyncCallback<String>() {
			public void onSuccess(String result) {				
				display.setPlaceId(result);
			}
			public void onFailure(Throwable caught) {
				display.setPlaceId(location.trim().toLowerCase());
			}
			
		});
	}
}
