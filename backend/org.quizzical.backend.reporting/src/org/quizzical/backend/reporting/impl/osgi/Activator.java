package org.quizzical.backend.reporting.impl.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.reporting.api.IReportGenerator;
import org.quizzical.backend.reporting.impl.PdfReportingServiceImpl;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
		manager.add(createComponent()
				.setInterface(IReportGenerator.class.getName(), null)
				.setImplementation(PdfReportingServiceImpl.class)
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
	}
	
	@Override
	public synchronized void destroy(BundleContext context,
			DependencyManager manager) throws Exception {
	}
}