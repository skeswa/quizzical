package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentSubType;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.Dictionary;


public class TestDesignCommands {
    public final static String SCOPE = "td";
    public final static String[] FUNCTIONS = new String[] { "delete","addsubtype"};

    @Descriptor("Clears all test designs from database")
    public static String reset() throws Exception {
    	CommandTestDesignTemplates cmd = new CommandTestDesignTemplates((ITestDesignTemplateDAOService)createServiceFromServiceType(IQuizDAOService.class));
    	ITestDesignTemplateDAOService svc = (ITestDesignTemplateDAOService)cmd.get();
        //svc.truncate();
        return "TD's reset successfully!";
    }
    
    @Descriptor("Adds new content subtype")
    public static Long addsubtype(@Descriptor("Content Subtype name") String cstName) throws Exception {
    	ITestDesignTemplateContentTypeDAOService svc = (ITestDesignTemplateContentTypeDAOService)createServiceFromServiceType(ITestDesignTemplateContentTypeDAOService.class);
    	TestDesignTemplateContentSubType st = new TestDesignTemplateContentSubType(cstName, cstName);
		return svc.provideContentSubType(st).getId();
    }
    
    
    @Descriptor("Deletes a test design from database")
    public static String delete(@Descriptor("id of the TD to delete") Long tdId) throws Exception {
    	CommandTestDesignTemplates cmd = new CommandTestDesignTemplates((ITestDesignTemplateDAOService)createServiceFromServiceType(ITestDesignTemplateDAOService.class));
    	ITestDesignTemplateDAOService svc = (ITestDesignTemplateDAOService)cmd.get();
    	svc.delete(tdId);
        return "TD ("+tdId+") deleted successfully!";
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
