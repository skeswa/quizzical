package org.quizzical.backend.security.api.dao.user;

public class UserExistsByEmailException extends Exception {
	public UserExistsByEmailException() {
		
	}
	
	public UserExistsByEmailException(String user) {
		super(user);
	}
}
