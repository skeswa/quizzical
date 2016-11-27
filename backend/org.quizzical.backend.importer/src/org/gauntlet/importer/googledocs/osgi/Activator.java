package org.gauntlet.importer.googledocs.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.importer.googledocs.GSheetProblemsImporter;
import org.gauntlet.importer.googledocs.IGSheetProblemsImporter;
import org.gauntlet.importer.googledocs.problems.Importer;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase {

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {

	}

	@Override
	public void init(BundleContext arg0, DependencyManager dm) throws Exception {
		dm.add(createComponent().setInterface(IGSheetProblemsImporter.class.getName(), null)
				.setImplementation(GSheetProblemsImporter.class))
		;
		
		
		dm.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(Importer.class)
				.setCallbacks(null, "start", null, null)
				.add(createServiceDependency().setService(IProblemDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IGSheetProblemsImporter.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
	}
}
