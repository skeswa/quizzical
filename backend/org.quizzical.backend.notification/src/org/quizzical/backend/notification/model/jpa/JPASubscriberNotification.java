package org.quizzical.backend.notification.model.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;
import org.quizzical.backend.notification.api.model.NOTIFICATIONSTATUS;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_PREFIX
    +org.quizzical.backend.notification.api.Constants.NOTIFICATION_TABLE_QUALIFIER+Constants.Q7L_TABLE_NAME_PREFIX
	+"event")
public abstract class JPASubscriberNotification extends JPABaseEntity {
    private long subscriberId;	
    
    private long contactGroupId;	   
    
    @Enumerated(EnumType.STRING)
    private NOTIFICATIONSTATUS status;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSent;	
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFailed;    
    
    @Column(length=8192)
    private String body;

	public long getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(long subscriberId) {
		this.subscriberId = subscriberId;
	}

	public long getContactGroupId() {
		return contactGroupId;
	}

	public void setContactGroupId(long contactGroupId) {
		this.contactGroupId = contactGroupId;
	}

	public NOTIFICATIONSTATUS getStatus() {
		return status;
	}

	public void setStatus(NOTIFICATIONSTATUS status) {
		this.status = status;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public Date getDateFailed() {
		return dateFailed;
	}

	public void setDateFailed(Date dateFailed) {
		this.dateFailed = dateFailed;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}        
}
