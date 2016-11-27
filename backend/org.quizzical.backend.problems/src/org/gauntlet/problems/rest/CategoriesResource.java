package org.gauntlet.problems.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.ProblemCategory;

@Path("categories")
public class CategoriesResource {
	private volatile IProblemDAOService problemService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProblemCategory> allCategories(@QueryParam("start") int start, @QueryParam("end") int end)
			throws ApplicationException {
		return problemService.findAllProblemCategories(start, end);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("count")
	public long countCategories() throws ApplicationException {
		return problemService.countAllProblemCategories();
	}

	@GET
	@Path("{problemCategoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProblemCategory getProblemCategory(@PathParam("problemCategoryId") long problemCategoryId)
			throws NoSuchModelException, ApplicationException {
		return problemService.getProblemCategoryByPrimary(problemCategoryId);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void provideProblemCategory(ProblemCategory problemCategory) throws ApplicationException {
		problemService.provideProblemCategory(problemCategory);
	}

	@DELETE
	@Path("{problemCategoryId}")
	public void deleteProbleCategory(@PathParam("problemCategoryId") long problemCategoryId)
			throws NoSuchModelException, ApplicationException {
		problemService.deleteProblemCategory(problemCategoryId);
	}
}
