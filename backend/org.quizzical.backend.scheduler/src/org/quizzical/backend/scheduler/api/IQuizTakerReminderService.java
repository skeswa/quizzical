package org.quizzical.backend.scheduler.api;

import org.gauntlet.core.api.ApplicationException;
import org.quizzical.backend.security.authorization.api.model.user.User;

public interface IQuizTakerReminderService {
	void createNewQuizReminderSchedule(final User user, final String cronDefinition) throws ApplicationException;
	void voidExistingQuizReminderSchedule(final User user) throws ApplicationException;
}
