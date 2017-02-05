package org.gauntlet.quizzes.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.quizzical.backend.security.authorization.api.model.user.User;

public interface IQuizProblemDAOService extends IBaseService {
	public QuizProblem getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	List<Long> getAllUserProblemIds(User user) throws ApplicationException;
}
