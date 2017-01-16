package org.quizzical.backend.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.io.IOException;
import java.security.Key;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Dictionary;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.model.user.User;
import org.quizzical.backend.security.jwt.api.IJWTTokenService;
import org.quizzical.backend.security.jwt.api.SessionUser;

@Path("/")
public class JWTTokenServiceImpl implements ManagedService, IJWTTokenService {
	public final static String PROP_JWT_KEY = "encryptKey";
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private String jwtKey;
	
	private volatile LogService logger;
	
	private volatile IUserDAOService userService;
	
	@Override
	public User extractUser(final HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, ApplicationException {
		final SessionUser sessionUser = extractSessionUser(request);
		return userService.getByEmail(sessionUser.getEmail());
	}	
	
	@Override
	public SessionUser extractSessionUser(final HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		String token = extractCookieToken(request);
		if (token != null) {
			final String userJson = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody().getSubject();
			final SessionUser user = mapper.readValue(userJson, SessionUser.class);
			return user;
		} 
		return null;
	}
	
	@Override
	public String extractSessionUserAsJson(final HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		final SessionUser user = extractSessionUser(request);
		return mapper.writeValueAsString(user);
	}	

	private String extractCookieToken(final HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(IJWTTokenService.COOKIE_NAME)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public String generateToken(final SessionUser user) throws JsonProcessingException {
		final String subject = mapper.writeValueAsString(user);
		final String compactJws = Jwts.builder()
				  .setSubject(subject)
				  .signWith(SignatureAlgorithm.HS512, jwtKey)
				  .compact();
		return compactJws;
	}

	@Override
	public void updated(final Dictionary<String, ?> properties) throws ConfigurationException {
		if (properties != null)
			this.jwtKey = (String) properties.get(PROP_JWT_KEY);
	}
}
