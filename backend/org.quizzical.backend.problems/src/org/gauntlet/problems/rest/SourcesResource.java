package org.gauntlet.problems.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import javax.ws.rs.core.Response;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.ProblemPicture;
import org.gauntlet.problems.api.model.ProblemSource;

@Path("sources")
public class SourcesResource {
	private volatile IProblemDAOService problemService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProblemSource> allSources(@QueryParam("start") int start, @QueryParam("end") int end)
			throws ApplicationException {
		return problemService.findAllProblemSources(start, end);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("count")
	public long countSources() throws ApplicationException {
		return problemService.countAllProblemSources();
	}

	@GET
	@Path("{problemSourceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProblemSource getProblemSource(@PathParam("problemSourceId") long problemSourceId)
			throws NoSuchModelException, ApplicationException {
		return problemService.getProblemSourceByPrimary(problemSourceId);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public ProblemSource provideProblemSource(ProblemSource problemSource) throws ApplicationException {
		final ProblemSource newProblemSource = problemService.provideProblemSource(problemSource);
		return newProblemSource;
	}

	@DELETE
	@Path("{problemSourceId}")
	public void deleteProblemSource(@PathParam("problemSourceId") long problemSourceId)
			throws NoSuchModelException, ApplicationException {
		problemService.deleteProblemSource(problemSourceId);
	}
}
