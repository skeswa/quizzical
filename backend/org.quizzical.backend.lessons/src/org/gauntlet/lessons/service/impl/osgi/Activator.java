package org.gauntlet.lessons.service.impl.osgi;

import java.util.Hashtable;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.lessons.api.model.Constants;
import org.gauntlet.lessons.service.impl.LessonEventHandlerImpl;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase {

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {

	}

	@Override
	public void init(BundleContext arg0, DependencyManager dm) throws Exception {
        String[] topics = new String[] {
                org.gauntlet.quizzes.api.model.Constants.EVENT_TOPIC_QUIZ_SUBMITTED
            };
        Hashtable props = new Hashtable();
        props.put(EventConstants.EVENT_TOPIC, topics);
		dm.add(createComponent()
				.setInterface(new String[]{EventHandler.class.getName()},props)
				.setImplementation(LessonEventHandlerImpl.class)
				.add(createServiceDependency().setService(ILessonsDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IQuizSubmissionDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false)));	
		}
}
