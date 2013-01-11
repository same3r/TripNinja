package edu.columbia.tripninja.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserDetails implements Serializable {
	private String username;
	private String displayName;
	private boolean isBlocked;

	public UserDetails() {
		new UserDetails("0", "", false);
	}

	public UserDetails(String id, String displayName, boolean isBlocked) {
		this.username = id;
		this.displayName = displayName;
		this.isBlocked = isBlocked;
	}

	public String getId() {
		return username;
	}

	public void setId(String id) {
		this.username = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
}
