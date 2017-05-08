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
import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.lessons.api.model.LessonStatus;
import org.osgi.service.log.LogService;


@Path("lesson/statuses")
public class LessonStatusResource  {
	private volatile LogService logger;
	private volatile ILessonsDAOService lessonService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<LessonStatus> allCategories(@QueryParam("start") int start, @QueryParam("end") int end)
			throws ApplicationException {
		List<LessonStatus> types = lessonService.findAllLessonStatuses(start, end);
		return types;
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("{id}") 
    public LessonStatus get(@PathParam("id") Long id) throws ApplicationException, NoSuchModelException {
		return lessonService.getLessonStatusByPrimary(id);
    }
    
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void post(LessonStatus lessonType) throws ApplicationException {
		lessonService.provideLessonStatus(lessonType);
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") long id) throws NoSuchModelException, ApplicationException {
		lessonService.deleteLessonStatus(id);
	}
}
