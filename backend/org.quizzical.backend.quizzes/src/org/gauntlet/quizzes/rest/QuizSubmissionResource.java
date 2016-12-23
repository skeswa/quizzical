package org.gauntlet.quizzes.rest;

import java.io.IOException;
import java.util.List;
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
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizTakeDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblemAnswer;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.gauntlet.quizzes.api.model.QuizType;
import org.osgi.service.log.LogService;


@Path("quiz/submissions")
public class QuizSubmissionResource {
	private volatile LogService logger;
	private volatile IQuizTakeDAOService quizTakeService;
	private volatile IQuizDAOService quizService;
	
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
    public QuizSubmission post(QuizSubmission quizTake) throws IOException, ApplicationException, NoSuchModelException { 
    	Long typeId = quizTake.getQuiz().getId();
    	Quiz q = quizService.getByPrimary(typeId);
    	quizTake.setQuiz(q);
    	return quizTakeService.add(quizTake);
    }     

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") long id) throws NoSuchModelException, ApplicationException {
		quizTakeService.delete(id);
	}
}
