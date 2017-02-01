package org.quizzical.backend.scheduler.impl;

import java.util.HashMap;
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
import org.quizzical.backend.sms.api.IAlertNotifier;

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
		
		QuizTakeReminderJob qReminderJob = new QuizTakeReminderJob(notifier,userService,quizSubmissionService);
		
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

}
