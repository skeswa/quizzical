package org.gauntlet.quizzes.rest.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizTakeDAOService;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorManagerService;
import org.gauntlet.quizzes.rest.QuizResource;
import org.gauntlet.quizzes.rest.QuizSubmissionResource;
import org.gauntlet.quizzes.rest.QuizTypeResource;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(QuizResource.class)
				.add(createServiceDependency().setService(IQuizDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IQuizGeneratorManagerService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(QuizTypeResource.class)
				.add(createServiceDependency().setService(IQuizDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(QuizSubmissionResource.class)
				.add(createServiceDependency().setService(IQuizDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IQuizTakeDAOService.class)
						.setRequired(true))			
				.add(createServiceDependency().setService(IQuizGeneratorManagerService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));		
	}

	@Override
	public synchronized void destroy(BundleContext context,
			DependencyManager manager) throws Exception {
	}
}