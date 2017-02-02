package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.Dictionary;


public class QuizCommands {
    public final static String SCOPE = "qz";
    public final static String[] FUNCTIONS = new String[] { "reset"};

    @Descriptor("Clears all quizzes and analytics from database")
    public static String reset() throws Exception {
    	IQuizDAOService qSvc  = (IQuizDAOService) createServiceFromServiceType(IQuizDAOService.class);
        qSvc.truncate();
        
        ITestUserAnalyticsDAOService aSvc  = (ITestUserAnalyticsDAOService) createServiceFromServiceType(ITestUserAnalyticsDAOService.class);
        aSvc.truncate();
        
        return "Quizzes and analytics cleared successfully!";
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
