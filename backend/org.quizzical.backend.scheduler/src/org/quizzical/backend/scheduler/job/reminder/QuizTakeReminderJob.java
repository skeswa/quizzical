package org.quizzical.backend.scheduler.job.reminder;

import java.util.ArrayList;
import java.util.List;

import org.amdatu.scheduling.Job;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.sms.api.AlertNotificationException;
import org.quizzical.backend.sms.api.IAlertNotifier;
import org.quizzical.backend.sms.api.NotificationMessage;

public class QuizTakeReminderJob implements Job {
	
	private IQuizSubmissionDAOService quizSubmissionService;
	
	private IAlertNotifier notifier;
	
	private IUserDAOService userService;

	@Override
	public void execute() {
		try {
			for (User user : userService.getAllActiveUsers()) {
				System.out.println(String.format("Notifying user %s", user.getUserId()));
				List<QuizSubmission> submissions = quizSubmissionService.findQuizSubmissionsMadeToday(user);
				if (submissions.isEmpty()) {
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
			}
		} catch (ApplicationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public QuizTakeReminderJob() {
	}

	public QuizTakeReminderJob(final IAlertNotifier notifier, final IUserDAOService userService, final IQuizSubmissionDAOService quizSubmissionService) {
		this.quizSubmissionService = quizSubmissionService;
		this.userService = userService;
		this.notifier = notifier;
	}
}
