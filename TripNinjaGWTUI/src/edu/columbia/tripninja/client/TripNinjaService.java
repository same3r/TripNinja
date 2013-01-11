package edu.columbia.tripninja.client;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.columbia.tripninja.shared.Auth;
import edu.columbia.tripninja.shared.User;
import edu.columbia.tripninja.shared.UserDetails;

@RemoteServiceRelativePath("tripninjaService")
public interface TripNinjaService extends RemoteService {

	Auth authenticate(String username, String password);

	String addUser(User user);

	ArrayList<String> getSearchResult(String query);

	ArrayList<UserDetails> getUserDetails();

	ArrayList<UserDetails> blockUsers(ArrayList<String> ids);

	User updateUser(User user);

	User getUser(String username);

	Integer newComment(String location, String comment, String username);

	ArrayList<String> getComments(String location);

	ArrayList<String> getInfo(String location);
	void votePlaces(ArrayList<String> ids, HashSet<String> likeSet,
			String username);

	HashSet<String> getLikesForUser(String username);
	
	ArrayList<String> getNinjasPick();

	String getPlaceIdAsync(String location);
}
