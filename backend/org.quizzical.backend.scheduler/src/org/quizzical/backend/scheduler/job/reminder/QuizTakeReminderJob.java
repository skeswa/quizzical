package org.quizzical.backend.scheduler.job.reminder;

import java.util.ArrayList;
import java.util.List;

import org.amdatu.scheduling.Job;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.quizzical.backend.scheduler.api.IQuizTakerReminderService;
import org.quizzical.backend.scheduler.impl.QuizTakerReminderServiceImpl;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.sms.api.AlertNotificationException;
import org.quizzical.backend.sms.api.IAlertNotifier;
import org.quizzical.backend.sms.api.NotificationMessage;

public class QuizTakeReminderJob implements Job {
	
	private IQuizSubmissionDAOService quizSubmissionService;
	
	private IAlertNotifier notifier;
	
	private IUserDAOService userService;

	private IQuizTakerReminderService quizTakerReminderService;

	@Override
	public void execute() {
		try {
			for (User user : userService.getAllActiveUsers()) {
				System.out.println(String.format("Notifying user %s", user.getUserId()));
				List<QuizSubmission> submissions = quizSubmissionService.findQuizSubmissionsMadeToday(user);
				if (submissions.isEmpty()) {
					quizTakerReminderService.sendReminder(user);
				}
				else
					System.out.println(String.format("User %s inactive. SMS not sent.", user.getUserId()));
			}
		} catch (ApplicationException e) {
			throw new RuntimeException(e);
		}
	}

	public QuizTakeReminderJob() {
	}

	public QuizTakeReminderJob(final IAlertNotifier notifier, final IUserDAOService userService, final IQuizSubmissionDAOService quizSubmissionService, IQuizTakerReminderService quizTakerReminderService) {
		this.quizSubmissionService = quizSubmissionService;
		this.userService = userService;
		this.notifier = notifier;
		this.quizTakerReminderService = quizTakerReminderService;
	}
}
