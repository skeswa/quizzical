package org.quizzical.backend.testdesign.impl.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.testdesign.api.ITestDesignTemplateGeneratorService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;
import org.quizzical.backend.testdesign.impl.TestDesignTemplateGeneratorImpl;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
		manager.add(createComponent()
				.setInterface(ITestDesignTemplateGeneratorService.class.getName(), null)
				.setImplementation(TestDesignTemplateGeneratorImpl.class)
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