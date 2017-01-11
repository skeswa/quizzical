package org.quizzical.backend.security.api.dao.user;

import java.util.List;

import org.quizzical.backend.security.api.model.user.User;


public interface IUserDAOService {
	public User get(long id);
	
	public List<User> getAll();
	
	public User getByEmail(String email);
	
	public User getByCode(String code);	

	public User add(User record);

	public void delete(User record);
	
	public void delete(String recordId);

	public User update(User record);
	
	public User provide(User record) throws Exception;

	public List<User> getAllActiveUsers();
	
	public User getUserByEmailAndPassword(String email, String password) throws UserNotFoundException;

	public User getUserByEmail(String email) throws UserNotFoundException;
}
