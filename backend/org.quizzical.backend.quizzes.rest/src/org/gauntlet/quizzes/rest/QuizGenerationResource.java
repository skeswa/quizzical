package org.gauntlet.quizzes.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.dao.user.UserNotFoundException;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.security.authentication.jwt.api.IJWTTokenService;
import org.quizzical.backend.security.authentication.jwt.api.SessionUser;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("quiz/generations")
public class QuizGenerationResource {
	@SuppressWarnings("unused")
	private volatile LogService logger;
	private ObjectMapper mapper = new ObjectMapper();
	
	private volatile IJWTTokenService tokenService;
	private volatile IUserDAOService userService;
	private volatile IQuizDAOService quizService;
	
	@Path("whattype")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response queryGeneratorType(@Context HttpServletRequest request) throws UserNotFoundException, ApplicationException {
		try {
			final SessionUser sessionUser = tokenService.extractSessionUser(request);
			final User user = userService.getUserByEmail(sessionUser.getEmail());
			boolean isDiagnosed = quizService.userHasTakenDiagnoticTest(user);
			
			String genType = null;
			if (!isDiagnosed)
				genType = "realistic";
			else
				genType = "generate_by_weakness";
			
			final String typeJson = mapper.readTree(String.format("{\"gentype\":\"%s\"}",genType)).toString();
			return Response.ok().entity(typeJson).build();
		} catch (JsonProcessingException e) {
			return Response.status(401).build();
		} catch (IOException e) {
			return Response.status(401).build();
		} 
	}
}
