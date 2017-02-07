package org.quizzical.backend.notification.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class JPASubscriberSMSNotification extends JPASubscriberNotification {
	private static final long serialVersionUID = -3356293992923859911L;

	@Column(length=1024)
    private String sentToSMSList;
	
	public JPASubscriberSMSNotification() {
	}

	public String getSentToSMSList() {
		return sentToSMSList;
	}

	public void setSentToSMSList(String sentToSMSList) {
		this.sentToSMSList = sentToSMSList;
	}
	
}
