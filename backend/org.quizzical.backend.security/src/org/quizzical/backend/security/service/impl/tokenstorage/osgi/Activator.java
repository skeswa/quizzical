package org.quizzical.backend.security.service.impl.tokenstorage.osgi;

import java.util.Properties;

import javax.persistence.EntityManager;

import org.amdatu.jta.ManagedTransactional;
import org.amdatu.security.tokenprovider.TokenStorageProvider;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.tokenstorage.IExpiredTokenPurger;
import org.quizzical.backend.security.service.impl.tokenstorage.JPATokenStorageProvider;

public class Activator extends DependencyActivatorBase {

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {

	}

	@Override
	public void init(BundleContext arg0, DependencyManager dm) throws Exception {
		String entityManagerModuleFilter = String.format("(%s=%s)",org.osgi.service.jpa.EntityManagerFactoryBuilder.JPA_UNIT_NAME,"idm");
		
		Properties props;
		
		props = new Properties();
		props.put(ManagedTransactional.SERVICE_PROPERTY, new String[]{TokenStorageProvider.class.getName(),IExpiredTokenPurger.class.getName()});
		dm.add(createComponent().setInterface(new String[]{TokenStorageProvider.class.getName(),IExpiredTokenPurger.class.getName()}, props)
				.setImplementation(JPATokenStorageProvider.class)
				.add(createServiceDependency().setService(EntityManager.class,entityManagerModuleFilter).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false)));
	}
}
