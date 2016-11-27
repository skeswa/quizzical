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
import org.gauntlet.problems.api.model.ProblemDifficulty;

@Path("difficulties")
public class DifficultiesResource {
	private volatile IProblemDAOService problemService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProblemDifficulty> allDifficulties(@QueryParam("start") int start, @QueryParam("end") int end)
			throws ApplicationException {
		return problemService.findAllProblemDifficulties(start, end);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("count")
	public long countDifficulties() throws ApplicationException {
		return problemService.countAllProblemDifficulties();
	}

	@GET
	@Path("{problemDifficultyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProblemDifficulty getProblemDifficulty(@PathParam("problemDifficultyId") long problemDifficultyId)
			throws NoSuchModelException, ApplicationException {
		return problemService.getProblemDifficultyByPrimary(problemDifficultyId);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void provideProblemDifficulty(ProblemDifficulty problemDifficulty) throws ApplicationException {
		problemService.provideProblemDifficulty(problemDifficulty);
	}

	@DELETE
	@Path("{problemDifficultyId}")
	public void deleteProbleDifficulty(@PathParam("problemDifficultyId") long problemDifficultyId)
			throws NoSuchModelException, ApplicationException {
		problemService.deleteProblemDifficulty(problemDifficultyId);
	}
}
