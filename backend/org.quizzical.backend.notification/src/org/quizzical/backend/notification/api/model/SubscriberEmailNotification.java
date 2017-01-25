package org.quizzical.backend.notification.api.model;

import java.io.Serializable;

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
}
