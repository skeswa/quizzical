package org.quizzical.backend.security.login.rest;

import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.amdatu.security.tokenprovider.InvalidTokenException;
import org.amdatu.security.tokenprovider.TokenProvider;
import org.amdatu.security.tokenprovider.TokenProviderException;
import org.gauntlet.core.api.ApplicationException;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.model.user.User;

public class SecuredResource {
	
	protected volatile TokenProvider tokenProvider;
	protected volatile IUserDAOService userService;
	
	protected String getEmailFromToken(HttpServletRequest request)
			throws TokenProviderException, InvalidTokenException {
		String token = tokenProvider.getTokenFromRequest(request);
		SortedMap<String, String> userDetails = tokenProvider.verifyToken(token);
		
		String customerEmail = userDetails.get(TokenProvider.USERNAME);
		return customerEmail;
	}
	
	protected User getUserFromToken(HttpServletRequest request)
			throws TokenProviderException, InvalidTokenException, ApplicationException {
		final String userEmailAddress = getEmailFromToken(request);
		final User user = userService.getByEmail(userEmailAddress);
		return user;
	}	
}
