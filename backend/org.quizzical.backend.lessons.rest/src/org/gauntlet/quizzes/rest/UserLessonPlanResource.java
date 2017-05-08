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


@Path("user/lessonplans")
public class UserLessonPlanResource  {
    private ObjectMapper mapper = new ObjectMapper();
	
	private volatile LogService logger;
	private volatile ILessonsDAOService lessonService;
	private volatile IContentItemDAOService contentService;
	private volatile IJWTTokenService tokenService;

	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
    public UserLessonPlan get(@Context HttpServletRequest request, @PathParam("id") Long id) throws ApplicationException, NoSuchModelException, JsonParseException, JsonMappingException, IOException {
		final User user = tokenService.extractUser(request);
		UserLessonPlan lessonPlan = lessonService.getUserLessonPlanByUserPk(user.getId());
		lessonPlan.getCurrentLesson().getLesson().setQuestions(null);
		lessonPlan.setUpcomingLessons(null);
		return lessonPlan;
    }
}
