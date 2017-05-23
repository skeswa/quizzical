package org.quizzical.backend.reporting.email;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.mail.EmailException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.gauntlet.core.commons.util.StringUtil;
import org.gauntlet.problems.api.model.ProblemCategoryLesson;
import org.osgi.service.log.LogService;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;
import org.quizzical.backend.mail.api.IMailService;
import org.quizzical.backend.mail.api.LogServiceChute;
import org.quizzical.backend.mail.api.MailPreparator;
import org.quizzical.backend.reporting.analytics.impl.UserAnalyticsReportingConfiguration;

public class PerformanceReportingMessagePreparator extends MailPreparator {

	private String emailAddress;
	private String fullName;
	private String firstName;
	private LogService logger;
	private List<String> bccEmails;
	private List<TestCategoryRating> excellentRatings;
	private List<TestCategoryRating> goodRatings;
	private List<TestCategoryRating> needImprovRatings;
	private List<TestCategoryRating> doNotMeetRatings;
	private Map<String, LessonResources> lessonMap;
	private UserAnalyticsReportingConfiguration config;
	private List<TestCategoryRating> notStartedRatings;

	public PerformanceReportingMessagePreparator(
			IMailService service, 
			LogService logger,
			String firstName,
			String emailAddress,
			List<String> bccEmails,
			String subject,
			final List<TestCategoryRating> excellentRatings,
			final List<TestCategoryRating> goodRatings,
			final List<TestCategoryRating> needImprovRatings,
			final List<TestCategoryRating> doNotMeetRatings, 
			final List<TestCategoryRating> notStartedRatings,
			Map<String, LessonResources> lessonMap, 
			UserAnalyticsReportingConfiguration config) throws EmailException {
		super(service,subject);
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.logger = logger;
		this.bccEmails = bccEmails;
		this.excellentRatings = excellentRatings;
		this.goodRatings = goodRatings;
		this.needImprovRatings = needImprovRatings;
		this.doNotMeetRatings = doNotMeetRatings;
		this.notStartedRatings = notStartedRatings;
		this.lessonMap = lessonMap;
		this.config = config;
	}
	
	@Override
	public void prepare() throws Exception  {
		super.sender.addTo(emailAddress, fullName);
		if (this.bccEmails != null) {
			for (String bcc : bccEmails) {
				super.sender.addBcc(bcc);
			}
		}
		
		final VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM,new LogServiceChute(this.logger));
		ve.init();

		
		// -- Repare body
		InputStream is = PerformanceReportingMessagePreparator.class
				.getResourceAsStream("/email_templates/daily_reports/daily-report-cid.html");
		String templateString = StringUtil.read(is);

		VelocityContext vec = new VelocityContext();
		StringWriter out = new StringWriter();
		ve.init();
		
		vec.put("firstName", firstName);
		
		vec.put("doNotMeetRatings", doNotMeetRatings);
		vec.put("excellentRatings", excellentRatings);
		vec.put("goodRatings", goodRatings);
		vec.put("needImprovRatings", needImprovRatings);
		vec.put("notStartedRatings", notStartedRatings);
		vec.put("lessonMap", lessonMap);
		vec.put("config", config);
		

		out = new StringWriter();
		ve.evaluate(vec, out, "mystring", templateString);
		String body = out.toString();

		// set the html message
		super.sender.setHtmlMsg(body);
	}
}
