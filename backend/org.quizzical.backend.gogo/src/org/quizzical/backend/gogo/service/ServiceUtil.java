package org.quizzical.backend.gogo.service;

import java.util.concurrent.TimeUnit;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class ServiceUtil {
    public static Object createServiceFromServiceType(Class type) throws Exception {
		Object svc = null;
		try {
			Bundle bundle = FrameworkUtil.getBundle(ServiceUtil.class);
			ServiceReference sr = getServiceReference(bundle.getBundleContext(), type);
	    	return bundle.getBundleContext().getService(sr);
		} catch (Exception e) {
			throw e;
		}
    }    
    
	private static ServiceReference getServiceReference(BundleContext context, Class targetServiceClass, long timeout)
			throws InterruptedException, InvalidSyntaxException {
		long end = System.currentTimeMillis() + timeout;

		ServiceReference sr = null;
		do {
			TimeUnit.MILLISECONDS.sleep(10);
			sr = getServiceReference(context,targetServiceClass);
		} while ((sr != null) && (System.currentTimeMillis() < end));
		
		return sr;
	}
	
    private static ServiceReference  getServiceReference(BundleContext context, Class targetServiceClass) throws InvalidSyntaxException {

        ServiceReference[] serviceRefs =
        		context.getServiceReferences(targetServiceClass.getName(),null);
        if (serviceRefs == null || serviceRefs.length == 0) {
            return null;
        }
        else {
        	return serviceRefs[0];
        }

    }    
}
