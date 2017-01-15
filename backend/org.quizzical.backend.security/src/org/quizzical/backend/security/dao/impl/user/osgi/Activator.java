package org.quizzical.backend.security.dao.impl.user.osgi;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import javax.persistence.EntityManager;

import org.amdatu.jta.ManagedTransactional;
import org.amdatu.security.authentication.idprovider.UserLookupService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;

import static org.amdatu.security.account.AccountConstants.TOPIC_PREFIX;

import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.dao.user.UserProfileService;
import org.quizzical.backend.security.dao.impl.user.UserDAOServiceImpl;
import org.quizzical.backend.security.dao.impl.user.UserProfileServiceImpl;

public class Activator extends DependencyActivatorBase {

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {

	}

	@Override
	public void init(BundleContext arg0, DependencyManager dm) throws Exception {
		String entityManagerModuleFilter = String.format("(%s=%s)",org.osgi.service.jpa.EntityManagerFactoryBuilder.JPA_UNIT_NAME,"idm");
		
		Properties props;
		
		props = new Properties();
		props.put(ManagedTransactional.SERVICE_PROPERTY, IUserDAOService.class.getName());
		dm.add(createComponent().setInterface(Object.class.getName(), props)
				.setImplementation(UserDAOServiceImpl.class)
				.add(createServiceDependency().setService(EntityManager.class,entityManagerModuleFilter).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false)));
		

        String[] ifaces = new String[] { EventHandler.class.getName(),
            UserProfileService.class.getName(), UserLookupService.class.getName() };
        
        Dictionary<String, Object> props_ = new Hashtable<>();
        props_.put(EventConstants.EVENT_TOPIC, TOPIC_PREFIX.concat("/*"));
        dm.add(createComponent()
            .setInterface(ifaces, props_)
            .setImplementation(UserProfileServiceImpl.class)
            .add(createServiceDependency().setService(LogService.class).setRequired(false)));
		
	}
}
