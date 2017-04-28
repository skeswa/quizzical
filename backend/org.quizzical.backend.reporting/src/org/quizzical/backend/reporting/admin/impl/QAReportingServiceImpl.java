package org.quizzical.backend.reporting.admin.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.mail.EmailException;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;
import org.quizzical.backend.mail.api.IMailService;
import org.quizzical.backend.reporting.analytics.impl.UserAnalyticsReportingConfiguration;
import org.quizzical.backend.reporting.api.IQAReporting;
import org.quizzical.backend.reporting.email.QAReportingMessagePreparator;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;

@SuppressWarnings("restriction")
public class QAReportingServiceImpl implements IQAReporting, EventHandler {
	
	private volatile LogService logger;
	
	private volatile BundleContext ctx;
	
	private volatile IUserDAOService userService;
	
	private volatile IProblemDAOService problemService;

	private volatile IQuizProblemDAOService quizProblemService;
	
	private volatile IMailService mailService;
	
	private volatile UserAnalyticsReportingConfiguration config;
	
	private volatile AdminReportingConfiguration qaConfig;


	@Override
	public void handleEvent(Event event) {
		if (config.getHandleEmailResultsEvent()) {
			List<Long> ids = (List<Long>) event.getProperty(EVENT_TOPIC_PROP_QPIDS);
	        try {
	        	reportFaultyProblems(ids);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void reportFaultyProblems(List<Long> quizPproblemIds) throws ApplicationException, EmailException {
		final User user = userService.getByEmail(qaConfig.getEmailAddress());
		
		if (user == null)
			throw new ApplicationException(String.format("UserId (%s) not found", qaConfig.getEmailAddress()));
		

		final String subject = String.format("q7l Faulty Problems Report(%s)", new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(new Date()));
		
		
		final List<Long> problemIds = quizPproblemIds.stream()
			.map(qpid -> {
				try {
					QuizProblem qp = quizProblemService.getByPrimary(qpid);
					return qp.getProblemId();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			})
			.collect(Collectors.toList());
		
		final QAReportingMessagePreparator prep = new QAReportingMessagePreparator(
				this.mailService, 
				this.logger,
				user.getFirstName(),
				user.getEmailAddress(),
				subject,
				problemIds,
				qaConfig,
				config);
		try {
			prep.prepare();
			prep.send();
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}
}