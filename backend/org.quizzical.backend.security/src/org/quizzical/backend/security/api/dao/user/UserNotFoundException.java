package org.quizzical.backend.security.api.dao.user;

public class UserNotFoundException extends Exception {
	public UserNotFoundException() {
		
	}
	
	public UserNotFoundException(String user) {
		super(user);
	}
}
