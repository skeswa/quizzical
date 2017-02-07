package org.quizzical.backend.notification.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class JPASubscriberEmailNotification extends JPASubscriberNotification {
	private static final long serialVersionUID = -8365667417766713633L;

	@Column(length=1024)
    private String sentToEmailList;
    
    @Column(length=256)
    private String subject;
    
    public JPASubscriberEmailNotification() {
    }

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
