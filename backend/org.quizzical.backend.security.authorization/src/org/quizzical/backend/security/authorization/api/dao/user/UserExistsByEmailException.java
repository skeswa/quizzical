package org.quizzical.backend.security.authorization.api.dao.user;

public class UserExistsByEmailException extends Exception {
	public UserExistsByEmailException() {
		
	}
	
	public UserExistsByEmailException(String user) {
		super(user);
	}
}
