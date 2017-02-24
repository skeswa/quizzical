package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.quizzical.backend.scheduler.api.IQuizTakerReminderService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class NotifyCommands {
    public final static String SCOPE = "ntfy";
    public final static String[] FUNCTIONS = new String[] { "dailyremind", "dailyreminds"};

    
    @Descriptor("Sends dailiy reminder Text")
    public static String dailyremind(@Descriptor("Email address as userid") String userId) throws Exception {
    	IUserDAOService svc = (IUserDAOService)createServiceFromServiceType(IUserDAOService.class);
    	final User user = svc.getUserByEmail(userId);
    	IQuizTakerReminderService rsvc = (IQuizTakerReminderService)createServiceFromServiceType(IQuizTakerReminderService.class);
    	rsvc.sendReminder(user);
        return "Text'ed User ("+userId+") successfully!";
    }   
    
    @Descriptor("Sends dailiy reminder Text to all active users")
    public static String dailyreminds() throws Exception {
    	IQuizTakerReminderService rsvc = (IQuizTakerReminderService)createServiceFromServiceType(IQuizTakerReminderService.class);
    	rsvc.sendReminders();
        return "Text'ed all users successfully!";
    } 
}
