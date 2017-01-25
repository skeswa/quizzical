package org.quizzical.backend.mail.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.mail.api.IMailService;
import org.quizzical.backend.mail.impl.GoogleMailServiceImpl;


public class Activator extends DependencyActivatorBase {
	private static final String PID = "org.quizzical.backend.mail";

	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
		manager.add(createComponent()
				.setInterface(IMailService.class.getName(), null)
				.setImplementation(GoogleMailServiceImpl.class)
				.add(createConfigurationDependency().setPid(PID))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
		);
	}

	@Override
	public synchronized void destroy(BundleContext context,
			DependencyManager manager) throws Exception {
	}
}
