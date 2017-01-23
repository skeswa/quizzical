package org.gauntlet.quizzes.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.quizzes.api.model.QuizSubmission;

public interface IQuizScoringService {
	public QuizSubmission score(QuizSubmission submission) throws ApplicationException, NoSuchModelException;
}
