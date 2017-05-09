package org.gauntlet.lessons.service.impl.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.lessons.service.impl.LessonEventHandlerImpl;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase {

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {

	}

	@Override
	public void init(BundleContext arg0, DependencyManager dm) throws Exception {
		dm.add(createComponent()
				.setInterface(new String[]{EventHandler.class.getName()},null)
				.setImplementation(LessonEventHandlerImpl.class)
				.add(createServiceDependency().setService(ILessonsDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IQuizSubmissionDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false)));	
		}
}
