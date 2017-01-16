package org.quizzical.backend.security.jwt.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface IJWTTokenService {
	public final static String COOKIE_NAME = "auth_token"; 
	
	String generateToken(final SessionUser user) throws JsonProcessingException;
	String extractUserAsJson(HttpServletRequest request) throws JsonProcessingException, IOException;
	SessionUser extractUser(HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException;
}
