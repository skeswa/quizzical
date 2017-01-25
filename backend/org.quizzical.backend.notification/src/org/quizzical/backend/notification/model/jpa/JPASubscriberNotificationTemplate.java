package org.quizzical.backend.notification.model.jpa;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;
import org.quizzical.backend.notification.api.model.EXCEPTIONTYPE;

@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_PREFIX
+org.quizzical.backend.notification.api.Constants.NOTIFICATION_TABLE_QUALIFIER+Constants.Q7L_TABLE_NAME_PREFIX
+"msg_template")
public class JPASubscriberNotificationTemplate extends JPABaseEntity {
	private static final long serialVersionUID = 8748379950369527076L;

	@Enumerated(EnumType.STRING)
    private EXCEPTIONTYPE exceptionType;

    @Column(length=256)
    private String bodyVmTemplateTitle;    
    
    @Column(length=256)
    private String bodyVmTemplateSignature;     
    
    @Column(length=8192)
    private String bodyVmTemplate;
    

    private long contactGroupId;       


	public EXCEPTIONTYPE getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(EXCEPTIONTYPE exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getBodyVmTemplateTitle() {
		return bodyVmTemplateTitle;
	}

	public void setBodyVmTemplateTitle(String bodyVmTemplateTitle) {
		this.bodyVmTemplateTitle = bodyVmTemplateTitle;
	}

	public String getBodyVmTemplateSignature() {
		return bodyVmTemplateSignature;
	}

	public void setBodyVmTemplateSignature(String bodyVmTemplateSignature) {
		this.bodyVmTemplateSignature = bodyVmTemplateSignature;
	}

	public String getBodyVmTemplate() {
		return bodyVmTemplate;
	}

	public void setBodyVmTemplate(String bodyVmTemplate) {
		this.bodyVmTemplate = bodyVmTemplate;
	}

	public long getContactGroupId() {
		return contactGroupId;
	}

	public void setContactGroupId(long contactGroupId) {
		this.contactGroupId = contactGroupId;
	}
}
