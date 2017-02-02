package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.sms.api.IAlertNotifier;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.List;


public class SMSCommands {
    public final static String SCOPE = "sms";
    public final static String[] FUNCTIONS = new String[] { "test"};

    @Descriptor("Tests SMS")
    public static String test(@Descriptor("Mobile #") String mobileNumber) throws Exception {
    	IAlertNotifier svc = (IAlertNotifier)createServiceFromServiceType(IAlertNotifier.class);
    	svc.testSMS(mobileNumber);
        return "Text sent successfully!";
    }  
}
