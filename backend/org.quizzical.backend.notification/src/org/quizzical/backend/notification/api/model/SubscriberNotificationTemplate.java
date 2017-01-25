package org.quizzical.backend.notification.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;
import org.quizzical.backend.notification.api.model.EXCEPTIONTYPE;

public class SubscriberNotificationTemplate extends BaseEntity implements Serializable {
    private EXCEPTIONTYPE exceptionType;

    private String bodyVmTemplateTitle;    
    
    private String bodyVmTemplateSignature;     
    
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
