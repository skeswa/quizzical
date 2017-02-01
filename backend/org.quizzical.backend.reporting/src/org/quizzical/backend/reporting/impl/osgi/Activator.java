package org.quizzical.backend.reporting.impl.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.contentrepository.api.dao.IContentItemDAOService;
import org.quizzical.backend.mail.api.IMailService;
import org.quizzical.backend.reporting.analytics.impl.UserAnalyticsReportingConfiguration;
import org.quizzical.backend.reporting.analytics.impl.UserAnalyticsReportingServiceImpl;
import org.quizzical.backend.reporting.api.IUserAnalyticsReporting;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;

public class Activator extends DependencyActivatorBase {
	private static final String PID = "org.quizzical.backend.reporting";

	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
/*		manager.add(createComponent()
				.setInterface(IReportGenerator.class.getName(), null)
				.setImplementation(PdfReportingServiceImpl.class)
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));*/
		
		manager.add(createComponent()
				.setInterface(IUserAnalyticsReporting.class.getName(), null)
				.setImplementation(UserAnalyticsReportingServiceImpl.class)
				.add(createServiceDependency().setService(IUserDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(ITestUserAnalyticsDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IMailService.class).setRequired(true))
				.add(createServiceDependency().setService(IContentItemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(UserAnalyticsReportingConfiguration.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false)));		
		
		
		manager.add(createComponent()
				.setInterface(UserAnalyticsReportingConfiguration.class.getName(), null)
				.setImplementation(UserAnalyticsReportingConfiguration.class)
				.add(createConfigurationDependency().setPid(PID))
				.add(createServiceDependency().setService(LogService.class).setRequired(false)));		
	}
	
	@Override
	public synchronized void destroy(BundleContext context,
			DependencyManager manager) throws Exception {
	}
}