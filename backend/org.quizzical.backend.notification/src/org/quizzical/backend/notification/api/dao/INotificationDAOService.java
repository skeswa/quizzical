package org.quizzical.backend.notification.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.quizzical.backend.notification.api.model.SubscriberEmailNotification;
import org.quizzical.backend.notification.api.model.SubscriberSMSNotification;

public interface INotificationDAOService {
	//Events
	public static final String EVENT_TOPIC_SAVE_NOTIFICATION = "org/quizzical/backend/notification/SAVE_NOTIFICATION";
	public static final String EVENT_TOPIC_PROP_USERID = "userid";
	public static final String EVENT_TOPIC_PROP_NOTIFICATION_TYPE = "type";
	public static final String EVENT_TOPIC_PROP_NOTIFICATION_TYPE_EMAIL = "email";
	public static final String EVENT_TOPIC_PROP_NOTIFICATION_TYPE_SMS = "sms";
	
	
	// SubscriberEmailNotification
	List<SubscriberEmailNotification> findAllSubscriberEmailNotification(int start, int end) throws ApplicationException;

	long countAllSubscriberEmailNotification() throws ApplicationException;

	SubscriberEmailNotification getSubscriberEmailNotificationByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	SubscriberEmailNotification provideSubscriberEmailNotification(SubscriberEmailNotification record) throws ApplicationException;

	SubscriberEmailNotification updateSubscriberEmailNotification(SubscriberEmailNotification record) throws ApplicationException;
	
	SubscriberEmailNotification deleteSubscriberEmailNotification(Long id) throws ApplicationException, NoSuchModelException;

	SubscriberEmailNotification getSubscriberEmailNotificationByCode(String code) throws ApplicationException;
	
	// SubscriberSMSNotification
	List<SubscriberSMSNotification> findAllSubscriberSMSNotification(int start, int end) throws ApplicationException;

	long countAllSubscriberSMSNotification() throws ApplicationException;

	SubscriberSMSNotification getSubscriberSMSNotificationByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	SubscriberSMSNotification provideSubscriberSMSNotification(SubscriberSMSNotification record) throws ApplicationException;

	SubscriberSMSNotification updateSubscriberSMSNotification(SubscriberSMSNotification record) throws ApplicationException;

	SubscriberSMSNotification deleteSubscriberSMSNotification(Long id) throws ApplicationException, NoSuchModelException;

	SubscriberSMSNotification getSubscriberSMSNotificationByCode(String code) throws ApplicationException;


	void truncate() throws ApplicationException;
}
