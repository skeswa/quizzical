package org.gauntlet.lessons.service.impl;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.lessons.api.model.UserLesson;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class LessonEventHandlerImpl implements EventHandler {
	
	private volatile ILessonsDAOService lessonService;
	private volatile IQuizSubmissionDAOService quizSubmissionService;
	
	@Override
	public void handleEvent(Event event) {
		Long qId = (Long) event.getProperty("quizId");
		Long qsId = (Long) event.getProperty("quizSubmissionId");
		
		//- Mark UserLesson as finished
		try {
			UserLesson ul = lessonService.findUserLessonByQuizId(qId);
			QuizSubmission qs = quizSubmissionService.getByPrimary(qsId);
			lessonService.markUserLessonAsComplete(ul,qs);
			lessonService.pickNextUserLesson(ul.getUserId());
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
