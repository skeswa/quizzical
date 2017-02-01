package org.quizzical.backend.sms.api;

public class AlertNotificationException extends Exception {

	public AlertNotificationException() {
		super();
	}

	public AlertNotificationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AlertNotificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlertNotificationException(String message) {
		super(message);
	}

	public AlertNotificationException(Throwable cause) {
		super(cause);
	}
	
}
