package org.gauntlet.problems.defaults.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.defaults.Controller;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(Controller.class)
				.setCallbacks(null, "start", null, null)
				.add(createServiceDependency().setService(IProblemDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IUserDAOService.class)
						.setRequired(true))				
				.add(createServiceDependency().setService(IQuizDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(ITestDesignTemplateDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(ITestDesignTemplateContentTypeDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
	}
	
	@Override
	public synchronized void destroy(BundleContext context,
			DependencyManager manager) throws Exception {
	}
}