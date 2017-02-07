package org.quizzical.backend.scheduler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.amdatu.scheduling.Job;
import org.amdatu.scheduling.constants.Constants;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.quizzical.backend.scheduler.api.IQuizTakerReminderService;
import org.quizzical.backend.scheduler.job.reminder.QuizTakeReminderJob;
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
	
    private Map<String, ServiceReference> references = new HashMap<String, ServiceReference>();

	@Override
	public void createNewQuizReminderSchedule(User user, String cronDefinition) throws ApplicationException {
		Properties properties = new Properties();
		properties.put(Constants.CRON, cronDefinition);
		properties.put(Constants.DESCRIPTION, user.getEmailAddress());
		
		QuizTakeReminderJob qReminderJob = new QuizTakeReminderJob(notifier,userService,quizSubmissionService,this);
		
		dm.add(dm.createComponent()
				.setInterface(Job.class.getName(), properties)
				.setImplementation(qReminderJob)
				.add(dm.createServiceDependency().setService(IQuizSubmissionDAOService.class).setRequired(true))
				.add(dm.createServiceDependency().setService(IAlertNotifier.class).setRequired(true))
				.add(dm.createServiceDependency().setService(IUserDAOService.class).setRequired(true))
				.add(dm.createServiceDependency().setService(LogService.class).setRequired(false)));
	}

	@Override
	public void voidExistingQuizReminderSchedule(User user) throws ApplicationException {
		// TODO Auto-generated method stub
		
	}
	
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

}
