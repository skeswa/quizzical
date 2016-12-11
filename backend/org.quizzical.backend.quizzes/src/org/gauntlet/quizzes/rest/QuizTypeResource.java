package org.gauntlet.quizzes.rest;

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
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizType;
import org.osgi.service.log.LogService;


@Path("quiz/types")
public class QuizTypeResource {
	private volatile LogService logger;
	private volatile IQuizDAOService quizService;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Quiz> get(@QueryParam("type") long quizType, @QueryParam("start") int start, @QueryParam("end") int end ) throws ApplicationException {
		return quizService.findByQuizType(quizType,start,end);
    }
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("{id}") 
    public QuizType get(@PathParam("id") Long id) throws ApplicationException, NoSuchModelException {
		return quizService.getQuizTypeByPrimary(id);
    }
    
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void post(QuizType quizType) throws ApplicationException {
		quizService.provideQuizType(quizType);
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") long id) throws NoSuchModelException, ApplicationException {
		quizService.deleteQuizType(id);
	}
}
