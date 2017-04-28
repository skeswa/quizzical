package org.quizzical.backend.reporting.api;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.gauntlet.core.api.ApplicationException;

public interface IQAReporting {
	public static final String EVENT_TOPIC_REPORT_FAULTY_PROBLEMS = "org/quizzical/backend/reporting/REPORT_FAULTY_PROBLEMS";
	public static final String EVENT_TOPIC_PROP_QPIDS = "qpids";
	
	public void reportFaultyProblems(List<Long> problemIds) throws ApplicationException, EmailException;
}
