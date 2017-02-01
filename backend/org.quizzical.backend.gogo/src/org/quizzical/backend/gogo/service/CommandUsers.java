package org.quizzical.backend.gogo.service;

import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;

public class CommandUsers {

    private final IUserDAOService userService;

    public CommandUsers(IUserDAOService userService) {
        this.userService = userService;
    }

    public IUserDAOService get() {
    	return userService;
    }
}
