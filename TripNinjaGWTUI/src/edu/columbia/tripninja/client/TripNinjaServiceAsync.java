package edu.columbia.tripninja.client;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.columbia.tripninja.shared.Auth;
import edu.columbia.tripninja.shared.User;
import edu.columbia.tripninja.shared.UserDetails;

public interface TripNinjaServiceAsync {

	public void getSearchResult(String query,
			AsyncCallback<ArrayList<String>> callback);

	public void authenticate(String username, String password,
			AsyncCallback<Auth> asyncCallback);

	public void addUser(User user, AsyncCallback<String> callback);

	public void getUserDetails(
			AsyncCallback<ArrayList<UserDetails>> asyncCallback);

	public void blockUsers(
			ArrayList<String> ids,
			AsyncCallback<ArrayList<edu.columbia.tripninja.shared.UserDetails>> asyncCallback);

	public void updateUser(User user, AsyncCallback<User> asyncCallback);

	public void getUser(String id, AsyncCallback<User> asyncCallback);

	public void getComments(String location,
			AsyncCallback<ArrayList<String>> callback);

	void newComment(String location, String comment, String username,
			AsyncCallback<Integer> callback);

	void getInfo(String location, AsyncCallback<ArrayList<String>> callback);
	
	void votePlaces(ArrayList<String> ids, HashSet<String> likeSet, String username,
			AsyncCallback<Void> callback);

	void getLikesForUser(String username,
			AsyncCallback<HashSet<String>> callback);

	void getNinjasPick(AsyncCallback<ArrayList<String>> callback);

	public void getPlaceIdAsync(String location,
			AsyncCallback<String> asyncCallback);

}
