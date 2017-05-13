package org.quizzical.backend.gogo.service;

import org.apache.commons.io.IOUtils;
import org.apache.felix.service.command.Descriptor;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.gauntlet.problems.api.model.ProblemPicture;
import org.gauntlet.problems.api.model.ProblemSource;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class ProblemCommands {
    public final static String SCOPE = "prblm";
    public final static String[] FUNCTIONS = new String[] { "showdiffs","adddiff","add","apicupdate","qpicupdate","addcatlesson","addcat","showcat","showcats","addsource","showsources","calcs","answer","index","page","pages","source","diff","src","calc","mc","del","delbysrc"};

    //-- Category
    @Descriptor("Adds lesson to problem category")
    public static String addcatlesson(@Descriptor("Category Id") Long categoryId, @Descriptor("Lesson Id") Long lessonId) throws Exception {
        CommandProblems cmd = new CommandProblems((IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class));
        IProblemDAOService svc = (IProblemDAOService)cmd.get();
        svc.addProblemCategoryLesson(categoryId, lessonId);
        return "Added cat lesson successfully!";
    }
    
    @Descriptor("Adds new problem category")
    public static Long addcat(@Descriptor("Category name") String catName) throws Exception {
        CommandProblems cmd = new CommandProblems((IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class));
        IProblemDAOService svc = (IProblemDAOService)cmd.get();
        return svc.provideProblemCategory(catName).getId();
    }
    
    @Descriptor("Shows a problem category by code")
    public static String showcat(@Descriptor("Unique problem category code") String code) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
        return svc.getProblemCategoryByCode(code).toString();
    }   
    
    @Descriptor("Shows a problem categories")
    public static void showcats() throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
        List<ProblemCategory> cats = svc.findAllProblemCategories(0, 100);
        cats.stream()
        	.forEach(cat -> {
        		System.out.println(String.format("%d-%s",cat.getId(),cat.getCode()));
        	});
    }  
    
    
    //-- Source
    @Descriptor("Adds new problem source")
    public static Long addsource(@Descriptor("Source name") String sourceName) throws Exception {
        CommandProblems cmd = new CommandProblems((IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class));
        IProblemDAOService svc = (IProblemDAOService)cmd.get();
        return svc.provideProblemSource(sourceName).getId();
    }
    
    @Descriptor("Shows a problem sources")
    public static void showsources() throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
        List<ProblemSource> cats = svc.findAllProblemSources(0, 100);
        cats.stream()
        	.forEach(cat -> {
        		System.out.println(String.format("%d-%s",cat.getId(),cat.getCode()));
        	});
    }  
    
    //-- Difficulty
    @Descriptor("Adds new problem difficulty")
    public static Long adddiff(@Descriptor("Difficulty name") String diffName) throws Exception {
        CommandProblems cmd = new CommandProblems((IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class));
        IProblemDAOService svc = (IProblemDAOService)cmd.get();
        return svc.provideProblemDifficulty(diffName).getId();
    }
    
    @Descriptor("Shows a problem difficulties")
    public static void showdiffs() throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
        List<ProblemDifficulty> cats = svc.findAllProblemDifficulties(0, 100);
        cats.stream()
        	.forEach(cat -> {
        		System.out.println(String.format("%d-%s",cat.getId(),cat.getCode()));
        	});
    }  
        
    
    //--
    @Descriptor("Adds new problem")
    public static String add(@Descriptor("Source ID") Long sourceId,
    		@Descriptor("Source Page") Integer sourcePage,
    		@Descriptor("Index in Page") Integer indexInPage,
    		@Descriptor("Category ID") Long categoryId,
    		@Descriptor("Difficulty ID") Long difficultyId,
    		@Descriptor("Calculator Allowed?") Boolean calcAllowed,
    		@Descriptor("MultipleChoice?") Boolean multipleChoice,
    		@Descriptor("Path to Q image") String qPicPath,
    		@Descriptor("Path to A image") String aPicPath,
    		@Descriptor("Answer") String answer) throws Exception {
    	IProblemDAOService psvc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	
    	FileInputStream fis = null;
    	byte[] qcontent = null;
    	byte[] acontent = null;
    	ProblemPicture qpic = null;
    	ProblemPicture apic = null;
		try {
			File f = new File(qPicPath);
			fis = new FileInputStream(f);
			qcontent = IOUtils.toByteArray(fis);
			String code = Long.toString(System.currentTimeMillis()) + f.getName();
			qpic = new ProblemPicture(code, code, qcontent, "image/png", f.length());
			
			fis = new FileInputStream(new File(aPicPath));
			acontent = IOUtils.toByteArray(fis);
			code = Long.toString(System.currentTimeMillis()) + f.getName();
			apic = new ProblemPicture(code, code, acontent, "image/png", f.length());
		} finally {
			if (fis != null)
				fis.close();
		}
		
		Problem newProblem = new Problem(answer, 
    			psvc.getProblemSourceByPrimary(sourceId), 
    			psvc.getProblemCategoryByPrimary(categoryId), 
    			sourcePage, 
    			indexInPage, 
    			psvc.getProblemDifficultyByPrimary(difficultyId), 
    			apic, 
    			qpic, 
    			multipleChoice, 
    			calcAllowed);
    	
    	newProblem = psvc.provide(newProblem);
    	
    	return newProblem.getCode();
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
    
    @Descriptor("Updates problems allow calc flag")
    public static void calcs(@Descriptor("Problem Ids") List<Long> Ids,
    		@Descriptor("Allow Calc") Boolean allowCal) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	List<Problem> problems = svc.findAll(Ids);
    	problems.stream()
    		.forEach(problem -> {
    	    	try {
					problem.setRequiresCalculator(allowCal);
					svc.update(problem);
				} catch (Exception e) {
					e.printStackTrace();
				}    			
    		});
    }
    
    @Descriptor("Updates problem multiplechoice flag")
    public static void mc(@Descriptor("Unique problem ID") Long id,
    		@Descriptor("Multiplechoice flag") Boolean mc) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	Problem problem = svc.getByPrimary(id);
    	problem.setMultipleChoice(mc);
    	svc.update(problem);
    }   
    
    @Descriptor("Updates problem Q pic")
    public static void qpicupdate(@Descriptor("problem ID") Long id,
    		@Descriptor("Path to image") String picPath) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	Problem problem = svc.getByPrimary(id);
    	ProblemPicture pic = svc.getProblemPictureByPrimary(problem.getQuestionPicture().getId());
    	
    	FileInputStream fis = null;
    	byte[] content = null;
		try {
			fis = new FileInputStream(new File(picPath));
			content = IOUtils.toByteArray(fis);
		} finally {
			if (fis != null)
				fis.close();
		}
		
		pic.setPicture(content);
    	svc.updateProblemPicture(pic);
    }   
    
    @Descriptor("Updates problem A pic")
    public static void apicupdate(@Descriptor("problem ID") Long id,
    		@Descriptor("Path to image") String picPath) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	Problem problem = svc.getByPrimary(id);
    	ProblemPicture pic = svc.getProblemPictureByPrimary(problem.getAnswerPicture().getId());
    	
    	FileInputStream fis = null;
    	byte[] content = null;
		try {
			fis = new FileInputStream(new File(picPath));
			content = IOUtils.toByteArray(fis);
		} finally {
			if (fis != null)
				fis.close();
		}
		
		pic.setPicture(content);
    	svc.updateProblemPicture(pic);
    } 
    
    @Descriptor("Deletes problem ")
    public static String del(@Descriptor("Problem ID") Long problemId, @Descriptor("Admin userId") String adminUserId, @Descriptor("Admin password") String adminPassword) throws Exception {
    	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
    	try {
			uSvc.getUserByEmailAndPassword(adminUserId, adminPassword);
		} catch (Exception e) {
			return "Admin creds invalid.";
		}
    	
    	IProblemDAOService pSvc  = (IProblemDAOService) createServiceFromServiceType(IProblemDAOService.class);
    	pSvc.delete(problemId);
        
        return "Deleted problem successfully!";
    }
    
    @Descriptor("Deletes problem ")
    public static String delbysrc(@Descriptor("Source ID") Long sourceId, @Descriptor("Admin userId") String adminUserId, @Descriptor("Admin password") String adminPassword) throws Exception {
    	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
    	try {
			uSvc.getUserByEmailAndPassword(adminUserId, adminPassword);
		} catch (Exception e) {
			return "Admin creds invalid.";
		}
    	
    	IProblemDAOService pSvc  = (IProblemDAOService) createServiceFromServiceType(IProblemDAOService.class);
    	pSvc.deleteAllBySourceId(sourceId);
        
        return "Deleted problems successfully!";
    }
}
