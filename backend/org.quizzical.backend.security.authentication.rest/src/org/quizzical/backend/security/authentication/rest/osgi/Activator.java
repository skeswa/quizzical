package org.quizzical.backend.security.authentication.rest.osgi;

import java.util.Properties;

import org.amdatu.web.rest.jaxrs.AmdatuWebRestConstants;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authentication.jwt.api.IJWTTokenService;
import org.quizzical.backend.security.authentication.rest.LoginResource;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
		Properties serviceProperties = new Properties();
	      serviceProperties.put(AmdatuWebRestConstants.JAX_RS_RESOURCE_BASE, "/");
		manager.add(createComponent()
				.setInterface(Object.class.getName(), serviceProperties)
				.setImplementation(LoginResource.class)
				.setCallbacks("init", null, null, null)//init, start, stop and destroy.
				.add(createServiceDependency().setService(IUserDAOService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(IQuizDAOService.class)
						.setRequired(true))				
				.add(createServiceDependency().setService(IJWTTokenService.class)
						.setRequired(true))
				.add(createServiceDependency().setService(LogService.class)
						.setRequired(false)));
	}

	@Override
	public synchronized void destroy(BundleContext context,
			DependencyManager manager) throws Exception {
	}
}