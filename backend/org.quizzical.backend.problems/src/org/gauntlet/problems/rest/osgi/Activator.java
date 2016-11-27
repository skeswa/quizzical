package org.gauntlet.problems.rest.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.rest.CategoriesResource;
import org.gauntlet.problems.rest.DifficultiesResource;
import org.gauntlet.problems.rest.ProblemsResource;
import org.gauntlet.problems.rest.SourcesResource;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(ProblemsResource.class)
				.add(createServiceDependency().setService(IProblemDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(CategoriesResource.class)
				.add(createServiceDependency().setService(IProblemDAOService.class)
						.setRequired(true)));
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(DifficultiesResource.class)
				.add(createServiceDependency().setService(IProblemDAOService.class)
						.setRequired(true)));
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(SourcesResource.class)
				.add(createServiceDependency().setService(IProblemDAOService.class)
						.setRequired(true)));
	}

	@Override
	public synchronized void destroy(BundleContext context,
			DependencyManager manager) throws Exception {
	}
}