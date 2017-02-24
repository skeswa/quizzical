package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.quizzical.backend.scheduler.api.IQuizTakerReminderService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class UserCommands {
    public final static String SCOPE = "usr";
    public final static String[] FUNCTIONS = new String[] { "submissions","add", "welcome", "whois", "deactivate", "activate","showactive","lru","ptest","sori"};

    
    @Descriptor("Creates a new user")
    public static String add(@Descriptor("Email address as userid") String userId, @Descriptor("User firstname") String firstName,  @Descriptor("User password")  String password) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	svc.addUser(userId, firstName, password, new ArrayList<String>());
        return "User ("+userId+") added successfully!";
    }   
    
    @Descriptor("Send a welcome notify to user")
    public static String welcome(@Descriptor("Email address as userid") String userId, @Descriptor("User password")  String password, @Descriptor("Bcc list to notify")  List<String> bccList) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	svc.sendWelcome(userId, password, bccList);
        return "User ("+userId+") welcomed successfully!";
    } 
    
    @Descriptor("Show user")
    public static String whois(@Descriptor("Email address as userid") String userId) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	final User user = svc.getUserByEmail(userId);
    	if (user == null)
    		return "User ("+userId+") not found";
    	else
    		return String.format("User ("+user.getFirstName()+") found: {active:%b, mobileNumber:%s}",user.isActive(),user.getMobileNumber());
    } 
    
    @Descriptor("Activate user")
    public static String activate(@Descriptor("Email address as userid") String userId) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	User user = svc.getUserByEmail(userId);
    	if (user == null)
    		return "User ("+userId+") not found";
    		
       	user = svc.activate(user);
       	return "User ("+user.getFirstName()+") activated";
    } 
    
    @Descriptor("Deactivate user")
    public static String deactivate(@Descriptor("Email address as userid") String userId) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	User user = svc.getUserByEmail(userId);
    	if (user == null)
    		return "User ("+userId+") not found";
    		
       	user = svc.deactivate(user);
       	return "User ("+user.getFirstName()+") deactivated";
    } 
    
    @Descriptor("Show all active users")
    public static String showactive() throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	String res = svc.getAllActiveUsers().stream()
    			.map( Object::toString )
                .collect( Collectors.joining( ";" ) );
       	return "Active: Users: "+res;
    } 
    
    @Descriptor("Deletes user ")
    public static String del(@Descriptor("User emailAddress") String emailAddress) throws Exception {
    	IUserDAOService uSvc  = (IUserDAOService) createServiceFromServiceType(IUserDAOService.class);
		User user = uSvc.getUserByEmail(emailAddress);
		uSvc.delete(user);
        return "Deleted user successfully!";
    }
    
    @Descriptor("Mark for LRU next quiz")
    public static String lru(@Descriptor("Email address as userid") String userId) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	User user = svc.getUserByEmail(userId);
    	if (user == null)
    		return "User ("+userId+") not found";
    		
       	user.setMakeNextRunLeastRecentlyPractice(true);
       	svc.update(user);
       	return "MakeNextRunLeastRecentlyPractice ("+user.getFirstName()+") set";
    } 
    
    @Descriptor("Mark for Practice Test next quiz")
    public static String ptest(@Descriptor("Email address as userid") String userId) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	User user = svc.getUserByEmail(userId);
    	if (user == null)
    		return "User ("+userId+") not found";
    		
       	user.setMakeNextRunAPracticeTest(true);
       	svc.update(user);
       	return "MakeNextRunAPracticeTest ("+user.getFirstName()+") set";
    } 
    
    @Descriptor("Mark for SkippedOrIncorrect next quiz")
    public static String sori(@Descriptor("Email address as userid") String userId) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	User user = svc.getUserByEmail(userId);
    	if (user == null)
    		return "User ("+userId+") not found";
    		
       	user.setMakeNextRunPracticeSkippedOrIncorrect(true);
       	svc.update(user);
       	return "MakeNextRunPracticeSkippedOrIncorrect ("+user.getFirstName()+") set";
    } 
    
    @Descriptor("Text reminder")
    public static String remindViaText(@Descriptor("Email address as userid") String userId) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	User user = svc.getUserByEmail(userId);
    	if (user == null)
    		return "User ("+userId+") not found";
    	
    	IQuizTakerReminderService qsvc = (IQuizTakerReminderService)createServiceFromServiceType(IQuizTakerReminderService.class);
    	qsvc.sendReminder(user);
    		
       	return "Reminded ("+user.getFirstName()+") successfully";
    } 
    
    @Descriptor("List todays submissions")
    public static String submissions(@Descriptor("Email address as userid") String userId) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	User user = svc.getUserByEmail(userId);
    	if (user == null)
    		return "User ("+userId+") not found";
    	
    	IQuizSubmissionDAOService qsvc = (IQuizSubmissionDAOService)createServiceFromServiceType(IQuizSubmissionDAOService.class);
    	List<QuizSubmission> subs = qsvc.findQuizSubmissionsMadeToday(user);
    		
       	return String.format("Found %d submissions: %s",subs.size(),subs.toString());
    }
}
