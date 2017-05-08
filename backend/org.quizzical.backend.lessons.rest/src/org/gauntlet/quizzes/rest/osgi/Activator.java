package org.gauntlet.quizzes.rest.osgi;

import java.util.Properties;

import org.amdatu.web.rest.jaxrs.AmdatuWebRestConstants;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.rest.UserLessonResource;
import org.gauntlet.quizzes.rest.LessonStatusResource;
import org.gauntlet.quizzes.rest.LessonTypeResource;
import org.gauntlet.quizzes.rest.UserLessonPlanResource;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.contentrepository.api.dao.IContentItemDAOService;
import org.quizzical.backend.security.authentication.jwt.api.IJWTTokenService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
		Properties serviceProperties = new Properties();
	      serviceProperties.put(AmdatuWebRestConstants.JAX_RS_RESOURCE_BASE, "/");
		manager.add(createComponent()
				.setInterface(Object.class.getName(), serviceProperties)
				.setImplementation(UserLessonResource.class)
				.add(createServiceDependency().setService(IQuizDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(ILessonsDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IJWTTokenService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IContentItemDAOService.class)
						.setRequired(true))				
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), serviceProperties)
				.setImplementation(LessonTypeResource.class)
				.add(createServiceDependency().setService(ILessonsDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IJWTTokenService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), serviceProperties)
				.setImplementation(LessonStatusResource.class)
				.add(createServiceDependency().setService(ILessonsDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IJWTTokenService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
		manager.add(createComponent()
				.setInterface(Object.class.getName(), serviceProperties)
				.setImplementation(UserLessonPlanResource.class)
				.add(createServiceDependency().setService(ILessonsDAOService.class)
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