package org.quizzical.backend.contentrepository.rest;

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
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import org.quizzical.backend.contentrepository.api.dao.IContentItemDAOService;
import org.quizzical.backend.contentrepository.api.model.ContentItem;

@Path("content")
public class ContentItemResource {
	private volatile IContentItemDAOService contentService;


	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public ContentItem provide(@Context HttpServletRequest request) throws IOException, ApplicationException {

		ContentItem contentItem = null;

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

			contentItem = convertFileItemtoContentItem((DiskFileItem) map.get("item"));

			contentItem = contentService.provide(contentItem);
		} catch (FileUploadException e) {
			throw new ApplicationException(e);
		} catch (NoSuchModelException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return contentItem;
	}

	private ContentItem convertFileItemtoContentItem(DiskFileItem fi) throws Exception {
		byte[] content = null;
		ContentItem ci = null;
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
			ci = new ContentItem(fileName, code, content, ct, cs);
		} finally {
			((FileItem) fi).delete();
		}
		return ci;
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
	@Path("{contentNameWithExtension}")
	public Response getImage(@PathParam("contentNameWithExtension") String contentNameWithExtension) throws ApplicationException {
		try {
			// Parse out the picture id.
			contentNameWithExtension = contentNameWithExtension.trim();
			
			// Fetch the picture from the database.
			final ContentItem item = contentService.getByCode(contentNameWithExtension);
			// Stream it back to the client.
			final InputStream pictureStream = new ByteArrayInputStream(item.getContent());
			return Response.ok().entity(pictureStream).type(item.getMimeType()).build();
		} catch (NoSuchModelException e) {
			return Response.status(404).build();
		}
	}
	
	@GET
	@Path("/id/{contentItemId}/{originalDocName}")
	public Response getImage(@PathParam("contentItemId") Long contentItemId, @PathParam("contentItemId") String originalDocName) throws ApplicationException {
		try {
			// Fetch the picture from the database.
			final ContentItem item = contentService.getByPrimary(contentItemId);
			// Stream it back to the client.
			final InputStream pictureStream = new ByteArrayInputStream(item.getContent());
			return Response.ok().entity(pictureStream).type(item.getMimeType()).header("Content-Disposition",  String.format("filename=%s.pdf",item.getName())).build();
		} catch (NoSuchModelException e) {
			return Response.status(404).build();
		}
	}
}
