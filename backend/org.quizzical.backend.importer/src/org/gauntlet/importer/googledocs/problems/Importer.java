package org.gauntlet.importer.googledocs.problems;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.importer.googledocs.IGSheetProblemsImporter;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.gauntlet.problems.api.model.ProblemPicture;
import org.gauntlet.problems.api.model.ProblemSource;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.QuizType;
import org.osgi.service.log.LogService;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.sheets.v4.model.ValueRange;


public class Importer {
	private volatile LogService logger;
	private volatile IProblemDAOService problemService;
	private volatile IQuizDAOService quizService;
	private volatile IGSheetProblemsImporter gServices;
	
	private void start() throws ApplicationException, IOException {
		final String spreadsheetID = "1d5j5BxGbexL-qfdY4atuq39I6MFKYWFGTbPESSasRXE";
		final String readRange = "Sheet 1!A2:L";
		final ValueRange response = gServices.getSheetsService().spreadsheets().values().get(spreadsheetID, readRange).execute();
	    final List<List<Object>> values = response.getValues();
	    
	    final Drive service = gServices.getDriveService();
	    int count = 1;
	    for (List<Object> row : values) {
	    	count++;
	    	String sourceName = null;
			Integer iPageNum = null;
			Integer iIndexInPageNum = null;
	    	if (row.size() > 1) {
	    		java.io.File tempFile = null;
				try {
					sourceName = (String)row.get(1);
					String pageNum = (String)row.get(3);
					String typeName = (String)row.get(4);
					String categoryName = (String)row.get(5);
					String difficulty = (String)row.get(6);
					String difficultyColor = "#f4424b";
					if ("E".equals(difficulty)) {
						difficulty = "Easy";
						difficultyColor = "#89f442";
					}
					else if ("M".equals(difficulty)) {
						difficulty = "Medium";
						difficultyColor = "#f4bf42";
					} 
					else {
						difficulty = "Hard";
						difficultyColor = "#f44242";
					} 
					String useCalculator = (String)row.get(7);
					String questionIndex = (String)row.get(8);
					String questionImgURL = (String)row.get(9);
					String answerKey = (String)row.get(10);
					String explainImgUrl = (String)row.get(11);
					
					iPageNum = pageNum != null && pageNum.trim().length() > 1 ?Integer.valueOf(pageNum):-1;
					iIndexInPageNum = questionIndex != null?Integer.valueOf(questionIndex):-1;
					
					if (questionImgURL != null && questionImgURL.trim().length() > 1
						&& explainImgUrl != null && explainImgUrl.trim().length() > 1) {
						final ProblemSource pSource = problemService.provideProblemSource(sourceName);
						
						boolean multipleChoice =  "MC".equals(typeName) ? true : false;
						Boolean requiresCalculator =  (useCalculator == null)?null:("Y".equals(useCalculator) ? true : false);
						
						final Problem problem = problemService.getBySourceAndPageNumberAndIndexAndCalcType(pSource.getId(),iPageNum,iIndexInPageNum,requiresCalculator);
						
						if (problem == null) {
							//-- Lookup category
							final ProblemCategory cat = problemService.provideProblemCategory(categoryName);
							
							//-- Lookup difficulty
							ProblemDifficulty prob = new ProblemDifficulty(difficulty, difficulty, difficultyColor);
							final ProblemDifficulty dif = problemService.provideProblemDifficulty(prob);
							
							
							//-- Question img
							Get qFileRequest = service.files().get(questionImgURL);
							com.google.api.services.drive.model.File fileMd = qFileRequest.execute();
							//tempFile = java.io.File.createTempFile("img",".png");
							ByteArrayOutputStream qOut = new ByteArrayOutputStream();
							qFileRequest.getMediaHttpDownloader().setDirectDownloadEnabled(true);
							qFileRequest.executeMediaAndDownloadTo(qOut);
							final ProblemPicture questionPicture = convertByteArraytoProblemPicture(qOut,fileMd.getName(),fileMd.getMimeType(),-1);
							
							//-- Answer img
							Get aFileRequest = service.files().get(explainImgUrl);
							fileMd = aFileRequest.execute();
							//tempFile = java.io.File.createTempFile("img",".png");
							ByteArrayOutputStream aOut = new ByteArrayOutputStream();
							aFileRequest.getMediaHttpDownloader().setDirectDownloadEnabled(true);
							aFileRequest.executeMediaAndDownloadTo(aOut);
							final ProblemPicture answerPicture = convertByteArraytoProblemPicture(aOut,fileMd.getName(),fileMd.getMimeType(),-1);
							
							Problem newProblem = new Problem(answerKey, 
									pSource, // source,
									cat, // category,
									iPageNum, iIndexInPageNum, 
									dif, // ProblemDifficulty
									answerPicture, // byte[] questionPicture,
									questionPicture, // byte[] questionPicture,
									multipleChoice, requiresCalculator);
							problemService.provide(newProblem);
							System.out.println(String.format("Processed %s", newProblem.toString()));
						}
					}
					else { //Skipped
						System.out.println(String.format("Skipped problem %s_%d_%d on row %d (questionImgURL,explainImgUrl)=(%s,%s)",sourceName,iPageNum,iIndexInPageNum,count,questionImgURL,explainImgUrl));
					}
				} catch (Exception e) {
					System.out.println(String.format("Error Skipped problem %s_%d_%d on row %d",sourceName,iPageNum,iIndexInPageNum,count));
				}
				finally {
					if (tempFile != null)
						tempFile.delete();
				}
	    	}
	    }
	    System.exit(0);
	}
	
	private ProblemPicture convertByteArraytoProblemPicture(ByteArrayOutputStream out, String fileName, String ct, long cs) throws Exception {
		byte[] content = null;
		ProblemPicture pp = null;
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(out.toByteArray());
			content = IOUtils.toByteArray(is);
		} finally {
			if (is != null)
				is.close();
			if (out != null)
				out.close();
		}
		final String code = Long.toString(System.currentTimeMillis()) + fileName;
		pp = new ProblemPicture(code, code, content, ct, cs);
		return pp;
	}
}
