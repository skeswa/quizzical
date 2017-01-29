package org.quizzical.backend.reporting.analytics.impl;

import java.text.SimpleDateFormat;
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
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.model.user.User;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;
import org.quizzical.backend.contentrepository.api.dao.IContentItemDAOService;
import org.quizzical.backend.contentrepository.api.model.ContentItem;
import org.quizzical.backend.mail.api.IMailService;
import org.quizzical.backend.reporting.api.IUserAnalyticsReporting;
import org.quizzical.backend.reporting.email.LessonResourceItem;
import org.quizzical.backend.reporting.email.LessonResources;
import org.quizzical.backend.reporting.email.PerformanceReportingMessagePreparator;

@SuppressWarnings("restriction")
public class UserAnalyticsReportingServiceImpl implements IUserAnalyticsReporting {
	
	private volatile LogService logger;
	
	private volatile BundleContext ctx;
	
	private volatile IUserDAOService userService;
	
	private volatile ITestUserAnalyticsDAOService userAnalyticsService;
	
	private volatile IContentItemDAOService contentItemService;
	
	private volatile IProblemDAOService problemService;
	
	private volatile IMailService mailService;
	
	private volatile UserAnalyticsReportingConfiguration config;

	@Override
	public void emailDailyReport(String userId, List<String> bccEmails) throws ApplicationException, EmailException {
		final User user = userService.getByEmail(userId);
		
		Map<String,LessonResources> lessonMap = new HashMap<>();
		
		
		final List<TestCategoryRating> excellentRatings = userAnalyticsService.findWeakestCategoriesLowerThanRating(user, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_EXCELLENT, 100);
		generateLessons(lessonMap, excellentRatings);
		final List<TestCategoryRating> goodRatings = userAnalyticsService.findWeakestCategoriesLowerThanRating(user, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_GOOD, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_EXCELLENT-1);
		generateLessons(lessonMap, goodRatings);
		final List<TestCategoryRating> needImprovRatings = userAnalyticsService.findWeakestCategoriesLowerThanRating(user, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_IMPROVING, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_GOOD-1);
		generateLessons(lessonMap, needImprovRatings);
		final List<TestCategoryRating> doNotMeetRatings = userAnalyticsService.findWeakestCategoriesLowerThanRating(user, 0, QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_IMPROVING-1);
		generateLessons(lessonMap, doNotMeetRatings);

		
		final String subject = String.format("q7l Progress Report for %s",new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(new Date()) );
		
		final PerformanceReportingMessagePreparator prep = new PerformanceReportingMessagePreparator(
				this.mailService, 
				this.logger,
				user.getEmailAddress(),
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
}