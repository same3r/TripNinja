package edu.columbia.tripninja.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.columbia.tripninja.client.TripNinjaService;
import edu.columbia.tripninja.server.db.DatabaseConnector;
import edu.columbia.tripninja.server.utils.BCrypt;
import edu.columbia.tripninja.server.utils.SingletonLuceneUtils;
import edu.columbia.tripninja.shared.Auth;
import edu.columbia.tripninja.shared.Constants;
import edu.columbia.tripninja.shared.User;
import edu.columbia.tripninja.shared.UserDetails;

@SuppressWarnings("serial")
public class TripNinjaServiceImpl extends RemoteServiceServlet implements
		TripNinjaService {

	private final HashMap<String, User> users = new HashMap<String, User>();

	private void initUsers() {

		try {
			Statement st = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();
				st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT * from tn_user");
				populateUsers(rs);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateUsers(ResultSet resultSet) throws SQLException {

		while (resultSet.next()) {

			String username = resultSet.getString("username");
			String emailId = resultSet.getString("email_id");
			String firstName = resultSet.getString("first_name");
			String lastName = resultSet.getString("last_name");
			boolean isBlocked = resultSet.getInt("is_blocked") == 1 ? true
					: false;

			User user = new User(username, firstName, lastName, emailId,
					isBlocked);
			users.put(user.getUsername(), user);

		}
	}

	public User updateUser(User user) {
		try {
			PreparedStatement preparedStmt = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();

				String hashedPswd = BCrypt.hashpw(user.getPassword(),
						BCrypt.gensalt());
				String query = "update tn_user set password = ? where username = ?";
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, hashedPswd);
				preparedStmt.setString(2, user.getUsername());

				preparedStmt.executeUpdate();

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				preparedStmt.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	public Boolean blockUsers(String username) {

		boolean ret = false;
		try {
			PreparedStatement preparedStmt = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();

				String query = "update tn_user set is_blocked = ? where username = ?";
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setInt(1, 1);
				preparedStmt.setString(2, username);

				int i = preparedStmt.executeUpdate();
				if (i == 1) {
					ret = true;
				}
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				preparedStmt.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public ArrayList<UserDetails> blockUsers(ArrayList<String> ids) {

		clearBlockedUsers();
		for (int i = 0; i < ids.size(); ++i) {
			blockUsers(ids.get(i));
		}

		return getUserDetails();
	}

	private void clearBlockedUsers() {
		try {
			PreparedStatement preparedStmt = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();

				String query = "update tn_user set is_blocked = 0";
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.executeUpdate();

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				preparedStmt.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<UserDetails> getUserDetails() {
		ArrayList<UserDetails> userDetails = new ArrayList<UserDetails>();
		initUsers();

		Iterator<String> it = users.keySet().iterator();
		while (it.hasNext()) {
			User user = users.get(it.next());
			userDetails.add(user.getLightWeightUser());
		}

		return userDetails;
	}

	public User getUser(String username) {
		return users.get(username);
	}

	public Auth authenticate(String username, String candidatePassword) {

		try {
			String sessionId = null;
			Statement st = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();
				st = conn.createStatement();
				ResultSet rs = st
						.executeQuery("SELECT password, first_name, is_admin, is_blocked from tn_user WHERE username = '"
								+ username.trim().toLowerCase() + "'");
				if (rs.next()) {
					String hashedPwd = rs.getString(1);
					boolean isAdmin = rs.getInt(3) == 1 ? true : false;
					boolean isBlocked = rs.getInt(4) == 1 ? true : false;
					if (BCrypt.checkpw(candidatePassword, hashedPwd)) {
						sessionId = UUID.randomUUID().toString();
						return new Auth(sessionId, isAdmin, isBlocked);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String addUser(User user) {

		try {
			Connection conn = null;
			Statement st = null;
			try {
				conn = DatabaseConnector.getConn();
				st = conn.createStatement();
				ResultSet rs = st
						.executeQuery("SELECT * from tn_user WHERE username = '"
								+ user.getUsername() + "'");
				if (rs.first()) {
					return Constants.USERNAME_EXISTS;
				}
				String hashedPswd = BCrypt.hashpw(user.getPassword(),
						BCrypt.gensalt());
				st.executeUpdate("INSERT INTO tn_user " + "VALUES ('"
						+ user.getUsername().trim().toLowerCase() + "','"
						+ hashedPswd + "','"
						+ user.getFirstName().trim().toLowerCase() + "','"
						+ user.getLastName().trim().toLowerCase() + "','"
						+ user.getEmailAddress().trim().toLowerCase()
						+ "', 0,0)");
			} catch (Exception e) {
				e.printStackTrace();
				return Constants.DB_EXCEPTION;
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Constants.USERNAME_NOT_EXISTS;
	}

	public String findUser(String username) {

		try {
			Connection conn = null;
			Statement st = null;
			try {
				conn = DatabaseConnector.getConn();
				st = conn.createStatement();

				ResultSet rs = st
						.executeQuery("SELECT * from tn_user WHERE username = '"
								+ username + "'");
				if (rs.first()) {
					return Constants.USERNAME_EXISTS;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Constants.DB_EXCEPTION;
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Constants.USERNAME_NOT_EXISTS;
	}

	public ArrayList<String> getSearchResult(String query) {
		ArrayList<String> searchResults = new ArrayList<String>();
		ArrayList<String> temp = null;
		try {
			temp = SingletonLuceneUtils.searchPlaces(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (String placeId : temp) {
			int likeCount = getLikeCount(placeId);
			searchResults.add(placeId + " (" + likeCount + ")");
		}

		return searchResults;
	}

	private int getLikeCount(String placeId) {

		try {
			Statement st = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();
				st = conn.createStatement();
				ResultSet rs = st
						.executeQuery("SELECT no_of_votes from tn_places where place_id = '"
								+ placeId + "';");
				rs.next();
				return rs.getInt("no_of_votes");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Boolean setVotePlace(String username, String placeId) {
		boolean ret = false;
		try {
			Statement st = null;
			Connection conn = null;
			placeId = placeId.trim();

			try {
				conn = DatabaseConnector.getConn();

				String sql = "insert into tn_votes values('" + placeId + "', '"
						+ username + "')";
				st = conn.createStatement();
				int i = st.executeUpdate(sql);
				if (i == 1)
					ret = true;
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public void votePlaces(ArrayList<String> ids, HashSet<String> notLikedSet,
			String username) {

		for (String id : notLikedSet) {
			clearDislikedPlaces(id, username);
			computeTotalVotes(id);
		}
		for (int i = 0; i < ids.size(); ++i) {
			setVotePlace(username, ids.get(i));
			computeTotalVotes(ids.get(i));
		}

	}

	@Override
	public HashSet<String> getLikesForUser(String username) {

		HashSet<String> likeSet = null;

		try {
			Statement st = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();
				st = conn.createStatement();
				ResultSet rs = st
						.executeQuery("SELECT * from tn_votes where username = '"
								+ username + "';");
				likeSet = new HashSet<String>();
				while (rs.next()) {
					String place_Id = rs.getString("place_id");
					likeSet.add(place_Id);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return likeSet;
	}

	private void computeTotalVotes(String placeId) {
		try {
			PreparedStatement preparedStmt = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();

				String query = "update tn_places set no_of_votes = (select count(*) from tn_votes where place_id = ?) where place_id = ?";
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, placeId);
				preparedStmt.setString(2, placeId);

				preparedStmt.executeUpdate();

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				preparedStmt.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void clearDislikedPlaces(String placeId, String username) {
		try {
			Statement st = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();

				String sql = "delete from tn_votes  where username = '"
						+ username.trim().toLowerCase() + "' and place_id = '"
						+ placeId + "';";
				st = conn.createStatement();

				st.executeUpdate(sql);

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ArrayList<String> getInfo(String location) {

		ArrayList<String> a = new ArrayList<String>();
		try {
			String tmp = SingletonLuceneUtils.fetchDesc(location);
			if (tmp != "")
				a.add(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return a;
	}

	@Override
	public ArrayList<String> getComments(String location) {

		ArrayList<String> commentList = new ArrayList<String>();

		try {
			Statement st = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();
				st = conn.createStatement();
				ResultSet resultSet = st
						.executeQuery("SELECT username, user_comments from tn_comments where place_id ='"
								+ location + "';");
				while (resultSet.next()) {

					String username = resultSet.getString("username");
					String userComments = resultSet.getString("user_comments");

					commentList.add(username.toUpperCase() + ": "
							+ userComments);

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return commentList;
	}

	@Override
	public Integer newComment(String location, String comment, String username) {

		int y = 0;
		try {
			Connection conn = null;
			Statement st = null;
			try {
				conn = DatabaseConnector.getConn();
				st = conn.createStatement();
				comment = comment.replaceAll("'", "''");
				y = st.executeUpdate("Insert into tn_comments values ('"
						+ location + "', '" + username + "', '" + comment
						+ "' );");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				st.close();
				conn.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return y;

	}

	@Override
	public ArrayList<String> getNinjasPick() {

		ArrayList<String> searchResults = new ArrayList<String>();
		ArrayList<String> temp = null;
		Random randomGenerator = new Random();
		int randomDocNum = randomGenerator.nextInt(1000);
		try {
			temp = SingletonLuceneUtils.searchNinjasPick(randomDocNum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (String placeId : temp) {
			int likeCount = getLikeCount(placeId);
			searchResults.add(placeId + " (" + likeCount + ")");
		}

		return searchResults;
	}

	@Override
	public String getPlaceIdAsync(String location) {
		String placeId = "location";

		try {
			Statement st = null;
			Connection conn = null;
			try {
				conn = DatabaseConnector.getConn();
				st = conn.createStatement();
				ResultSet resultSet = st
						.executeQuery("SELECT city from tn_places where place_id ='"
								+ location + "';");
				while (resultSet.next()) {
					placeId = resultSet.getString("city");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return placeId.trim().toLowerCase();
	}

}
