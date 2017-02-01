package org.gauntlet.quizzes.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorManagerService;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.security.authentication.jwt.api.IJWTTokenService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@Path("quizzes")
public class QuizResource  {
	private volatile LogService logger;
	private volatile IQuizDAOService quizService;
	private volatile IProblemDAOService problemService;
	private volatile IQuizGeneratorManagerService quizGeneratorManagerService;	
	private volatile IJWTTokenService tokenService;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Quiz> get(@Context HttpServletRequest request, @PathParam("id") Long id, @QueryParam("type") long quizType, @QueryParam("start") int start, @QueryParam("end") int end ) throws ApplicationException, NoSuchModelException, JsonParseException, JsonMappingException, IOException {
		final User user = tokenService.extractUser(request);
		List<Quiz> res  = quizService.findByQuizType(user,quizType,start,end);
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
			problem.getCategory().setLessons(null);
			qproblem.setProblem(problem);
			qproblem.setQuiz(null);
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
    @Path("generate") 
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON) 
    public Quiz generate(@Context HttpServletRequest request, QuizGenerationParameters params) throws IOException, ApplicationException, NoSuchModelException { 
		final User user = tokenService.extractUser(request);
		final Quiz quiz = quizGeneratorManagerService.generate(user,params);
		quiz.getQuestions().stream()
				.forEach(e -> {
					e.getProblem().getCategory().setLessons(null);
					e.setQuiz(null);
				});
		return quiz;
    }  
}
