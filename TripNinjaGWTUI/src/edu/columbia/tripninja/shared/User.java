package edu.columbia.tripninja.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String username;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String password;
	private boolean isAdmin;
	private boolean isBlocked;

	public User() {
	}

	public User(String username, String firstName, String lastName,
			String emailAddress, String password) {
		this.username = username.trim().toLowerCase();
		this.firstName = firstName.trim().toLowerCase();
		this.lastName = lastName.trim().toLowerCase();
		this.emailAddress = emailAddress.trim().toLowerCase();
		this.password = password;
	}

	public User(String username, String firstName, String lastName,
			String emailId, boolean isBlocked) {
		this.username = username.trim().toLowerCase();
		this.firstName = firstName.trim().toLowerCase();
		this.lastName = lastName.trim().toLowerCase();
		this.emailAddress = emailId.trim().toLowerCase();
		this.setBlocked(isBlocked);
	}

	public UserDetails getLightWeightUser() {
		return new UserDetails(username, getFullName(), isBlocked);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.trim().toLowerCase();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.trim().toLowerCase();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.trim().toLowerCase();
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress.trim().toLowerCase();
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
