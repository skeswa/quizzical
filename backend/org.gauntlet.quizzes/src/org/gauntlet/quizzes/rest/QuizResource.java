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
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizType;
import org.osgi.service.log.LogService;


@Path("quizzes")
public class QuizResource {
	private volatile LogService logger;
	private volatile IQuizDAOService quizService;
	
	/**
	 * 
	 * ========= Quizzes
	 */
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("all/quiztype/{quizType}/{start}/{end}")
    public List<Quiz> listProducts(@PathParam("quizType") long quizType, @QueryParam("start") int start, @QueryParam("end") int end ) throws ApplicationException {
		return quizService.findByQuizType(quizType,start,end);
    }
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Quiz> all(
    		@QueryParam("start") final Integer start,
    		@QueryParam("end") final Integer end,
    		@QueryParam("quizTypeId") final Long quizTypeId) throws ApplicationException {
		return quizService.findAll(start, end);
    }
	
    @GET 
    @Path("{quizId}") 
    @Produces(MediaType.APPLICATION_JSON) 
    public Quiz getQuiz(@PathParam("quizId") long quizId) throws NoSuchModelException, ApplicationException {
		return quizService.getByPrimary(quizId);
    }
    
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateQuiz(Quiz quiz) throws ApplicationException {
		quizService.update(quiz);
	}

	@DELETE
	@Path("{quizId}")
	public void delete(@PathParam("quizId") long quizId) throws NoSuchModelException, ApplicationException {
		quizService.delete(quizId);
	}
	
    
    @POST 
    @Path("provide") 
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON) 
    public Quiz provide(Quiz quiz) throws IOException, ApplicationException, NoSuchModelException { 
    	Long typeId = quiz.getQuizType().getId();
    	QuizType qt = quizService.getQuizTypeByPrimary(typeId);
    	quiz.setQuizType(qt);
    	return quizService.provide(quiz);
    }     

	/**
	 * 
	 * ========= Quiz cats
	 */
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("quiztypes/all/{start}/{end}")
    public List<QuizType> allCategories(@QueryParam("start") int start, @QueryParam("end") int end) throws ApplicationException {
		return quizService.findAllQuizTypes(start, end);
    }	
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("quiztypes/count")
    public long countCategories() throws ApplicationException {
		return quizService.countAllQuizTypes();
    }	
	
    @GET 
    @Path("quiztypes/{quizTypeId}") 
    @Produces(MediaType.APPLICATION_JSON) 
    public QuizType getQuizType(@PathParam("quizTypeId") long quizTypeId) throws NoSuchModelException, ApplicationException {
		return quizService.getQuizTypeByPrimary(quizTypeId);
    }
    
	@POST
	@Path("quiztypes/provide")
	@Consumes(MediaType.APPLICATION_JSON)
	public void provideQuizType(QuizType quizType) throws ApplicationException {
		quizService.provideQuizType(quizType);
	}

	@DELETE
	@Path("quiztypes/{quizTypeId}")
	public void deleteProbleCategory(@PathParam("quizTypeId") long quizTypeId) throws NoSuchModelException, ApplicationException {
		quizService.deleteQuizType(quizTypeId);
	}	
}
