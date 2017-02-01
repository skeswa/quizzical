package org.quizzical.backend.security.authorization.api.dao.user;

public class UserNotFoundException extends Exception {
	public UserNotFoundException() {
		
	}
	
	public UserNotFoundException(String user) {
		super(user);
	}
}
