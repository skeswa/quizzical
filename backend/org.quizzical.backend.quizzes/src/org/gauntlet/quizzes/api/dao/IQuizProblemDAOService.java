package org.gauntlet.quizzes.api.dao;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.quizzes.api.model.QuizProblem;

public interface IQuizProblemDAOService extends IBaseService {
	public QuizProblem getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;
}
