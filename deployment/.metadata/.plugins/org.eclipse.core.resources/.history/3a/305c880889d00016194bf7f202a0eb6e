package org.gauntlet.quizzes.rest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizTakeDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.osgi.service.log.LogService;


@Path("quiz/submissions")
public class QuizSubmissionResource {
	@SuppressWarnings("unused")
	private volatile LogService logger;
	private volatile IProblemDAOService problemService;
	private volatile IQuizDAOService quizService;
	private volatile IQuizProblemDAOService quizProblemService;
	private volatile IQuizTakeDAOService quizTakeService;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuizSubmission> get(@QueryParam("start") int start, @QueryParam("end") int end) throws ApplicationException {
		return quizTakeService.findAll(start, end);
    }	
	
    @GET 
    @Path("{id}") 
    @Produces(MediaType.APPLICATION_JSON) 
    public QuizSubmission get(@PathParam("id") long id) throws NoSuchModelException, ApplicationException {
		return quizTakeService.getByPrimary(id);
    }
    
    @POST 
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON) 
    public QuizSubmission post(final QuizSubmission quizSubmission)
    		throws IOException, ApplicationException, NoSuchModelException { 
    	final Long quizId = quizSubmission.getQuizId();
    	final Quiz quiz = quizService.getByPrimary(quizId);

    	quizSubmission.setQuiz(quiz);
    	quizSubmission.setCode(String.format("%s-%d", quizId, System.currentTimeMillis()));
    	
    	// Evaluate the which quiz problem responses are correct.
    	quizSubmission.getResponses()
    		.parallelStream()
    		.map(problemResponse -> {
	    		final Long quizProblemId = problemResponse.getQuizProblemId();
				try {
					final QuizProblem quizProblem = quizProblemService.getByPrimary(quizProblemId);
					@SuppressWarnings("unused")
					final Problem problem = problemService.getByPrimary(quizProblem.getProblemId());
					
					// TODO(skeswa): evaluate if correct using the problem.
					final Boolean correct = false;
					
					final QuizProblemResponse newQuizProblemResponse = new QuizProblemResponse(
							QuizProblemResponse.code(quizProblemId, problemResponse.getResponse()),
							problemResponse.getResponse(),
							problemResponse.getSkipped(),
							correct,
							problemResponse.getSecondsElapsed(),
							quizProblemId);

					// TODO(skeswa): use a DAO service to insert the newQuizProblemResponse.
					return newQuizProblemResponse;
				} catch (final NoSuchModelException e) {
					throw new IllegalArgumentException("Failed to find problem for provided quiz problem id.", e);
				} catch (final ApplicationException e) {
					throw new RuntimeException(e);
				}
	    	})
    		.collect(Collectors.toList());
    	
    	return quizTakeService.add(quizSubmission);
    }     

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") long id) throws NoSuchModelException, ApplicationException {
		quizTakeService.delete(id);
	}
}
