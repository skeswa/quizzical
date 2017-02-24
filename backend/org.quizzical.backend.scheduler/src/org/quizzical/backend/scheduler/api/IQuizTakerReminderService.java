package org.quizzical.backend.scheduler.api;

import org.quizzical.backend.security.authorization.api.model.user.User;

public interface IQuizTakerReminderService {
	void sendReminder(User user);
	void sendReminders();
}
