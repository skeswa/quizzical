package org.quizzical.backend.reporting.jasper.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.quizzical.backend.reporting.api.IReportGenerator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;


public class PdfReportingServiceImpl implements IReportGenerator {
	
	private static DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}		

	@Override
	public OutputStream generatePDF(String reportName,
			Map<String, Object> params) throws Exception{
		JasperPrint jasperPrint = null;
		OutputStream ouputStream = null;
		try 
		{
			InputStream reportTemplateInputStream = PdfReportingServiceImpl.class.getResourceAsStream("/jasperreports/"+reportName+".jasper");
			if (reportTemplateInputStream == null)
				throw new JRRuntimeException(
						"File WebappReport.jasper not found. The report design must be compiled first.");

			try {
				Connection connection = dataSource.getConnection();
				jasperPrint = JasperFillManager.fillReport(reportTemplateInputStream,params, connection);

				connection.close();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List jasperPrintList = new ArrayList();
		jasperPrintList.add(jasperPrint);


		//	response.setContentType("application/pdf");
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,
				jasperPrintList);

		ouputStream = new ByteArrayOutputStream();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
				ouputStream);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new Exception(e);
		} finally {
			if (ouputStream != null) {
				try {
					ouputStream.close();
				} catch (IOException ex) {
				}
			}
		}
		
		
		return ouputStream;
	}

	@Override
	public void generatePDFToFile(String reportName,
			Map<String, Object> params, String outputFileName) throws Exception {
		JasperPrint jasperPrint = null;
		try 
		{
			InputStream reportTemplateInputStream = PdfReportingServiceImpl.class.getResourceAsStream("/jasperreports/"+reportName+".jasper");
			if (reportTemplateInputStream == null)
				throw new JRRuntimeException(
						"File WebappReport.jasper not found. The report design must be compiled first.");

			try {
				Connection connection = dataSource.getConnection();
				jasperPrint = JasperFillManager.fillReport(reportTemplateInputStream,params, connection);

				connection.close();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List jasperPrintList = new ArrayList();
		jasperPrintList.add(jasperPrint);


		//	response.setContentType("application/pdf");
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,
				jasperPrintList);

		FileOutputStream ouputStream = new FileOutputStream(outputFileName);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
				ouputStream);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new Exception(e);
		} finally {
			if (ouputStream != null) {
				try {
					ouputStream.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	@Override
	public String getUrlPathForPDFGenerator(String serverUrl) {
		return serverUrl.endsWith("/") ? serverUrl + "reporting/pdfgen" : serverUrl + "/reporting/pdfgen";
	}
}
