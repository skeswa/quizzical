package org.gauntlet.quizzes.rest;

import java.io.IOException;
import java.util.ArrayList;
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
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.security.authentication.jwt.api.IJWTTokenService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@Path("quiz/submissions")
public class QuizSubmissionResource {
	@SuppressWarnings("unused")
	private volatile LogService logger;
	private volatile IQuizSubmissionDAOService quizSubmissionDAOService;
	private volatile IQuizDAOService quizDAOService;
	private volatile IJWTTokenService tokenService;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuizSubmission> get(@Context HttpServletRequest request, @QueryParam("start") int start, @QueryParam("end") int end) throws ApplicationException {
		//final User user = getUserFromToken(request);
		return quizSubmissionDAOService.findAll(null,start, end);
    }	
	
    @GET 
    @Path("{quizId}") 
    @Produces(MediaType.APPLICATION_JSON) 
    public QuizSubmission get(@Context HttpServletRequest request, @PathParam("quizId") long quizId) throws NoSuchModelException, ApplicationException, JsonParseException, JsonMappingException, IOException {
    	final User user = tokenService.extractUser(request);
    	final QuizSubmission submission = quizSubmissionDAOService.findByQuizId(user, quizId);
    	submission.getResponses().stream()
		.forEach(e -> {
			((QuizProblemResponse)e).getQuizProblem().getProblem().getCategory().setLessons(null);
			((QuizProblemResponse)e).getQuizProblem().setQuiz(null);
		});
    	submission.getQuiz().setQuestions(null);
    	return submission;
    }
    
    @GET 
    @Path("latest") 
    @Produces(MediaType.APPLICATION_JSON) 
    public QuizSubmission latest(@Context HttpServletRequest request) throws NoSuchModelException, ApplicationException, JsonParseException, JsonMappingException, IOException {
    	final User user = tokenService.extractUser(request);
    	final QuizSubmission submission = quizSubmissionDAOService.findLatestQuizSubmission(user);
    	if (submission != null) {
	    	submission.getQuiz().setQuestions(null);
	    	return submission;
    	}
    	else
    		return null;
    }
    
    @GET 
    @Path("unsubmitted") 
    @Produces(MediaType.APPLICATION_JSON) 
    public List<Quiz> unsubmitted(@Context HttpServletRequest request) throws ApplicationException, NoSuchModelException, JsonParseException, JsonMappingException, IOException {
		final User user = tokenService.extractUser(request);
		List<Quiz> res  = quizSubmissionDAOService.findQuizzesWithNoSubmission(user);
		for (Quiz quiz : res) {
			quiz.setQuestions(new ArrayList<QuizProblem>());
			List<QuizProblem> qproblems = quiz.getQuestions();
/*			for (QuizProblem qproblem : qproblems) {
				Problem problem = problemService.getByPrimary(qproblem.getProblemId());
				qproblem.setProblem(problem);
			}*/
		}
		return res;
    }
    
    @GET 
    @Path("recent") 
    @Produces(MediaType.APPLICATION_JSON) 
    public List<QuizSubmission> recent(@Context HttpServletRequest request) throws ApplicationException, NoSuchModelException, JsonParseException, JsonMappingException, IOException {
		final User user = tokenService.extractUser(request);
		List<QuizSubmission> res  = quizSubmissionDAOService.findMostRecentUserSubmissions(user, 5);
		res.stream().forEach(qs -> {
			qs.getQuiz().setQuestions(null);
		});
		
		return res;
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON) 
    public QuizSubmission put(@Context HttpServletRequest request,final QuizSubmission quizSubmission)
    		throws IOException, ApplicationException, NoSuchModelException { 
    	final User user = tokenService.extractUser(request);
    	final QuizSubmission submission = quizSubmissionDAOService.submit(user,quizSubmission);
    	submission.getResponses().stream()
		.forEach(e -> {
			((QuizProblemResponse)e).getQuizProblem().getProblem().getCategory().setLessons(null);
			((QuizProblemResponse)e).getQuizProblem().setQuiz(null);
		});
    	submission.getQuiz().setQuestions(null);
    	return submission;
    }     

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") long quizId) throws NoSuchModelException, ApplicationException {
		quizDAOService.delete(quizId);
	}
}
