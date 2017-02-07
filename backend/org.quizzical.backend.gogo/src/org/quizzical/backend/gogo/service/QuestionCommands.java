package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.Dictionary;


public class QuestionCommands {
    public final static String SCOPE = "qstn";
    public final static String[] FUNCTIONS = new String[] { "show","showbyid","delete"};

    @Descriptor("Shows a question by code")
    public static String show(@Descriptor("Unique problem code") String code) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
        return svc.getByCode(code).toString();
    }
    
    @Descriptor("Shows a question by id")
    public static String showbyid(@Descriptor("Unique problem id") Long id) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
        return svc.getByPrimary(id).toString();
    }
    
    @Descriptor("Deletes a question by code")
    public static String delete(@Descriptor("Unique problem code") String code) throws Exception {
       	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
        Problem p = svc.getByCode(code);
        if (p == null)
        	return "Question not found";
        svc.delete(p.getId());
        return "Question deleted successfully!";
    }
}
