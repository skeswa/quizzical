package org.quizzical.backend.reporting.admin.impl;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class AdminReportingConfiguration implements ManagedService {
	public static final String Q7L_QA_EMAIL = "q7l.qa.email";
	
	private String emailAddress;
	
	@Override
	public void updated(Dictionary props) throws ConfigurationException {
		this.emailAddress = (String)props.get(Q7L_QA_EMAIL);
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
