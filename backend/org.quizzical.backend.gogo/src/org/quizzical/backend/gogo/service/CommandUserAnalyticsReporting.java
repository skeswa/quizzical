package org.quizzical.backend.gogo.service;

import org.quizzical.backend.reporting.api.IUserAnalyticsReporting;

public class CommandUserAnalyticsReporting {

    private final IUserAnalyticsReporting userAnalyticsReportingService;

    public CommandUserAnalyticsReporting(IUserAnalyticsReporting userAnalyticsReportingService) {
        this.userAnalyticsReportingService = userAnalyticsReportingService;
    }

    public IUserAnalyticsReporting get() {
    	return userAnalyticsReportingService;
    }
}
