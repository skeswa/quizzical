package org.quizzical.backend.notification.api.model;

import java.io.Serializable;
import java.util.Date;

import org.gauntlet.core.model.BaseEntity;
import org.quizzical.backend.notification.api.model.NOTIFICATIONSTATUS;

public abstract class SubscriberNotification extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1872317397144393015L;

	private long subscriberId;	
    
    private long contactGroupId;	   
    
    private NOTIFICATIONSTATUS status;
    
    private Date dateSent;	
    
    private Date dateFailed;    
    
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
	
	protected SubscriberNotification() {
	}

	protected SubscriberNotification(long subscriberId, NOTIFICATIONSTATUS status, Date dateSent,
			String body) {
		super();
		this.subscriberId = subscriberId;
		this.status = status;
		this.dateSent = dateSent;
		this.body = body;
	}     
}
