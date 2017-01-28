package org.quizzical.backend.reporting.analytics.impl;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class UserAnalyticsReportingConfiguration implements ManagedService {
	public static final String Q7L_APP_SERVER_HOSTNAME = "q7l.app.server.hostname";
	public static final String Q7L_APP_SERVER_HTTP_PROTOCOL = "q7l.app.server.protocol";
	public static final String Q7L_APP_SERVER_HTTP_PORT = "q7l.app.server.port";
	public static final String Q7L_APP_SERVER_HTTP_CONTEXT_PATH = "q7l.app.server.contextpath";
	
	private String hostname;
	private Integer port;
	private String protocol;
	private String contextpath;
	
	@Override
	public void updated(Dictionary props) throws ConfigurationException {
		this.hostname = (String)props.get(Q7L_APP_SERVER_HOSTNAME);
		this.port = Integer.valueOf((String)props.get(Q7L_APP_SERVER_HTTP_PORT));
		this.protocol = (String)props.get(Q7L_APP_SERVER_HTTP_PROTOCOL);
		this.contextpath = (String)props.get(Q7L_APP_SERVER_HTTP_CONTEXT_PATH);
	}

	public String getHostname() {
		return hostname;
	}

	public Integer getPort() {
		return port;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getContextpath() {
		return contextpath;
	}	
}
