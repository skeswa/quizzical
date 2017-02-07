package org.quizzical.backend.notification.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gauntlet.core.api.ApplicationException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.quizzical.backend.mail.api.IMailService;
import org.quizzical.backend.notification.api.INotificationService;
import org.quizzical.backend.notification.api.dao.INotificationDAOService;
import org.quizzical.backend.notification.api.model.SubscriberEmailNotification;
import org.quizzical.backend.notification.api.model.SubscriberSMSNotification;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.sms.api.AlertNotificationException;
import org.quizzical.backend.sms.api.IAlertNotifier;
import org.quizzical.backend.sms.api.NotificationMessage;


@SuppressWarnings("restriction")
public class NotificationServiceImpl implements INotificationService {
	private volatile LogService logger;
	
	private volatile BundleContext ctx;
	
	private volatile IAlertNotifier smsService;
	
	private volatile IUserDAOService userService;
	
	private volatile IMailService mailService;
	
	private volatile INotificationDAOService notificationDAOService;

	@Override
	public void notify(SubscriberEmailNotification email) throws ApplicationException {
		List<String> toList = Arrays.asList(email.getSentToEmailList().split(","));
		NotificationMessage message = new NotificationMessage(toList, email.getBody(), email.getSubject());
		try {
			notificationDAOService.provideSubscriberEmailNotification(email);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void notify(SubscriberSMSNotification sms) throws ApplicationException {
		List<String> toList = Arrays.asList(sms.getSentToMobileNumberList().split(","));
		NotificationMessage message = new NotificationMessage(toList, sms.getBody(), sms.getSubject());
		try {
			notificationDAOService.provideSubscriberSMSNotification(sms);
			smsService.notifyViaSMS(message);
		} catch (AlertNotificationException e) {
			throw new ApplicationException(e);
		}
	}
	
}