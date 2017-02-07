package org.quizzical.backend.notification.api.model;

import java.io.Serializable;
import java.util.Date;

public class SubscriberSMSNotification extends SubscriberNotification implements Serializable {
	private static final long serialVersionUID = -6323327799378160406L;
	
	private String sentToMobileNumberList;
	
	private String subject;
    
	public SubscriberSMSNotification() {
	}

	public SubscriberSMSNotification(
			String subject,
			String sentToMobileNumberList, 
			long subscriberId, 
			NOTIFICATIONSTATUS status, 
			Date dateSent,
			String body) {
		super(subscriberId, status, dateSent, body);
	}

	public String getSentToMobileNumberList() {
		return sentToMobileNumberList;
	}

	public void setSentToMobileNumberList(String sentToMobileNumberList) {
		this.sentToMobileNumberList = sentToMobileNumberList;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	} 
}
