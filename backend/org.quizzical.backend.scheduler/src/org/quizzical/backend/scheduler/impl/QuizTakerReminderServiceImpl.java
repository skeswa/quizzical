package org.quizzical.backend.scheduler.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.osgi.service.log.LogService;
import org.quizzical.backend.scheduler.api.IQuizTakerReminderService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.sms.api.AlertNotificationException;
import org.quizzical.backend.sms.api.IAlertNotifier;
import org.quizzical.backend.sms.api.NotificationMessage;

public class QuizTakerReminderServiceImpl implements IQuizTakerReminderService{
	
	private volatile DependencyManager dm;
	
	private volatile LogService logger;
	
	private volatile IQuizSubmissionDAOService quizSubmissionService;
	
	private volatile IAlertNotifier notifier;
	
	private volatile IUserDAOService userService;
	
	@Override
	public void sendReminder(User user) {
		List<String> to = new ArrayList<>();
		to.add(user.getMobileNumber());
		String body = String.format("It's Q7L Time %s!, at http://www.q7l.io", user.getFirstName());
		NotificationMessage message = new NotificationMessage(to, body, "q7l Alert");
		try {
			notifier.notifyViaSMS(message);
		} catch (AlertNotificationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendReminders() {
		try {
			System.out.println(String.format("Checking for users to notify via Text..."));
			for (User user : userService.getAllActiveUsers()) {
				System.out.println(String.format("Notifying user %s", user.getEmailAddress()));
				List<QuizSubmission> submissions = quizSubmissionService.findQuizSubmissionsMadeToday(user);
				if (submissions.isEmpty()) {
					sendReminder(user);
				}
				else
					System.out.println(String.format("User %s has submitted a quiz today. SMS not sent.", user.getEmailAddress()));
			}
		} catch (ApplicationException e) {
			throw new RuntimeException(e);
		}
	}

}
