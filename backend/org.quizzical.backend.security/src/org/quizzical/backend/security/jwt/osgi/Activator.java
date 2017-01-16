package org.quizzical.backend.security.jwt.osgi;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.jwt.JWTTokenServiceImpl;
import org.quizzical.backend.security.jwt.api.IJWTTokenService;

public class Activator extends DependencyActivatorBase {
	public final static String PID = "org.quizzical.backend.security.jwt";
    @Override
    public void init(BundleContext context, DependencyManager dm) throws Exception {
        Dictionary<String, Object> props = new Hashtable<>();
        props.put(Constants.SERVICE_PID, PID);
        dm.add(createComponent()
            .setInterface(new String[] { ManagedService.class.getName(), IJWTTokenService.class.getName() }, props)
            .setImplementation(JWTTokenServiceImpl.class)
            .add(createServiceDependency().setService(LogService.class).setRequired(false)));
    }
}
