package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.quizzical.backend.reporting.api.IUserAnalyticsReporting;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.List;


public class UserAnalyticsReportingCommands {
    public final static String SCOPE = "rpt";
    public final static String[] FUNCTIONS = new String[] { "senddaily"};
    
    @Descriptor("Send daily progress report")
    public static String senddaily(@Descriptor("Email address as userid") String userId, @Descriptor("Bcc list to notify")  List<String> bccList) throws Exception {
    	CommandUserAnalyticsReporting cmd = new CommandUserAnalyticsReporting((IUserAnalyticsReporting)createServiceFromServiceType(IUserAnalyticsReporting.class));
    	IUserAnalyticsReporting svc = cmd.get();
    	svc.emailDailyReport(userId, bccList);
        return "Daily Progress Report ("+userId+") sent successfully!";
    } 
}
