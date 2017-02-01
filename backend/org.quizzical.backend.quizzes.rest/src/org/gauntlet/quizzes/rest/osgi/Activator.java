package org.gauntlet.quizzes.rest.osgi;

import java.util.Properties;

import org.amdatu.web.rest.jaxrs.AmdatuWebRestConstants;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.dao.IQuizTakeDAOService;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorManagerService;
import org.gauntlet.quizzes.rest.QuizResource;
import org.gauntlet.quizzes.rest.QuizSubmissionResource;
import org.gauntlet.quizzes.rest.QuizTypeResource;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authentication.jwt.api.IJWTTokenService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
		Properties serviceProperties = new Properties();
	      serviceProperties.put(AmdatuWebRestConstants.JAX_RS_RESOURCE_BASE, "/");
		manager.add(createComponent()
				.setInterface(Object.class.getName(), serviceProperties)
				.setImplementation(QuizResource.class)
				.add(createServiceDependency().setService(IQuizDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IQuizGeneratorManagerService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IJWTTokenService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), serviceProperties)
				.setImplementation(QuizTypeResource.class)
				.add(createServiceDependency().setService(IQuizDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IJWTTokenService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), serviceProperties)
				.setImplementation(QuizSubmissionResource.class)
				.add(createServiceDependency().setService(IProblemDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IQuizDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IQuizGeneratorManagerService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IQuizProblemDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IQuizSubmissionDAOService.class)
						.setRequired(true))	
				.add(createServiceDependency().setService(IQuizTakeDAOService.class)
						.setRequired(true))		
				.add(createServiceDependency().setService(IJWTTokenService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));		
	}

	@Override
	public synchronized void destroy(BundleContext context,
			DependencyManager manager) throws Exception {
	}
}