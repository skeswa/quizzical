package org.gauntlet.quizzes.generator.api;

import org.gauntlet.core.api.ApplicationException;

public class UserHasNotTakenDiagnosticTestException extends ApplicationException {

	public UserHasNotTakenDiagnosticTestException() {
		super();
	}

	public UserHasNotTakenDiagnosticTestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserHasNotTakenDiagnosticTestException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserHasNotTakenDiagnosticTestException(String message) {
		super(message);
	}

	public UserHasNotTakenDiagnosticTestException(Throwable cause) {
		super(cause);
	}

}
