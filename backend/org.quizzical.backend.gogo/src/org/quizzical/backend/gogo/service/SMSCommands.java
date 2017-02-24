package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.sms.api.IAlertNotifier;
import org.quizzical.backend.sms.api.NotificationMessage;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.List;


public class SMSCommands {
    public final static String SCOPE = "sms";
    public final static String[] FUNCTIONS = new String[] { "test","text","textList"};

    @Descriptor("Tests SMS")
    public static String test(@Descriptor("Mobile #") String mobileNumber) throws Exception {
    	IAlertNotifier svc = (IAlertNotifier)createServiceFromServiceType(IAlertNotifier.class);
    	svc.testSMS(mobileNumber);
        return "Text sent successfully!";
    }  
    
    @Descriptor("Text mobile")
    public static String text(@Descriptor("Mobile #") String mobileNumber, @Descriptor("Recepient name") String name, @Descriptor("Message") String message, @Descriptor("Title") String title) throws Exception {
    	IAlertNotifier svc = (IAlertNotifier)createServiceFromServiceType(IAlertNotifier.class);
		String body = String.format(message, name);
		NotificationMessage sms = new NotificationMessage(mobileNumber, body, title);
    	svc.notifyViaSMS(sms);
        return "Text sent successfully!";
    }  
    
    @Descriptor("Text mobile")
    public static String textList(@Descriptor("Mobile #") List<String> mobileNumberList, @Descriptor("Recepient name") String name, @Descriptor("Message") String message, @Descriptor("Title") String title) throws Exception {
    	IAlertNotifier svc = (IAlertNotifier)createServiceFromServiceType(IAlertNotifier.class);
		String body = String.format(message, name);
		NotificationMessage sms = new NotificationMessage(mobileNumberList, body, title);
    	svc.notifyViaSMS(sms);
        return "Text sent successfully!";
    } 
}
