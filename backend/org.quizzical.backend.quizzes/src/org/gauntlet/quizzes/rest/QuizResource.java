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
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizType;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorManagerService;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;
import org.osgi.service.log.LogService;


@Path("quizzes")
public class QuizResource {
	private volatile LogService logger;
	private volatile IQuizDAOService quizService;
	private volatile IProblemDAOService problemService;
	private volatile IQuizGeneratorManagerService quizGeneratorManagerService;	
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Quiz> get(@PathParam("id") Long id, @QueryParam("type") long quizType, @QueryParam("start") int start, @QueryParam("end") int end ) throws ApplicationException, NoSuchModelException {
		List<Quiz> res = quizService.findByQuizType(quizType,start,end);
		for (Quiz quiz : res) {
			List<QuizProblem> qproblems = quiz.getQuestions();
			for (QuizProblem qproblem : qproblems) {
				Problem problem = problemService.getByPrimary(qproblem.getProblemId());
				qproblem.setProblem(problem);
			}
		}
		return res;
    }
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("{id}") 
    public Quiz get(@PathParam("id") Long id) throws ApplicationException, NoSuchModelException {
		Quiz quiz = quizService.getByPrimary(id);
		List<QuizProblem> qproblems = quiz.getQuestions();
		for (QuizProblem qproblem : qproblems) {
			Problem problem = problemService.getByPrimary(qproblem.getProblemId());
			qproblem.setProblem(problem);
		}
		return quiz;
    }
    
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void post(Quiz quiz) throws ApplicationException {
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
    
    @POST
    @Path("generate") 
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON) 
    public Quiz generate(QuizGenerationParameters params) throws IOException, ApplicationException, NoSuchModelException { 
    	return quizGeneratorManagerService.generate(params);
    }  
}
