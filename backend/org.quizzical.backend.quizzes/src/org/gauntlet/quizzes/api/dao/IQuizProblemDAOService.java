package org.gauntlet.quizzes.api.dao;

import java.util.Collection;
import java.util.List;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.quizzical.backend.security.authorization.api.model.user.User;

public interface IQuizProblemDAOService extends IBaseService {
	public QuizProblem getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public List<Long> findUnpractisedProblemsByUserAndCategory(User user, String categoryCode) throws ApplicationException;
	
	public List<Long> findByCategoryNotIn(final User user, final String categoryCode, final Collection ids)  throws ApplicationException;
	
	List<Long> getAllUserProblemIds(User user) throws ApplicationException;

	public List<Long> findByDifficultyNotIn(final User user, final String difficultyCode, final Collection ids, final Integer limit)  
			throws ApplicationException;

	public List<Long> findUnpractisedProblemsByUserAndDifficulty(User user, String diffcultyCode, final Integer limit) throws ApplicationException;
}
