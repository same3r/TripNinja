package edu.coumbia.tripninja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import edu.columbia.tripninja.server.TripNinjaServiceImpl;
import edu.columbia.tripninja.shared.Auth;
import edu.columbia.tripninja.shared.User;
import edu.columbia.tripninja.shared.UserDetails;

public class TripNinjaServiceImplTest {

	@Test
	public void testUpdateUser() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		User y = new User("jaimin1", "jaimin", "shah", "xyz", true);
		y.setPassword("shah");
		x.updateUser(y);
		Auth u = x.authenticate("jaimin1", "shah");
		assertEquals(false, u.isAdmin());
	}

	@Test
	public void testBlockUsersString() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		ArrayList<String> usernameList = new ArrayList<String>();

		x.blockUsers(usernameList);

		String username = "jaimin1";
		assertTrue("Check whether user a blocked", x.blockUsers(username));

		username = "Fake_User";
		assertFalse("Check whether fake user is not blocked",
				x.blockUsers(username));

	}

	@Test
	public void testBlockUsersArrayListOfString() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		ArrayList<String> usernameList = new ArrayList<String>();
		usernameList.add("jaimin1");
		usernameList.add("jaimin");
		ArrayList<UserDetails> userList = x.blockUsers(usernameList);

		boolean areBlocked = true;
		for (UserDetails uD : userList) {
			if ((uD.getId() == "jaimin1" || uD.getId() == "jaimin")
					&& !uD.isBlocked())
				areBlocked = false;
		}
		assertTrue("Check whether user a blocked", areBlocked);
	}

	@Test
	public void testGetUserDetails() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		ArrayList<UserDetails> y = x.getUserDetails();
		assertEquals(y.size(), 4);
	}

	@Test
	public void testAuthenticate() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		Auth u = x.authenticate("admin", "tiger");
		assertEquals(true, u.isAdmin());
		assertEquals(false, u.isBlocked());

	}

	@Test
	public void testAddUser() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		User y = new User("jaimin1", "jaimin", "shah", "xyz", true);
		y.setPassword("shah1");
		x.addUser(y);
		Auth u = x.authenticate("jaimin1", "shah");
		assertEquals(false, u.isAdmin());
		//assertEquals(true, u.isBlocked());
	}

	@Test
	public void testFindUser() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		assertEquals(x.findUser("jaimin1"),"username_exists");
		assertNotSame(x.findUser("jaimin2"), "username_exists");
	}

	@Test
	public void testGetSearchResult() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		String query = "china town";
		ArrayList<String> searchResults = x.getSearchResult(query);
		assertTrue("Check whether search is working", searchResults.size() > 0);
	}

	@Test
	public void testSetVotePlace() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		HashSet<String> notLikedSet = new HashSet<String>();
		ArrayList<String> ids = new ArrayList<String>();
		HashSet<String> getLikes = new HashSet<String>();

		getLikes = x.getLikesForUser("jaimin1");
		for (String id : getLikes) {
			notLikedSet.add(id);
		}
		x.votePlaces(ids, notLikedSet, "jaimin1");

		ids.add("Chicago_Illinois");
		x.setVotePlace("jaimin1", "Chicago_Illinois");
		getLikes = x.getLikesForUser("jaimin1");
		assertTrue("Check whether likes added", getLikes.size() == 1);
	}

	@Test
	public void testVotePlaces() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		HashSet<String> notLikedSet = new HashSet<String>();
		ArrayList<String> ids = new ArrayList<String>();
		HashSet<String> getLikes = new HashSet<String>();

		getLikes = x.getLikesForUser("jaimin1");
		for (String id : getLikes) {
			notLikedSet.add(id);
		}
		x.votePlaces(ids, notLikedSet, "jaimin1");
		notLikedSet.clear();

		ids.add("Chicago_Illinois");
		ids.add("Abilene_Texas");
		x.votePlaces(ids, notLikedSet, "jaimin1");
		getLikes = x.getLikesForUser("jaimin1");
		assertTrue("Check whether likes added", getLikes.size() == 2);

		ids.clear();
		notLikedSet.add("Chicago_Illinois");
		notLikedSet.add("Abilene_Texas");
		x.votePlaces(ids, notLikedSet, "jaimin1");
		getLikes = x.getLikesForUser("jaimin1");
		assertTrue("Check whether dislike registered", getLikes.size() == 0);

	}

	@Test
	public void testGetLikesForUser() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		HashSet<String> desc = new HashSet<String>();
		desc = x.getLikesForUser("jaimin1");
		assertTrue("Check whether like set is generated", desc != null);
		desc = x.getLikesForUser("Fake_User");
		assertFalse("Checks whether like set is not generated", desc == null);
	}

	@Test
	public void testGetInfo() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		ArrayList<String> desc = new ArrayList<String>();
		desc = x.getInfo("Chicago_Illinois");
		assertTrue("Test if location exists", desc.size() > 0);
		desc = x.getInfo("Fake_Location");
		assertFalse("Test if location does not exist", desc.size() > 0);
	}

	@Test
	public void testGetComments() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		int z = x.newComment("Allen_Texas", "test1", "jaimin");
		assertEquals(z, 1);
		ArrayList<String> z1 = x.getComments("Allen_Texas");
		assertTrue("asdsagfasdf", z1.size() > 0);
		assertEquals(z1.get(0).contains("JAIMIN"), true);
	}

	@Test
	public void testNewComment() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		int z = x.newComment("Chicago_Illinois", "hey", "jaimin");
		assertEquals(z, 1);
		z = x.newComment("Chicago_Illinois", "hey",
				"jaisdad");
		assertEquals(z, 0);
	}

	@Test
	public void testGetNinjasPick() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		x.getNinjasPick();
		assertNotSame("Random location works for ninja pick", x.getNinjasPick()
				.get(0), x.getNinjasPick().get(0));
	}


	@Test
	public void testGetPlaceIdAsync() {
		TripNinjaServiceImpl x = new TripNinjaServiceImpl();
		assertEquals("getPlaceIdAsync works for a valid case", "chicago",
				x.getPlaceIdAsync("Chicago_Illinois"));
		assertNotSame("getPlaceIdAsync doesn't work for a invalid case",
				"chicago", x.getPlaceIdAsync("chicago_Illinois"));
	}

}
