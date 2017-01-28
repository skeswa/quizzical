package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.gauntlet.problems.api.dao.IProblemDAOService;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

public class ProblemCommands {
    public final static String SCOPE = "prblm";
    public final static String[] FUNCTIONS = new String[] { "addcatlesson","showcat"};

    @Descriptor("Adds lesson to problem category")
    public static String addcatlesson(@Descriptor("Category Id") Long categoryId, @Descriptor("Lesson Id") Long lessonId) throws Exception {
        CommandProblems cmd = new CommandProblems((IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class));
        IProblemDAOService svc = (IProblemDAOService)cmd.get();
        svc.addProblemCategoryLesson(categoryId, lessonId);
        return "Added cat lesson successfully!";
    }
    
    @Descriptor("Shows a problem category by code")
    public static String showcat(@Descriptor("Unique problem category code") String code) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
        return svc.getProblemCategoryByCode(code).toString();
    }   
}
