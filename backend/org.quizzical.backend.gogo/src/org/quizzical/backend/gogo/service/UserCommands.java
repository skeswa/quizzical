package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.model.user.User;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.List;


public class UserCommands {
    public final static String SCOPE = "usr";
    public final static String[] FUNCTIONS = new String[] { "add", "welcome", "whois"};

    
    @Descriptor("Creates a new user")
    public static String add(@Descriptor("Email address as userid") String userId, @Descriptor("User firstname") String firstName,  @Descriptor("User password")  String password, @Descriptor("Bcc list to notify")  List<String> bccList) throws Exception {
    	CommandUsers cmd = new CommandUsers((IUserDAOService)createServiceFromServiceType(IUserDAOService.class));
    	IUserDAOService svc = cmd.get();
    	svc.addUser(userId, firstName, password, bccList);
        return "User ("+userId+") added successfully!";
    }   
    
    @Descriptor("Send a welcome notify to user")
    public static String welcome(@Descriptor("Email address as userid") String userId, @Descriptor("User password")  String password, @Descriptor("Bcc list to notify")  List<String> bccList) throws Exception {
    	CommandUsers cmd = new CommandUsers((IUserDAOService)createServiceFromServiceType(IUserDAOService.class));
    	IUserDAOService svc = cmd.get();
    	svc.sendWelcome(userId, password, bccList);
        return "User ("+userId+") welcomed successfully!";
    } 
    
    @Descriptor("Show user")
    public static String whois(@Descriptor("Email address as userid") String userId) throws Exception {
    	CommandUsers cmd = new CommandUsers((IUserDAOService)createServiceFromServiceType(IUserDAOService.class));
    	IUserDAOService svc = cmd.get();
    	final User user = svc.getUserByEmail(userId);
    	if (user == null)
    		return "User ("+userId+") not found";
    	else
    		return "User ("+user.getFirstName()+") found";
    } 
}
