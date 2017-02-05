package org.gauntlet.problems.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.gauntlet.problems.api.model.ProblemPicture;
import org.gauntlet.problems.api.model.ProblemSource;
import org.quizzical.backend.security.authentication.jwt.api.IJWTTokenService;
import org.quizzical.backend.security.authorization.api.model.user.User;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Path("problems")
public class ProblemsResource {
	private volatile IProblemDAOService problemService;
	private volatile IJWTTokenService tokenService;

	/**
	 * 
	 * ========= Problems
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws IllegalAccessException 
	 */
	@GET
	@Path("difficulty")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Problem> listProblems(@Context HttpServletRequest request, @PathParam("difficulty") long difficulty, @QueryParam("start") int start,
			@QueryParam("end") int end) throws ApplicationException, JsonParseException, JsonMappingException, IOException, IllegalAccessException {
		final User user = tokenService.extractUser(request);
		if (!(user.getAdmin() || user.getQa()))
			throw new IllegalAccessException("Admin Role required");
		return problemService.findByDifficulty(difficulty, start, end);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Problem> all(@Context HttpServletRequest request, @QueryParam("start") int start, @QueryParam("end") int end) throws ApplicationException, JsonParseException, JsonMappingException, IOException, IllegalAccessException {
		final User user = tokenService.extractUser(request);
		if (!(user.getAdmin() || user.getQa()))
			throw new IllegalAccessException("Admin Role required");
		List<Problem> problems = problemService.findAll(start, end);
		problems.stream()
				.forEach(e -> {
					((Problem)e).getCategory().setLessons(null);
				});
		return problems;
	}

	@GET
	@Path("{problemId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Problem getProblem(@Context HttpServletRequest request, @PathParam("problemId") long problemId)
			throws NoSuchModelException, ApplicationException, JsonParseException, JsonMappingException, IOException, IllegalAccessException {
		final User user = tokenService.extractUser(request);
		if (!(user.getAdmin() || user.getQa()))
			throw new IllegalAccessException("Admin Role required");
		return problemService.getByPrimary(problemId);
	}

	@POST
	@Path("{problemId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateProblem(@Context HttpServletRequest request, Problem problem) throws ApplicationException, JsonParseException, JsonMappingException, IOException, IllegalAccessException {
		final User user = tokenService.extractUser(request);
		if (!user.getAdmin())
			throw new IllegalAccessException("Admin Role required");
		problemService.update(problem);
	}

	@DELETE
	@Path("{problemId}")
	public void delete(@Context HttpServletRequest request, @PathParam("problemId") long problemId) throws NoSuchModelException, ApplicationException, JsonParseException, JsonMappingException, IOException, IllegalAccessException {
		final User user = tokenService.extractUser(request);
		if (!user.getAdmin())
			throw new IllegalAccessException("Admin Role required");
		problemService.delete(problemId);
	}

	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Problem provide(@Context HttpServletRequest request) throws IOException, ApplicationException, IllegalAccessException {
		final User user = tokenService.extractUser(request);
		if (!user.getAdmin())
			throw new IllegalAccessException("Admin Role required");
		Problem newProblem = null;

		String answer = null;
		Integer sourcePageNumber = null;
		Integer sourceIndexWithinPage = null;
		Boolean multipleChoice = null;
		Boolean requiresCalculator = null;
		Long sourceId = null;
		Long categoryId = null;
		Long difficultyId = null;
		ProblemPicture answerPicture = null;
		ProblemPicture questionPicture = null;

		Map<String, FileItem> map = new HashMap<>();
		ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> parseRequest = (List<FileItem>) uploader.parseRequest(request);
			for (FileItem fileItem : parseRequest) {
				if (fileItem.isFormField() || fileItem instanceof DiskFileItem) {
					String fieldName = fileItem.getFieldName();
					map.put(fieldName, fileItem);
				}

			}
			answer = map.get("answer").getString();
			sourcePageNumber = Integer.valueOf(map.get("sourcePageNumber").getString());
			sourceIndexWithinPage = Integer.valueOf(map.get("sourceIndexWithinPage").getString());
			multipleChoice = Boolean.valueOf(map.get("multipleChoice").getString());
			requiresCalculator = Boolean.valueOf(map.get("requiresCalculator").getString());
			sourceId = Long.valueOf(map.get("sourceId").getString());
			categoryId = Long.valueOf(map.get("categoryId").getString());
			difficultyId = Long.valueOf(map.get("difficultyId").getString());

			answerPicture = convertFileItemtoProblemPicture((DiskFileItem) map.get("answerPicture"));
			questionPicture = convertFileItemtoProblemPicture((DiskFileItem) map.get("questionPicture"));

			ProblemSource ps = problemService.getProblemSourceByPrimary(sourceId);
			ProblemCategory pc = problemService.getProblemCategoryByPrimary(categoryId);
			ProblemDifficulty pd = problemService.getProblemDifficultyByPrimary(difficultyId);

			newProblem = new Problem(answer, ps, // source,
					pc, // category,
					sourcePageNumber, sourceIndexWithinPage, pd, // ProblemDifficulty
																	// difficulty,
					answerPicture, // byte[] answerPicture,
					questionPicture, // byte[] questionPicture,
					multipleChoice, requiresCalculator);

			newProblem = problemService.provide(newProblem);
		} catch (FileUploadException e) {
			throw new ApplicationException(e);
		} catch (NoSuchModelException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return newProblem;
	}

	private ProblemPicture convertFileItemtoProblemPicture(DiskFileItem fi) throws Exception {
		byte[] content = null;
		ProblemPicture pp = null;
		try {
			if (fi.isInMemory()) {
				InputStream is = null;
				try {
					is = fi.getInputStream();
					content = IOUtils.toByteArray(is);
				} finally {
					if (is != null)
						is.close();
				}
			} else {
				File file = File.createTempFile(fi.getFieldName(), "tmp");
				fi.write(file);
				file.deleteOnExit();

				content = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
			}
			String ct = fi.getContentType();
			long cs = fi.getSize();
			String fileName = ((FileItem) fi).getName();
			final String code = Long.toString(System.currentTimeMillis()) + fileName;
			pp = new ProblemPicture(code, code, content, ct, cs);
		} finally {
			((FileItem) fi).delete();
		}
		return pp;
	}

	@SuppressWarnings("resource")
	public byte[] readImageOldWay(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		// Get the size of the file
		long length = file.length();
		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}
		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	@GET
	@Path("pictures/{pictureIdWithExtension}")
	public Response getImage(@PathParam("pictureIdWithExtension") String pictureIdWithExtension) throws ApplicationException {
		try {
			// Parse out the picture id.
			pictureIdWithExtension = pictureIdWithExtension.trim();
			final int extensionIndex = pictureIdWithExtension.indexOf('.');
			Long pictureId = null;
			if (extensionIndex >= 0) {
				final String pictureIdText = pictureIdWithExtension.substring(0, extensionIndex);
				pictureId = Long.valueOf(pictureIdText);
			}
			else {// assume .PNG
				pictureId = Long.valueOf(pictureIdWithExtension);
			}
			
			// Fetch the picture from the database.
			final ProblemPicture picture = problemService.getProblemPictureByPrimary(pictureId);
			
			// Stream it back to the client.
			final InputStream pictureStream = new ByteArrayInputStream(picture.getPicture());
			final MediaType mediaType = new MediaType("image", "png");
			return Response.ok().type(mediaType).entity(pictureStream).build();
		} catch (NumberFormatException | ApplicationException | NoSuchModelException e) {
			throw new ApplicationException(e);
		}
	}
}
