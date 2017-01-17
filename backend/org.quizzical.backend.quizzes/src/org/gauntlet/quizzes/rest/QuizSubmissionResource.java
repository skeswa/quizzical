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

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.model.user.User;
import org.quizzical.backend.security.jwt.api.IJWTTokenService;


@Path("quiz/submissions")
public class QuizSubmissionResource {
	@SuppressWarnings("unused")
	private volatile LogService logger;
	private volatile IQuizSubmissionDAOService quizSubmissionDAOService;
	private volatile IJWTTokenService tokenProvider;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuizSubmission> get(@Context HttpServletRequest request, @QueryParam("start") int start, @QueryParam("end") int end) throws ApplicationException {
		//final User user = getUserFromToken(request);
		return quizSubmissionDAOService.findAll(null,start, end);
    }	
	
    @GET 
    @Path("{id}") 
    @Produces(MediaType.APPLICATION_JSON) 
    public QuizSubmission get(@PathParam("id") long id) throws NoSuchModelException, ApplicationException {
		return quizSubmissionDAOService.getByPrimary(id);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON) 
    public QuizSubmission put(final QuizSubmission quizSubmission)
    		throws IOException, ApplicationException, NoSuchModelException { 
    	return quizSubmissionDAOService.submit(quizSubmission);
    }     

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") long id) throws NoSuchModelException, ApplicationException {
		quizSubmissionDAOService.delete(id);
	}
}
