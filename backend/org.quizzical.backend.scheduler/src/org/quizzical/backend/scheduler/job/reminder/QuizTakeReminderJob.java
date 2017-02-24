package org.quizzical.backend.scheduler.job.reminder;

import org.amdatu.scheduling.Job;
import org.quizzical.backend.scheduler.api.IQuizTakerReminderService;

public class QuizTakeReminderJob implements Job {

	private volatile IQuizTakerReminderService quizTakerReminderService;

	@Override
	public void execute() {
		quizTakerReminderService.sendReminders();
	}

	public QuizTakeReminderJob() {
	}
}
