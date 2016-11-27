package org.gauntlet.importer.itests;
import static org.amdatu.testing.configurator.TestConfigurator.configure;
import static org.amdatu.testing.configurator.TestConfigurator.createFactoryConfiguration;
import static org.amdatu.testing.configurator.TestConfigurator.createServiceDependency;
import static org.amdatu.testing.configurator.TestUtils.getBundleContext;
import static org.osgi.framework.Constants.OBJECTCLASS;
import static org.osgi.framework.Constants.SERVICE_PID;
import static org.osgi.framework.FrameworkUtil.createFilter;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.amdatu.testing.configurator.TestConfiguration;
import org.amdatu.testing.configurator.TestConfigurator;
import org.amdatu.testing.configurator.TestUtils;
import org.apache.felix.dm.DependencyManager;
import org.junit.After;
import org.junit.Before;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.util.tracker.ServiceTracker;

public abstract class BaseJPATest {
	public static final long DEFAULT_TIMEOUT = 5000; /* ms */
	
	
	protected volatile DependencyManager m_manager;
	protected TestConfiguration conf;
	
    protected void setUp() {
        conf = configure(this);
        	//.add(createServiceDependency().setService(TenantFactoryConfiguration.class).setRequired(true))      
            //.add(createFactoryConfiguration("org.amdatu.tenant.factory").set("org.amdatu.tenant.pid", TENANT_A).set("org.amdatu.tenant.name", "Tenant A").set(org.conxworks.paas.db.api.Constants.CNX_DB_CLUSTER_ID,"conx-db-cluster-tenants1")
            //    .setSynchronousDelivery(true))                
/*            .add(createFactoryConfiguration("org.amdatu.tenant.factory").set("org.amdatu.tenant.pid", TENANT_B).set("org.amdatu.tenant.name", "Tenant B")
                .setSynchronousDelivery(true))*/;
    }

    @After
    public void tearDown(){
        TestConfigurator.cleanUp(this);
    }
    
    /**
     * Helper method to count the number of service registrations for a given service-class.
     *
     * @param context the bundle context to use;
     * @param service the service to count;
     * @param filter the optional filter to search for, can be <code>null</code>.
     * @return a service registration count, >= 0.
     */
    static int countServices(BundleContext context, String serviceName, String filter) {
        ServiceReference[] serviceReferences = null;
        try {
            serviceReferences = context.getServiceReferences(serviceName, filter);
        }
        catch (InvalidSyntaxException exception) {
            throw new RuntimeException("null-filter is incorrect?!");
        }
        return serviceReferences == null ? 0 : serviceReferences.length;
    }

    /**
     * Helper method to count the number of service registrations for a given service-class.
     *
     * @param context the bundle context to use;
     * @param service the service to count.
     * @return a service registration count, >= 0.
     */
    protected static int countServices(BundleContext context, String serviceName) {
        return countServices(context, serviceName, null /* filter */);
    }
    
    
    /**
     * Waits until either a given number of services is seen, or until a certain timeout occurred.
     *
     * @param timeout the time out, in milliseconds;
     * @param serviceCount the number of services to expect;
     * @param countedServiceClass the service to count.
     * @throws InterruptedException in case we're being interrupted while waiting for the services to come up.
     */
    protected void waitUntilServicesAreRegistered(BundleContext context,long timeout, int serviceCount, Class<?> countedServiceClass)
        throws InterruptedException {
        long end = System.currentTimeMillis() + timeout;

        int sc;
        do {
            TimeUnit.MILLISECONDS.sleep(10);
            sc = countServices(context, countedServiceClass.getName());
        }
        while ((sc != serviceCount) && (System.currentTimeMillis() < end));
    }

    /**
     * @param tenantPID
     * @return
     */
    protected Object  getServiceInstance(BundleContext context,String tenantPID, Class targetServiceClass) throws Exception {
    	final String filter = tenantPID != null ? String.format("(%1$s=%2$s)", "org.amdatu.tenant.pid", tenantPID) : null;
        ServiceReference[] serviceRefs =
        		context.getServiceReferences(targetServiceClass.getName(),
                filter);
        if (serviceRefs == null || serviceRefs.length == 0) {
            return null;
        }

        Object service = context.getService(serviceRefs[0]);
        if (service == null) {
            return null;
        }

        return service;
    }
    
    protected void registerServicePid(BundleContext ctx, String servicePid, Dictionary<String, ?> m_properties, boolean isFactory, long timeOut) {
        try {
            ServiceTracker m_tracker = new ServiceTracker<>(ctx, createManagedServiceFilter(isFactory,servicePid), null);
            m_tracker.open();

            Object service = m_tracker.waitForService(timeOut);
            if (service == null) {
                throw new RuntimeException("Unable to find service!");
            }

            ;
			if (isFactory) {
                // Generate a particular PID for this factory...
                String pid = UUID.randomUUID().toString();

                ((ManagedServiceFactory) service).updated(pid, m_properties);
            } else {
                ((ManagedService) service).updated(m_properties);
            }
        } catch (ConfigurationException | InvalidSyntaxException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            // Ok; we're stopping already...
        }
    }
    
    private Filter createManagedServiceFilter(boolean isFactory, String servicePid) throws InvalidSyntaxException {
        String type = (isFactory ? ManagedServiceFactory.class : ManagedService.class).getName();
        return createFilter(String.format("(&(%s=%s)(%s=%s))", OBJECTCLASS, type, SERVICE_PID, servicePid));
    }
}