package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.lessons.api.model.Lesson;
import org.gauntlet.lessons.api.model.LessonProblem;
import org.gauntlet.lessons.api.model.UserLesson;
import org.gauntlet.lessons.api.model.UserLessonPlan;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.problems.api.model.ProblemSource;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.generator.api.Constants;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorManagerService;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.List;
import java.util.stream.Collectors;

public class LessonCommands {
    public final static String SCOPE = "lssn";
    public final static String[] FUNCTIONS = new String[] { "create","del","list","addplan","resetplan","listplans","addlesson"};

    //-- Lesson
    @Descriptor("Creates lesson from problems whose source id is sourceId")
    public static String create(@Descriptor("Lesson name") String lessonName, 
    							@Descriptor("Source Id") Long sourceId,
    							@Descriptor("Category Id") Long categoryId,
    							@Descriptor("Content Id") Long contentItemId) throws Exception {
    	IProblemDAOService svc = (IProblemDAOService)createServiceFromServiceType(IProblemDAOService.class);
    	final ProblemSource ps = svc.getProblemSourceByPrimary(sourceId);
    	List<Problem> probs = svc.findAllBySource(sourceId);
    	List<LessonProblem> questions = probs.stream()
    			.map(p -> {
    				final String code = String.format("%s-%d",ps.getCode(),p.getSourceIndexWithinPage());
    				return new LessonProblem(code,code,p.getSourceIndexWithinPage(),p.getId());
    			})
    			.collect(Collectors.toList());
    	
    	Lesson lesson = new Lesson(lessonName, ps.getCode(), categoryId, contentItemId, questions);
    	
    	ILessonsDAOService lsvc = (ILessonsDAOService)createServiceFromServiceType(ILessonsDAOService.class);
    	lesson = lsvc.provide(lesson);

    	return "Added lesson successfully!";
    }
    
    @Descriptor("List lessons")
    public static void list() throws Exception {
    	ILessonsDAOService lsvc = (ILessonsDAOService)createServiceFromServiceType(ILessonsDAOService.class);
        List<Lesson> lessons = lsvc.findAll();
        lessons.stream()
        	.forEach(cat -> {
        		System.out.println(String.format("%d-%s",cat.getId(),cat.getCode()));
        	});
    }  
    
    
    @Descriptor("Deletes lesson ")
    public static String del(@Descriptor("Lesson ID") Long lessonId, @Descriptor("Admin userId") String adminUserId, @Descriptor("Admin password") String adminPassword) throws Exception {
    	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
    	try {
			uSvc.getUserByEmailAndPassword(adminUserId, adminPassword);
		} catch (Exception e) {
			return "Admin creds invalid.";
		}
    	
    	ILessonsDAOService lsvc = (ILessonsDAOService)createServiceFromServiceType(ILessonsDAOService.class);
    	lsvc.delete(lessonId);
        
        return "Deleted lesson successfully!";
    }
    
    @Descriptor("Creates user lesson plan")
    public static String addplan(@Descriptor("Email address as userid") String userId, @Descriptor("Admin userId") String adminUserId, @Descriptor("Admin password") String adminPassword) throws Exception {
    	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
    	try {
			uSvc.getUserByEmailAndPassword(adminUserId, adminPassword);
		} catch (Exception e) {
			return "Admin creds invalid.";
		}
    	
    	User user = uSvc.getByEmail(userId);
    	
    	ILessonsDAOService lsvc = (ILessonsDAOService)createServiceFromServiceType(ILessonsDAOService.class);
    	String code = String.format("%s Lesson Plan", user.getFirstName());
    	UserLessonPlan plan = new UserLessonPlan(code, code, user.getId());
    	plan = lsvc.provideUserLessonPlan(plan);
    	
        return String.format("Plan %s created successfully!",plan.getCode());
    }
        
    
    @Descriptor("Add lesson to UserLessonPlan")
    public static String addlesson(@Descriptor("Lesson ID") Long lessonId, @Descriptor("UserId ID") String targetUserId) throws Exception {
    	ILessonsDAOService lsvc = (ILessonsDAOService)createServiceFromServiceType(ILessonsDAOService.class);
    	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
    	IQuizGeneratorManagerService gSvc  = (IQuizGeneratorManagerService) createServiceFromServiceType(IQuizGeneratorManagerService.class);
    	IProblemDAOService pSvc  = (IProblemDAOService) createServiceFromServiceType(IProblemDAOService.class);

    	User user = uSvc.getByEmail(targetUserId);
    	UserLessonPlan plan = lsvc.getUserLessonPlanByUserPk(user.getId());
    	Lesson lesson = lsvc.getByPrimary(lessonId);
    	LessonProblem q = lesson.getQuestions().iterator().next();
    	Problem p = pSvc.getByPrimary(q.getProblemId());
    	
    	//Generate a Quiz from Lesson
    	QuizGenerationParameters params = new QuizGenerationParameters();
    	params.setGeneratorType(Constants.GENERATOR_TYPE_BY_SOURCE);
    	params.setProblemSourceId(p.getSource().getId());
    	Quiz quiz = gSvc.generate(user, params);
    	
    	//Create UserLesson
    	UserLesson ul = new UserLesson(user.getId(),lesson, quiz);
    	if (plan.getCurrentLesson() == null) {
    		ul = lsvc.provideLessonAsCurrentToPlan(ul, plan.getId());
    	}
    	else {
    		ul = lsvc.provideLessonAsUpcomingToPlan(ul, plan.getId());
    	}
    	
        return String.format("UserLesson %s created successfully!",ul.getCode());
    }    
    
    @Descriptor("List lesson plans")
    public static void listplans() throws Exception {
    	ILessonsDAOService lsvc = (ILessonsDAOService)createServiceFromServiceType(ILessonsDAOService.class);
    	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
        List<UserLessonPlan> lps = lsvc.findAllUserLessonPlans();
        lps.stream()
        	.forEach(lp -> {
        		User user = null;
				try {
					user = uSvc.getByPrimaryKey(lp.getUserId());
					System.out.println(String.format("%d-[%s]-[%s]",lp.getId(),lp.getCode(),user.getEmailAddress()));
				} catch (Exception e) {
					e.printStackTrace();
				}
        	});
    } 
    
    @Descriptor("Resets user lesson plan")
    public static String resetplan(@Descriptor("UserId ID") String targetUserId) throws Exception {
    	ILessonsDAOService lsvc = (ILessonsDAOService)createServiceFromServiceType(ILessonsDAOService.class);
    	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
    	
    	User user = uSvc.getByEmail(targetUserId);
    	UserLessonPlan plan = lsvc.getUserLessonPlanByUserPk(user.getId());
    	
    	lsvc.resetUserLessonPlan(user);
    	
    	return String.format("LessonPlan for user %s deleted successfully!",user.getFirstName());
    } 
}
