package org.gauntlet.core.api.dao;

public class NoSuchModelException extends Exception {
	public NoSuchModelException() {
		super();
	}

	public NoSuchModelException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoSuchModelException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchModelException(String message) {
		super(message);
	}

	public NoSuchModelException(Throwable cause) {
		super(cause);
	}
}
