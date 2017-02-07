package org.quizzical.backend.notification.api.model;

import java.io.Serializable;
import java.util.Date;

public class SubscriberEmailNotification extends SubscriberNotification implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2184346172933386151L;

	private String sentToEmailList;
    
    private String subject;

	public String getSentToEmailList() {
		return sentToEmailList;
	}

	public void setSentToEmailList(String sentToEmailList) {
		this.sentToEmailList = sentToEmailList;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public SubscriberEmailNotification() {
	}

	public SubscriberEmailNotification(String sentToEmailList, 
			String subject,
			long subscriberId, 
			NOTIFICATIONSTATUS status, 
			Date dateSent,
			String body) {
		super(subscriberId, status, dateSent, body);
	} 
}
