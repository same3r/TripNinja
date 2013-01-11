package edu.columbia.tripninja.client.presenter;

import java.util.ArrayList;
import java.util.HashSet;
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
import edu.columbia.tripninja.client.event.GetDetailEvent;
import edu.columbia.tripninja.client.event.LogoutEvent;

public class SearchPresenter implements Presenter {

	private List<String> searchResult;

	public interface Display {
		HasClickHandlers getSearchButton();

		HasClickHandlers getNinjasPick();

		HasClickHandlers getVoteButton();

		void setData(List<String> data, HashSet<String> result);

		String getSearchText();

		Widget asWidget();

		int getClickedResult(ClickEvent Event);

		HasClickHandlers getList();

		List<Integer> getSelectedRows();

		HasClickHandlers getLogoutButton();

		void showVoteButton();

	}

	private final TripNinjaServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	private String username;
	private HashSet<String> likeSet;

	public SearchPresenter(TripNinjaServiceAsync rpcService,
			HandlerManager eventBus, Display view, String username) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
		this.username = username;
	}

	public void bind() {
		display.getSearchButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dofetchSearchResult();
			}
		});

		display.getNinjasPick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doFetchNinjasPick();
			}
		});

		display.getLogoutButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}
		});

		display.getVoteButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				postVote();
			}

		});

		display.getList().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int selectedRow = display.getClickedResult(event);

				if (selectedRow >= 0) {
					String id = searchResult.get(selectedRow);
					eventBus.fireEvent(new GetDetailEvent(id.split(" ")[0]));
				}
			}
		});

	}

	public void go(final HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
	}

	public void setData(List<String> searchResult) {
		this.searchResult = searchResult;
	}

	private void dofetchSearchResult() {

		String query = display.getSearchText();
		fetchSearchResult(query);
	}

	private void fetchSearchResult(String query) {
		rpcService.getSearchResult(query,
				new AsyncCallback<ArrayList<String>>() {
					public void onSuccess(ArrayList<String> result) {
						searchResult = result;

						rpcService.getLikesForUser(username,
								new AsyncCallback<HashSet<String>>() {
									public void onFailure(Throwable caught) {
									}

									@Override
									public void onSuccess(HashSet<String> result) {
										likeSet = result;
										display.setData(searchResult, result);
										display.showVoteButton();

									}
								});
					}

					public void onFailure(Throwable caught) {
					}
				});
	}

	private void doFetchNinjasPick() {
		rpcService.getNinjasPick(new AsyncCallback<ArrayList<String>>() {
			public void onSuccess(ArrayList<String> result) {
				searchResult = result;

				rpcService.getLikesForUser(username,
						new AsyncCallback<HashSet<String>>() {
							public void onFailure(Throwable caught) {
								Window.alert("Lucene index unavailable");
							}

							@Override
							public void onSuccess(HashSet<String> result) {
								likeSet = result;
								display.setData(searchResult, result);
								display.showVoteButton();
							}
						});
			}

			public void onFailure(Throwable caught) {
				Window.alert("Lucene index unavailable");
			}
		});
	}

	private void postVote() {
		List<Integer> selectedRows = display.getSelectedRows();
		ArrayList<String> placeIdList = new ArrayList<String>();

		for (int i = 0; i < selectedRows.size(); ++i) {
			String placeIdWithCount = searchResult.get(selectedRows.get(i));
			placeIdList.add(placeIdWithCount.split(" ")[0]);
			likeSet.remove(placeIdWithCount.split(" ")[0]);
			System.out.println(searchResult.get(selectedRows.get(i)));
		}

		rpcService.votePlaces(placeIdList, likeSet, username,
				new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						dofetchSearchResult();
						Window.alert("Vote Registered!");

					}

					public void onFailure(Throwable caught) {
						Window.alert("Vote failed");
					}

				});

	}
}
