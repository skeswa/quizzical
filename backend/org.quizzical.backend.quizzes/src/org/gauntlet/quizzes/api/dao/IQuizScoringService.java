package org.gauntlet.quizzes.api.dao;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.quizzical.backend.security.authorization.api.model.user.User;

public interface IQuizScoringService {
	public QuizSubmission score(User user, QuizSubmission submission, boolean ensureBaline) throws ApplicationException, NoSuchModelException;
}
