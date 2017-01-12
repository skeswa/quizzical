package org.quizzical.backend.security.itests;

import javax.persistence.EntityManager;

import org.amdatu.bndtools.test.BaseOSGiServiceTest;
import org.amdatu.multitenant.TenantFactoryConfiguration;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.model.user.User;

import junit.framework.Assert;


public class UserDAOTest extends BaseOSGiServiceTest<LogService> {
	private volatile DependencyManager m_manager;
	private final BundleContext m_bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

	public UserDAOTest() {
		super(LogService.class);
	}
	
	@Override
	public void setUp() throws Exception {		
		super.setUp();
		addServiceDependencies(LogService.class);
		getService(EntityManager.class, String.format("%s=%s",org.osgi.service.jpa.EntityManagerFactoryBuilder.JPA_UNIT_NAME,"idm"));
		getService(IUserDAOService.class);
	}
	
    /**
     * Tears down an individual test case.
     */
    @Override
	public void tearDown() {
    }
    
    public void testUserCreation() throws Exception {
    	IUserDAOService svc = getService(IUserDAOService.class,null);
    	Assert.assertNotNull(svc);
    	User du = new User("mandisakeswa999@gmail.com","M","K");
    	svc.provide(du);
    }
    
    @Override
    protected <T> T getService(Class<T> serviceClass, String filterString) throws InvalidSyntaxException {
        T serviceInstance = null;

        ServiceTracker serviceTracker;
        if (filterString == null) {
            serviceTracker = new ServiceTracker(context, serviceClass.getName(), null);
        }
        else {
            String classFilter = "(" + Constants.OBJECTCLASS + "=" + serviceClass.getName() + ")";
            filterString = "(&" + classFilter + filterString + ")";
            serviceTracker = new ServiceTracker(context, context.createFilter(filterString), null);
        }
        serviceTracker.open();
        try {
            serviceInstance = (T) serviceTracker.waitForService(30 * 1000);

            if (serviceInstance == null) {
                fail(serviceClass + " service not found.");
            }
            else {
                return serviceInstance;
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            fail(serviceClass + " service not available: " + e.toString());
        }

        return serviceInstance;
    }
    
}
