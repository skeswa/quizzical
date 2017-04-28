package org.quizzical.backend.reporting.email;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.gauntlet.core.commons.util.StringUtil;
import org.osgi.service.log.LogService;
import org.quizzical.backend.mail.api.IMailService;
import org.quizzical.backend.mail.api.LogServiceChute;
import org.quizzical.backend.mail.api.MailPreparator;
import org.quizzical.backend.reporting.admin.impl.AdminReportingConfiguration;
import org.quizzical.backend.reporting.analytics.impl.UserAnalyticsReportingConfiguration;

public class QAReportingMessagePreparator extends MailPreparator {

	private String emailAddress;
	private String firstName;
	private LogService logger;
	private UserAnalyticsReportingConfiguration config;
	private AdminReportingConfiguration qaConfig;
	private List<Long> problemIds;

	public QAReportingMessagePreparator(
			IMailService service, 
			LogService logger,
			String firstName,
			String emailAddress,
			String subject,
			final List<Long> problemIds,
			AdminReportingConfiguration qaConfig,
			UserAnalyticsReportingConfiguration config) throws EmailException {
		super(service,subject);
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.logger = logger;
		this.config = config;
		this.qaConfig = qaConfig;
		this.problemIds = problemIds;
	}
	
	@Override
	public void prepare() throws Exception  {
		super.sender.addTo(emailAddress, firstName);

		
		final VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM,new LogServiceChute(this.logger));
		ve.init();

		
		// -- Repare body
		InputStream is = QAReportingMessagePreparator.class
				.getResourceAsStream("/email_templates/qa_reports/qa-report-cid.html");
		String templateString = StringUtil.read(is);

		VelocityContext vec = new VelocityContext();
		StringWriter out = new StringWriter();
		ve.init();
		
		vec.put("firstName", firstName);
		vec.put("problemids", problemIds);
		vec.put("qaconfig", qaConfig);
		vec.put("config", config);
		

		out = new StringWriter();
		ve.evaluate(vec, out, "mystring", templateString);
		String body = out.toString();

		// set the html message
		super.sender.setHtmlMsg(body);
	}
}
