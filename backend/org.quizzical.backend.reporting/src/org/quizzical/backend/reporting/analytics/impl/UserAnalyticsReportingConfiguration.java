package org.quizzical.backend.reporting.analytics.impl;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class UserAnalyticsReportingConfiguration implements ManagedService {
	public static final String Q7L_BACKEND_SERVER_HOSTNAME = "q7l.backend.server.hostname";
	public static final String Q7L_BACKEND_SERVER_HTTP_PROTOCOL = "q7l.backend.server.protocol";
	public static final String Q7L_BACKEND_SERVER_HTTP_PORT = "q7l.backend.server.port";
	public static final String Q7L_BACKEND_SERVER_HTTP_CONTEXT_PATH = "q7l.backend.server.contextpath";
	
	public static final String Q7L_FRONTEND_SERVER_HOSTNAME = "q7l.frontend.server.hostname";
	public static final String Q7L_FRONTEND_SERVER_HTTP_PROTOCOL = "q7l.frontend.server.protocol";
	public static final String Q7L_FRONTEND_SERVER_HTTP_PORT = "q7l.frontend.server.port";
	public static final String Q7L_FRONTEND_SERVER_HTTP_CONTEXT_PATH = "q7l.frontend.server.contextpath";	
	
	public static final String Q7L_REPORTING_EMAIL_RESULTS = "q7l.reporting.emailresults";
	
	private String backendHostname;
	private Integer backendPort;
	private String backendProtocol;
	private String backendContextpath;
	
	private String frontendHostname;
	private Integer frontendPort;
	private String frontendProtocol;
	private String frontendContextpath;	
	
	private Boolean handleEmailResultsEvent = false;
	
	@Override
	public void updated(Dictionary props) throws ConfigurationException {
		this.backendHostname = (String)props.get(Q7L_BACKEND_SERVER_HOSTNAME);
		
		if (props.get(Q7L_BACKEND_SERVER_HTTP_PORT) != null && props.get(Q7L_BACKEND_SERVER_HTTP_PORT).toString().length() > 0)
			this.backendPort = Integer.valueOf((String)props.get(Q7L_BACKEND_SERVER_HTTP_PORT));
		
		this.backendProtocol = (String)props.get(Q7L_BACKEND_SERVER_HTTP_PROTOCOL);
		this.backendContextpath = (String)props.get(Q7L_BACKEND_SERVER_HTTP_CONTEXT_PATH);
		
		this.frontendHostname = (String)props.get(Q7L_FRONTEND_SERVER_HOSTNAME);
		
		if (props.get(Q7L_FRONTEND_SERVER_HTTP_PORT) != null && props.get(Q7L_FRONTEND_SERVER_HTTP_PORT).toString().length() > 0)
			this.frontendPort = Integer.valueOf((String)props.get(Q7L_FRONTEND_SERVER_HTTP_PORT) );
		
		this.frontendProtocol = (String)props.get(Q7L_FRONTEND_SERVER_HTTP_PROTOCOL);
		this.frontendContextpath = (String)props.get(Q7L_FRONTEND_SERVER_HTTP_CONTEXT_PATH);
		
		if (props.get(Q7L_REPORTING_EMAIL_RESULTS) != null && props.get(Q7L_REPORTING_EMAIL_RESULTS).toString().length() > 0)
			this.handleEmailResultsEvent = props.get(Q7L_REPORTING_EMAIL_RESULTS).toString().equalsIgnoreCase("true") ? true : false;		
	}

	public String getBackendHostname() {
		return backendHostname;
	}
	public Integer getBackendPort() {
		return backendPort;
	}
	public String getBackendProtocol() {
		return backendProtocol;
	}
	public String getBackendContextpath() {
		return backendContextpath;
	}
	public String getFrontendHostname() {
		return frontendHostname;
	}
	public Integer getFrontendPort() {
		return frontendPort;
	}
	public String getFrontendProtocol() {
		return frontendProtocol;
	}
	public String getFrontendContextpath() {
		return frontendContextpath;
	}
	public Boolean getHandleEmailResultsEvent() {
		return handleEmailResultsEvent;
	}
	public void setHandleEmailResultsEvent(Boolean handleEmailResultsEvent) {
		this.handleEmailResultsEvent = handleEmailResultsEvent;
	}

	public String getBackendURLPrefix() {
		if (getBackendPort() != null)
			return String.format("%s://%s:%d", getBackendProtocol(), getBackendHostname(), getBackendPort());
		else
			return String.format("%s://%s", getBackendProtocol(), getBackendHostname());
	}
	
	public String getFrontendURLPrefix() {
		if (getFrontendPort() != null)
			return String.format("%s://%s:%d", getFrontendProtocol(), getFrontendHostname(), getFrontendPort());
		else
			return String.format("%s://%s", getFrontendProtocol(), getFrontendHostname());
	}
}
