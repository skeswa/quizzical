package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemResponseDAOService;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.Dictionary;
import java.util.List;


public class QuizCommands {
    public final static String SCOPE = "qz";
    public final static String[] FUNCTIONS = new String[] { "reset", "removeunsubmitted","showunsubmitted","showunpractised"};

    @Descriptor("Clears all quizzes and analytics from database")
    public static String reset(@Descriptor("Admin userId") String adminUserId, @Descriptor("Admin password") String adminPassword) throws Exception {
    	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
    	try {
			uSvc.getUserByEmailAndPassword(adminUserId, adminPassword);
		} catch (Exception e) {
			return "Admin creds invalid.";
		}
    	
    	IQuizDAOService qSvc  = (IQuizDAOService) createServiceFromServiceType(IQuizDAOService.class);
        qSvc.truncate();
        
        ITestUserAnalyticsDAOService aSvc  = (ITestUserAnalyticsDAOService) createServiceFromServiceType(ITestUserAnalyticsDAOService.class);
        aSvc.truncate();
        
        return "Quizzes and analytics cleared successfully!";
    }
    
    @Descriptor("Clears all unsubmitted quizzes")
    public static String removeunsubmitted(@Descriptor("userId") String userId) throws Exception {
       	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
    	IQuizSubmissionDAOService qsSvc  = (IQuizSubmissionDAOService) createServiceFromServiceType(IQuizSubmissionDAOService.class);
    	IQuizDAOService qSvc  = (IQuizDAOService) createServiceFromServiceType(IQuizDAOService.class);
       
    	List<Quiz> qs = qsSvc.findQuizzesWithNoSubmission(uSvc.getUserByEmail(userId));
    	qs.stream()
    		.forEach(q -> {
    			try {
    				qSvc.forceDelete(q.getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		});
        return "Cleared all unsubmitted quizzes!";
    }
    
    @Descriptor("Show all unsubmitted quizzes")
    public static String showunsubmitted(@Descriptor("userId") String userId) throws Exception {
       	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
    	IQuizSubmissionDAOService qsSvc  = (IQuizSubmissionDAOService) createServiceFromServiceType(IQuizSubmissionDAOService.class);
    	IQuizDAOService qSvc  = (IQuizDAOService) createServiceFromServiceType(IQuizDAOService.class);
       
    	List<Quiz> qs = qsSvc.findQuizzesWithNoSubmission(uSvc.getUserByEmail(userId));
    	qs.stream()
    		.forEach(q -> {
    			try {
    				System.out.println(String.format("%d,%s", q.getId(),q.getName()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		});
        return "Cleared all unsubmitted quizzes!";
    }
    
    @Descriptor("Show all unpracticed problems")
    public static void showunpractised(@Descriptor("userId") String userId) throws Exception {
       	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
    	IProblemDAOService problemDAOService  = (IProblemDAOService) createServiceFromServiceType(IProblemDAOService.class);
    	IQuizProblemResponseDAOService quizProblemResponseService  = (IQuizProblemResponseDAOService) createServiceFromServiceType(IQuizProblemResponseDAOService.class);
    	
    	User user = uSvc.getUserByEmail(userId);

    	final List<Long> userProblemIds = quizProblemResponseService.getAllUserPracticedProblemIds(user);
    	final List<Problem> problems = problemDAOService.getAllUserNonQuizzedProblems(user.getCurrentProblemTypeId(), user, userProblemIds, 1000);

    	
    	problems.stream()
    		.forEach(p -> {
    			try {
    				System.out.println(String.format("%d,%s", p.getId(),p.getCode()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		});
    }
    

    
    public static String getConfigStringValue(BundleContext context, String key, Dictionary<String, ?> properties,
            String defaultValue) throws ConfigurationException {

            String value = null;
            if (properties != null && properties.get(key) != null) {
                value = properties.get(key).toString();
            }
            if (context != null && value == null) {
                value = context.getProperty(key);
            }
            if (value == null) {
                return defaultValue;
            }
            return value;
        }   
    
    public static int getConfigIntValue(BundleContext context, String key, Dictionary<String, ?> properties,
            int defaultValue) throws ConfigurationException {

            String value = null;
            if (properties != null && properties.get(key) != null) {
                value = properties.get(key).toString();
            }
            if (context != null && value == null) {
                value = context.getProperty(key);
            }
            if (value == null) {
                return defaultValue;
            }
            try {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException e) {
                throw new ConfigurationException(key, "not an integer", e);
            }
        }    
}
