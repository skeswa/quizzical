package org.gauntlet.quizzes.rest;

import java.io.IOException;
import java.util.ArrayList;
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
import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.lessons.api.model.Constants;
import org.gauntlet.lessons.api.model.LessonType;
import org.gauntlet.lessons.api.model.UserLesson;
import org.gauntlet.lessons.api.model.UserLessonPlan;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.contentrepository.api.dao.IContentItemDAOService;
import org.quizzical.backend.contentrepository.api.model.ContentItem;
import org.quizzical.backend.security.authentication.jwt.api.IJWTTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("user/lesson")
public class UserLessonResource  {
    private ObjectMapper mapper = new ObjectMapper();
	
	private volatile LogService logger;
	private volatile ILessonsDAOService lessonService;
	private volatile IContentItemDAOService contentService;
	private volatile IQuizDAOService quizService;
	private volatile IJWTTokenService tokenService;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("upcoming") 
    public List<UserLesson> getUpcoming(@Context HttpServletRequest request, @QueryParam("start") int start, @QueryParam("end") int end ) throws ApplicationException, NoSuchModelException, JsonParseException, JsonMappingException, IOException {
		final User user = tokenService.extractUser(request);
		final LessonType lt = lessonService.getLessonTypeByCode(Constants.LESSON_TYPE_SCHEDULED);
		List<UserLesson> res  = lessonService.findAllUserLessonsByType(user,lt.getId());
		res.stream()
			.forEach(ul -> {
				Quiz quiz;
				try {
					quiz = quizService.getByPrimary(ul.getQuizId());
					quiz.setQuestions(null);
					ul.setQuiz(quiz);
					ul.getLesson().setQuestions(null);
					
					ContentItem ci = contentService.getByPrimary(ul.getLesson().getContentItemId());
					ul.getLesson().setContentItemId(ci.getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		return res;
    }
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("current") 
    public UserLesson getCurrent(@Context HttpServletRequest request) throws ApplicationException, NoSuchModelException, JsonParseException, JsonMappingException, IOException {
		final User user = tokenService.extractUser(request);
		final LessonType lt = lessonService.getLessonTypeByCode(Constants.LESSON_TYPE_CURRENT);
		UserLesson lesson = lessonService.findUserLessonByType(user,lt.getId());
		
		Quiz quiz = quizService.getByPrimary(lesson.getQuizId());
		quiz.setQuestions(null);
		lesson.setQuiz(quiz);
		lesson.getLesson().setQuestions(null);
		return lesson;
    }
}
