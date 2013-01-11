package edu.columbia.tripninja.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Auth implements Serializable {

	private String sessionId;
	private boolean isAdmin;
	private boolean isBlocked;

	public Auth(String sessionId, boolean isAdmin, boolean isBlocked) {
		this.sessionId = sessionId;
		this.isBlocked = isBlocked;
		this.isAdmin = isAdmin;
	}

	public Auth() {

	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

}
