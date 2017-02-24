package org.quizzical.backend.scheduler.osgi;

import java.util.Properties;

import org.amdatu.scheduling.Job;
import org.amdatu.scheduling.constants.Constants;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quartz.Scheduler;
import org.quizzical.backend.scheduler.api.IQuizTakerReminderService;
import org.quizzical.backend.scheduler.api.IScheduler;
import org.quizzical.backend.scheduler.impl.QuizTakerReminderServiceImpl;
import org.quizzical.backend.scheduler.impl.SchedulerServiceWrapperImpl;
import org.quizzical.backend.scheduler.job.reminder.QuizTakeReminderJob;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.sms.api.IAlertNotifier;

public class Activator extends DependencyActivatorBase {
	private static final String PID = "org.quizzical.backend.scheduler";
	
	@Override
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
        manager.add(createComponent()
        	.setInterface(IQuizTakerReminderService.class.getName(), null)
            .setImplementation(QuizTakerReminderServiceImpl.class)
            .add(createServiceDependency().setService(IQuizSubmissionDAOService.class).setRequired(true))
            .add(createServiceDependency().setService(IAlertNotifier.class).setRequired(true))
            .add(createServiceDependency().setService(IUserDAOService.class).setRequired(true))
            .add(createServiceDependency().setService(LogService.class).setRequired(false))
            );
        
        manager.add(createComponent()
            	.setInterface(IScheduler.class.getName(), null)
                .setImplementation(SchedulerServiceWrapperImpl.class)
                .add(createServiceDependency().setService(Scheduler.class).setRequired(true))
                .add(createServiceDependency().setService(LogService.class).setRequired(false))
                );
        
        //-Jobs
		Properties properties = new Properties();
		properties.put(Constants.CRON, "0 0 16 ? * MON-FRI *");
		manager.add(createComponent()
				.setInterface(Job.class.getName(), properties)
				.setImplementation(QuizTakeReminderJob.class)
				.add(createServiceDependency().setService(IQuizTakerReminderService.class).setRequired(true)));
            
    }

    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}