package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.List;


public class UserCommands {
    public final static String SCOPE = "usr";
    public final static String[] FUNCTIONS = new String[] { "add", "welcome", "whois", "deactivate", "activate"};

    
    @Descriptor("Creates a new user")
    public static String add(@Descriptor("Email address as userid") String userId, @Descriptor("User firstname") String firstName,  @Descriptor("User password")  String password, @Descriptor("Bcc list to notify")  List<String> bccList) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	svc.addUser(userId, firstName, password, bccList);
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
    
    @Descriptor("Deactive user")
    public static String deactivate(@Descriptor("Email address as userid") String userId) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	User user = svc.getUserByEmail(userId);
    	if (user == null)
    		return "User ("+userId+") not found";
    		
       	user = svc.activate(user);
       	return "User ("+user.getFirstName()+") deactivated";
    } 
}
