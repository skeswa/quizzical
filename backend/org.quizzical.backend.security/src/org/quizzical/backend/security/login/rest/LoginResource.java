package org.quizzical.backend.security.login.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.commons.util.Validator;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.dao.user.UserNotFoundException;
import org.quizzical.backend.security.api.model.user.User;
import org.quizzical.backend.security.jwt.api.IJWTTokenService;
import org.quizzical.backend.security.jwt.api.SessionUser;
import com.fasterxml.jackson.core.JsonProcessingException;

@Path("auth")
public class LoginResource {
	private volatile IUserDAOService userService;
	private volatile IJWTTokenService tokenService;
	private volatile LogService logger;
	

	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(final Login login) throws ApplicationException {
		try {
			
			if (Validator.isNull(login))
				return Response.status(401).build();	

			final User user = userService.getUserByEmailAndPassword(login.getUsername(), login.getPassword());
			final SessionUser sessionUser = new SessionUser(user);
			final String token = tokenService.generateToken(sessionUser);

			NewCookie cookie = new NewCookie(IJWTTokenService.COOKIE_NAME, token, "/", "", "Authentication cookie", NewCookie.DEFAULT_MAX_AGE, false);
			return Response.ok().cookie(cookie).build();
		} catch(final UserNotFoundException ex) {
			return Response.status(401).build();
		} catch (JsonProcessingException e) {
			return Response.status(401).build();
		} 
	}

	@Path("whoami")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response me(@Context HttpServletRequest request) throws UnauthorizedAccessException {
		try {
			final String userJson = tokenService.extractSessionUserAsJson(request);
			if (userJson == null || "null".equalsIgnoreCase(userJson))
				return Response.status(403).build();
			NewCookie cookie = new NewCookie(IJWTTokenService.COOKIE_NAME, userJson, "/", "", "Authentication cookie", NewCookie.DEFAULT_MAX_AGE, false);
			return Response.ok().cookie(cookie).build();
		} catch (JsonProcessingException e) {
			return Response.status(401).build();
		} catch (IOException e) {
			return Response.status(401).build();
		} 
	}

	@Path("logout")
	@POST
	public Response logout() {
		NewCookie cookie = new NewCookie(IJWTTokenService.COOKIE_NAME, null, "/", "", "Authentication cookie", 0, false);
		return Response.ok().cookie(cookie).build();
	}
}
