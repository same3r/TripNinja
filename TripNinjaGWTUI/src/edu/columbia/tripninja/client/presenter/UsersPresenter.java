package edu.columbia.tripninja.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import edu.columbia.tripninja.client.TripNinjaServiceAsync;
import edu.columbia.tripninja.client.event.EditPasswordEvent;
import edu.columbia.tripninja.client.event.LogoutEvent;
import edu.columbia.tripninja.shared.UserDetails;

public class UsersPresenter implements Presenter {

	private List<UserDetails> userDetails;

	public interface Display {

		HasClickHandlers getBlockButton();

		HasClickHandlers getList();

		void setData(List<String> data, List<Boolean> blockedStatusList);

		int getClickedRow(ClickEvent event);

		List<Integer> getSelectedRows();

		Widget asWidget();

		HasClickHandlers getLogoutButton();
	}

	private final TripNinjaServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public UsersPresenter(TripNinjaServiceAsync rpcService,
			HandlerManager eventBus, Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
	}

	public void bind() {
		
		display.getLogoutButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}
		});

		display.getBlockButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				blockSelectedUsers();
			}
		});

		display.getList().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int selectedRow = display.getClickedRow(event);

				if (selectedRow >= 0) {
					String id = userDetails.get(selectedRow).getId();
					eventBus.fireEvent(new EditPasswordEvent(id));
				}
			}
		});
	}

	public void go(final HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		fetchUserDetails();
	}

	public void setUserDetails(List<UserDetails> UserDetails) {
		this.userDetails = UserDetails;
	}

	public UserDetails getUserDetail(int index) {
		return userDetails.get(index);
	}

	private void fetchUserDetails() {
		rpcService.getUserDetails(new AsyncCallback<ArrayList<UserDetails>>() {
			public void onSuccess(ArrayList<UserDetails> result) {
				userDetails = result;
				List<String> data = new ArrayList<String>();
				List<Boolean> isBockedList = new ArrayList<Boolean>();

				for (int i = 0; i < result.size(); ++i) {
					data.add(userDetails.get(i).getDisplayName());
					isBockedList.add(userDetails.get(i).isBlocked());

				}

				display.setData(data, isBockedList);
			}

			public void onFailure(Throwable caught) {
				Window.alert("Error fetching User details");
			}
		});
	}

	private void blockSelectedUsers() {
		List<Integer> selectedRows = display.getSelectedRows();
		ArrayList<String> ids = new ArrayList<String>();

		for (int i = 0; i < selectedRows.size(); ++i) {
			ids.add(userDetails.get(selectedRows.get(i)).getId());
		}

		rpcService.blockUsers(ids, new AsyncCallback<ArrayList<UserDetails>>() {
			public void onSuccess(ArrayList<UserDetails> result) {
				userDetails = result;
				List<String> data = new ArrayList<String>();
				List<Boolean> isBockedList = new ArrayList<Boolean>();

				for (int i = 0; i < result.size(); ++i) {
					data.add(userDetails.get(i).getDisplayName());
					isBockedList.add(userDetails.get(i).isBlocked());
				}

				display.setData(data, isBockedList);

			}

			public void onFailure(Throwable caught) {
				Window.alert("Error deleting selected Users");
			}
		});
	}
}
