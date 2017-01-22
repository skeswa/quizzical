package org.quizzical.backend.testdesign.dao.impl.osgi;

import java.util.Properties;

import javax.persistence.EntityManager;

import org.amdatu.jta.ManagedTransactional;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;
import org.quizzical.backend.testdesign.api.model.Constants;
import org.quizzical.backend.testdesign.dao.impl.TestDesignTemplateContentTypeDAOImpl;
import org.quizzical.backend.testdesign.dao.impl.TestDesignTemplateDAOImpl;

public class Activator extends DependencyActivatorBase {

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {

	}

	@Override
	public void init(BundleContext arg0, DependencyManager dm) throws Exception {
		String entityManagerModuleFilter = String.format("(%s=%s)",org.osgi.service.jpa.EntityManagerFactoryBuilder.JPA_UNIT_NAME,Constants.Q7L_JPA_MODULE);
		
		Properties props;
		
		props = new Properties();
		props.put(ManagedTransactional.SERVICE_PROPERTY, ITestDesignTemplateDAOService.class.getName());
		dm.add(createComponent().setInterface(Object.class.getName(), props)
				.setImplementation(TestDesignTemplateDAOImpl.class)
				.add(createServiceDependency().setService(EntityManager.class,entityManagerModuleFilter).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false)));
		
		props = new Properties();
		props.put(ManagedTransactional.SERVICE_PROPERTY, ITestDesignTemplateContentTypeDAOService.class.getName());
		dm.add(createComponent().setInterface(Object.class.getName(), props)
				.setImplementation(TestDesignTemplateContentTypeDAOImpl.class)
				.add(createServiceDependency().setService(EntityManager.class,entityManagerModuleFilter).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false)));
	}
}
