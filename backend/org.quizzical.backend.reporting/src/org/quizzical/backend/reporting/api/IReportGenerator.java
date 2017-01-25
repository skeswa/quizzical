package org.quizzical.backend.reporting.api;

import java.io.OutputStream;
import java.util.Map;

public interface IReportGenerator {
	public OutputStream generatePDF(String reportName, Map<String, Object> params) throws Exception;

	public void generatePDFToFile(String reportName, Map<String, Object> params, String outputFileName) throws Exception;

	/**
	 * Gets the url of the report generating web service relative to the
	 * provided url context path.
	 * 
	 * @param serverUrl the server url that the reporting service is running on e.g. localhost:8080
	 * @return the url of the reporting web service
	 */
	public String getUrlPathForPDFGenerator(String serverUrl);
}
