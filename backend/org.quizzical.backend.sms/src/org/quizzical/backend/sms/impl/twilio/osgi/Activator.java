package org.quizzical.backend.sms.impl.twilio.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.sms.api.IAlertNotifier;
import org.quizzical.backend.sms.impl.twilio.TwilioAlertNotifierImpl;

public class Activator extends DependencyActivatorBase {
	private static final String PID = "org.quizzical.backend.sms.impl.twilio";
	
    @Override
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
        manager.add(createComponent()
        	.setInterface(IAlertNotifier.class.getName(), null)
            .setImplementation(TwilioAlertNotifierImpl.class)
            .add(createConfigurationDependency().setPid(PID))
            .setCallbacks(null, "start", null, null)//init, start, stop and destroy.
            .add(createServiceDependency()
                .setService(LogService.class)
                .setRequired(false))
            );
    }

    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}