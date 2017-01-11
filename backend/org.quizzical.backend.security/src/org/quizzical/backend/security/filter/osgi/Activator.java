package org.quizzical.backend.security.filter.osgi;


import java.util.Properties;

import javax.servlet.Filter;

import org.amdatu.security.tokenprovider.TokenProvider;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.quizzical.backend.security.filter.SecurityFilter;


public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext bc, DependencyManager dm) throws Exception {
		Properties properties = new Properties();
		properties.put("pattern", "/*.*");
		dm.add(createComponent()
				.setInterface(Filter.class.getName(), properties)
				.setImplementation(SecurityFilter.class)
				.add(createServiceDependency().setService(TokenProvider.class).setRequired(true)));
				//.add(createServiceDependency().setService(AdminLoginService.class).setRequired(true)));
	}

	@Override
	public void destroy(BundleContext bc, DependencyManager dm)
			throws Exception {

	}

}