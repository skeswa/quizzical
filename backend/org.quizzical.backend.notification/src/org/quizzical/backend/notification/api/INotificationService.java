package org.quizzical.backend.notification.api;

import org.gauntlet.core.api.ApplicationException;
import org.quizzical.backend.notification.api.model.SubscriberEmailNotification;
import org.quizzical.backend.notification.api.model.SubscriberSMSNotification;

public interface INotificationService {
	//Events
	public static final String EVENT_TOPIC_SAVE_NOTIFICATION = "org/quizzical/backend/notification/SAVE_NOTIFICATION";
	public static final String EVENT_TOPIC_PROP_USERID = "userid";
	public static final String EVENT_TOPIC_PROP_NOTIFICATION_BODY       = "messagebody";
	public static final String EVENT_TOPIC_PROP_NOTIFICATION_SUBJECT    = "subject";
	public static final String EVENT_TOPIC_PROP_NOTIFICATION_EMAIL_CC   = "cc";
	public static final String EVENT_TOPIC_PROP_NOTIFICATION_SMS_CC     = "cc";
	public static final String EVENT_TOPIC_PROP_NOTIFICATION_TYPE       = "type";
	public static final String EVENT_TOPIC_PROP_NOTIFICATION_TYPE_EMAIL = "email";
	public static final String EVENT_TOPIC_PROP_NOTIFICATION_TYPE_SMS   = "sms";
	
	
	void notify(SubscriberEmailNotification email) throws ApplicationException;
	void notify(SubscriberSMSNotification sms) throws ApplicationException;
}
