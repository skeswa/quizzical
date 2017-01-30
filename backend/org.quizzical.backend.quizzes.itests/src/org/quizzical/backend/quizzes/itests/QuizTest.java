package org.quizzical.backend.quizzes.itests;

import org.amdatu.bndtools.test.BaseOSGiServiceTest;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.quizzes.api.dao.IQuizProblemResponseDAOService;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorManagerService;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.model.user.User;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplate;

import junit.framework.Assert;


public class QuizTest extends BaseOSGiServiceTest<LogService> {
	private volatile DependencyManager m_manager;
	private final BundleContext m_bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

	public QuizTest() {
		super(LogService.class);
	}
	
	@Override
	public void setUp() throws Exception {		
		super.setUp();
		addServiceDependencies(LogService.class);
		getService(IUserDAOService.class);
		getService(IQuizSubmissionDAOService.class);
	}
	
    /**
     * Tears down an individual test case.
     */
    @Override
	public void tearDown() {
    }
    
    
    public void testQuiz() throws Exception {
    	IUserDAOService userSvc = getService(IUserDAOService.class,null);
    	Assert.assertNotNull(userSvc);
    	User user = userSvc.getByEmail("test@me.io");
    	Assert.assertNotNull(user);
    	
    	IQuizProblemResponseDAOService svc1 = getService(IQuizProblemResponseDAOService.class,null);
    	Assert.assertNotNull(svc1);
    	svc1.add(new QuizProblemResponse("1234", null, true, true, 1111, null));
    	
    	svc1.findAll();
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
