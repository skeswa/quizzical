package org.quizzical.backend.security.login.rest;

import org.gauntlet.core.api.ApplicationException;

public class UnauthorizedAccessException extends ApplicationException {
	private static final long serialVersionUID = -8718126518642928060L;

	public UnauthorizedAccessException() {}

	public UnauthorizedAccessException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnauthorizedAccessException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedAccessException(final String message) {
		super(message);
	}

	public UnauthorizedAccessException(final Throwable cause) {
		super(cause);
	}

}
