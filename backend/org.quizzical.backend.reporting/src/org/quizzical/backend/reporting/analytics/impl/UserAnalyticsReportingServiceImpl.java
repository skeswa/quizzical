package org.quizzical.backend.reporting.analytics.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.mail.EmailException;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemCategoryLesson;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;
import org.quizzical.backend.contentrepository.api.dao.IContentItemDAOService;
import org.quizzical.backend.contentrepository.api.model.ContentItem;
import org.quizzical.backend.mail.api.IMailService;
import org.quizzical.backend.reporting.api.IUserAnalyticsReporting;
import org.quizzical.backend.reporting.email.LessonResourceItem;
import org.quizzical.backend.reporting.email.LessonResources;
import org.quizzical.backend.reporting.email.PerformanceReportingMessagePreparator;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;

@SuppressWarnings("restriction")
public class UserAnalyticsReportingServiceImpl implements IUserAnalyticsReporting, EventHandler {
	
	private volatile LogService logger;
	
	private volatile BundleContext ctx;
	
	private volatile IUserDAOService userService;
	
	private volatile ITestUserAnalyticsDAOService userAnalyticsService;
	
	private volatile IContentItemDAOService contentItemService;
	
	private volatile IProblemDAOService problemService;

	private volatile IQuizProblemDAOService quizProblemService;
	
	private volatile IMailService mailService;
	
	private volatile UserAnalyticsReportingConfiguration config;

	@Override
	public void emailDailyReport(String userId, List<String> bccEmails) throws ApplicationException, EmailException {
		final User user = userService.getByEmail(userId);
		
		if (user == null)
			throw new ApplicationException(String.format("UserId (%s) not found", userId));
		
		Map<String,LessonResources> lessonMap = new HashMap<>();
		
		
		final List<TestCategoryRating> excellentRatings = userAnalyticsService.findWeakestCategoriesLowerThanRating(user, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_EXCELLENT, 100);
		generateLessons(lessonMap, excellentRatings);
		final List<TestCategoryRating> goodRatings = userAnalyticsService.findWeakestCategoriesLowerThanRating(user, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_GOOD, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_EXCELLENT-1);
		generateLessons(lessonMap, goodRatings);
		final List<TestCategoryRating> needImprovRatings = userAnalyticsService.findWeakestCategoriesLowerThanRating(user, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_IMPROVING, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_GOOD-1);
		generateLessons(lessonMap, needImprovRatings);
		final List<TestCategoryRating> doNotMeetRatings = userAnalyticsService.findWeakestCategoriesLowerThanRating(user, 0, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_IMPROVING-1);
		generateLessons(lessonMap, doNotMeetRatings);

		
		final String subject = String.format("%s's q7l Progress Report @ %s", user.getFirstName(),new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(new Date()) );
		
		final PerformanceReportingMessagePreparator prep = new PerformanceReportingMessagePreparator(
				this.mailService, 
				this.logger,
				user.getFirstName(),
				user.getEmailAddress(),
				bccEmails,
				subject,
				excellentRatings,
				goodRatings,
				needImprovRatings,
				doNotMeetRatings,
				lessonMap,
				config);
		try {
			prep.prepare();
			prep.send();
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}
	
	private void generateLessons(Map<String,LessonResources> lessonMap, List<TestCategoryRating> ratings) throws ApplicationException {
		for (TestCategoryRating r : ratings) {
			final ProblemCategory cat = problemService.getProblemCategoryByCode(r.getName());
			Set<ProblemCategoryLesson> catLessons = cat.getLessons();
			final List<LessonResourceItem> ritems = catLessons
					.stream()
					.map(p -> {
						ContentItem ci = null;
						try {
							ci = contentItemService.getByPrimary(p.getContentItemId());
						} catch (Exception e) {
						}
						return new LessonResourceItem(config,ci.getId(),ci.getDescription());
					})
					.collect(Collectors.toList());
			final LessonResources lrs = new LessonResources(ritems);
			lessonMap.put(r.getName(),lrs);
		}
	}

	@Override
	public void handleEvent(Event event) {
		if (config.getHandleEmailResultsEvent()) {
	        String userId = (String) event.getProperty(EVENT_TOPIC_PROP_USERID);
	        try {
	        	if (config.getBcc() != null && !config.getBcc().isEmpty())
	        		emailDailyReport(userId,config.getBcc());
	        	else
	        		emailDailyReport(userId,Collections.emptyList());
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}