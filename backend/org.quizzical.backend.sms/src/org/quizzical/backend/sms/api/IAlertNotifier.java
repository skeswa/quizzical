package org.quizzical.backend.sms.api;

public interface IAlertNotifier {
	public void notifyViaSMS(NotificationMessage message) throws AlertNotificationException;
	public void notifyViaEmail(NotificationMessage message) throws AlertNotificationException;
	public void testSMS(final String mobileNumber) throws AlertNotificationException;
}
