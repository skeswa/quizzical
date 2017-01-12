package org.quizzical.backend.security.login.rest;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.amdatu.security.tokenprovider.InvalidTokenException;
import org.amdatu.security.tokenprovider.TokenProvider;
import org.amdatu.security.tokenprovider.TokenProviderException;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.commons.util.Validator;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.dao.user.UserNotFoundException;
import org.quizzical.backend.security.api.model.user.User;

@Path("idm/security")
public class LoginResource {
	
	private volatile IUserDAOService userService;
	
	private volatile TokenProvider tokenProvider;

	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@FormParam("email") String email, @FormParam("password") String password) throws ApplicationException {
		try {
			if (Validator.isNull(password))
				return Response.status(401).build();	

			userService.getUserByEmailAndPassword(email, password);
			SortedMap<String, String> userMap = new TreeMap<>();
			userMap.put(TokenProvider.USERNAME, email);

			NewCookie cookie = new NewCookie(TokenProvider.TOKEN_COOKIE_NAME, tokenProvider.generateToken(userMap), "/", "", "Authentication cookie", NewCookie.DEFAULT_MAX_AGE, false);
			return Response.ok().cookie(cookie).build();
		} catch(UserNotFoundException ex) {
			return Response.status(401).build();
		} catch (TokenProviderException e) {
			return Response.serverError().entity("Error while logging in").build();
		}
	}

	@Path("me")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public User me(@Context HttpServletRequest request) throws TokenProviderException, InvalidTokenException, UserNotFoundException, ApplicationException {
		String token = tokenProvider.getTokenFromRequest(request);
		SortedMap<String, String> userMap = tokenProvider.verifyToken(token);
		return userService.getUserByEmail(userMap.get(TokenProvider.USERNAME));
	}

	@Path("logout")
	@POST
	public void logout(@Context HttpServletRequest request) {
		String token = tokenProvider.getTokenFromRequest(request);
		tokenProvider.invalidateToken(token);
	}
}
