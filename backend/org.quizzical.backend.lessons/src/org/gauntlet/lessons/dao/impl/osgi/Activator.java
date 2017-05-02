package org.gauntlet.lessons.dao.impl.osgi;

import java.util.Properties;

import javax.persistence.EntityManager;

import org.amdatu.jta.ManagedTransactional;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.lessons.api.model.Constants;
import org.gauntlet.lessons.dao.impl.LessonDAOImpl;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase {

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {

	}

	@Override
	public void init(BundleContext arg0, DependencyManager dm) throws Exception {
		String entityManagerModuleFilter = String.format("(%s=%s)",org.osgi.service.jpa.EntityManagerFactoryBuilder.JPA_UNIT_NAME,Constants.GNT_JPA_MODULE);
		
		Properties props;
		
		props = new Properties();
		props.put(ManagedTransactional.SERVICE_PROPERTY, ILessonsDAOService.class.getName());
		dm.add(createComponent().setInterface(Object.class.getName(), props)
				.setImplementation(LessonDAOImpl.class)
				.add(createServiceDependency().setService(EntityManager.class,entityManagerModuleFilter).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false)));
		}
}
