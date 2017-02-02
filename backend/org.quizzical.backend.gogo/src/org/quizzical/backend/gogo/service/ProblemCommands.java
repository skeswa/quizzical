package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.problems.api.model.ProblemDifficulty;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.List;

public class ProblemCommands {
    public final static String SCOPE = "prblm";
    public final static String[] FUNCTIONS = new String[] { "addcatlesson","showcat","answer","index","page","pages","source","diff","src","calc","mc"};

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
    
    @Descriptor("Updates problem answer")
    public static void answer(@Descriptor("Unique problem ID") Long id,
    		@Descriptor("Answer") String answer) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	Problem problem = svc.getByPrimary(id);
    	problem.setAnswer(answer);
    	svc.update(problem);
    }     
    
    @Descriptor("Updates problems source page")
    public static void page(@Descriptor("Problem ID") Long problemId,
    		@Descriptor("Source Page Number") Integer sourcePageNumber) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	try {
    		Problem problem = svc.getByPrimary(problemId);
			problem.setSourcePageNumber(sourcePageNumber);
			svc.update(problem);
		} catch (Exception e) {
			e.printStackTrace();
		}  
    } 
    
    @Descriptor("Updates problems source page")
    public static void pages(@Descriptor("Source ID") Long sourceId,
    		@Descriptor("Source Page Number") Integer sourcePageNumber) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	List<Problem> problems = svc.findAllBySource(sourceId);
    	problems.stream()
    		.forEach(problem -> {
    	    	try {
					problem.setSourcePageNumber(sourcePageNumber);
					svc.update(problem);
				} catch (Exception e) {
					e.printStackTrace();
				}    			
    		});
    } 
    
    @Descriptor("Updates problem source page")
    public static void src(@Descriptor("Unique problem ID") Long id,
    		@Descriptor("Source Page #") Integer srcPageNumber) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	Problem problem = svc.getByPrimary(id);
    	problem.setSourcePageNumber(srcPageNumber);
    	svc.update(problem);
    }  
    
    @Descriptor("Updates problem source page")
    public static void diff(@Descriptor("Unique problem ID") Long id,
    		@Descriptor("Difficulty ID") Long difficultyID) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	Problem problem = svc.getByPrimary(id);
    	ProblemDifficulty diff = svc.getProblemDifficultyByPrimary(difficultyID);
    	problem.setDifficulty(diff);;
    	svc.update(problem);
    }   
}
