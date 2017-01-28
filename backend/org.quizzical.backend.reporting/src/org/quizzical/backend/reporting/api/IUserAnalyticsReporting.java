package org.quizzical.backend.reporting.api;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.gauntlet.core.api.ApplicationException;

public interface IUserAnalyticsReporting {
	public static final Integer QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_EXCELLENT = 90;
	public static final Integer QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_GOOD = 80;
	public static final Integer QUIZ_RATING_PERFORMANCE_RATING_THRESHOLD_IMPROVING = 50;
	
	public void emailDailyReport(String userId, List<String> bccEmails) throws ApplicationException, EmailException;
}
