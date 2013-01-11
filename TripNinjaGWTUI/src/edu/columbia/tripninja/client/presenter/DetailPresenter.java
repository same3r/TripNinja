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
import edu.columbia.tripninja.client.event.LogoutEvent;
import edu.columbia.tripninja.client.event.MoreDetailsEvent;
import edu.columbia.tripninja.client.event.SignupEvent;

public class DetailPresenter implements Presenter {

	private final String location;
	private List<String> detailResult;
	private String comment;
	private String username;
	
	public interface Display {

		HasClickHandlers getPostButton();
		
		HasClickHandlers getMoreDetailsButton();

		void setData(List<String> data);

		Widget asWidget();
		
		String getComment();
		
		HasClickHandlers getLogoutButton();
		
		void clearCommentBox();


	}

	private final TripNinjaServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public DetailPresenter(TripNinjaServiceAsync rpcService,
			HandlerManager eventBus, Display view, String location, String username) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
		this.location = location;
		this.username = username;
	}
	
	
	public void bind() {
		display.getPostButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				comment = display.getComment(); 
				comment = comment.trim();
				display.clearCommentBox();
				if (comment.length() > 0)
					insertComment();
			}
		});
		
		display.getLogoutButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}
		});
		
		display.getMoreDetailsButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new MoreDetailsEvent());
			}
		});
	}

	public void go(final HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		dofetchDetailList();
		
	}


	

	public void insertComment() {
		rpcService.newComment(location, comment, username, new AsyncCallback<Integer>() {
			public void onSuccess(Integer Result) {
		
				dofetchDetailList();
			}
			public void onFailure(Throwable caught) {
				Window.alert("Error inserting comments");
			}
			
		});
	}
	
	private void dofetchDetailList() {
		fetchInfo(location);
		fetchComments(location);
	}
	
	private void fetchInfo(String location) {
		rpcService.getInfo(location, new AsyncCallback<ArrayList<String>>() {
					public void onSuccess(ArrayList<String> result) {
						detailResult = result;
						//display.setData(DetailResult);
					}
					public void onFailure(Throwable caught) {
						Window.alert("Error fetching information");
					}
				});
	}
	
	private void fetchComments(String location) {
		rpcService.getComments(location,new AsyncCallback<ArrayList<String>>() {
			public void onSuccess(ArrayList<String> result) {
				detailResult.addAll(result);
				display.setData(detailResult);
			}
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching comments");
			}			
		});
	}
}
