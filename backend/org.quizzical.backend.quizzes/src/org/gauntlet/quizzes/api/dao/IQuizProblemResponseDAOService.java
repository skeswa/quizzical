package org.gauntlet.quizzes.api.dao;

import java.util.List;
import java.util.Set;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;

public interface IQuizProblemResponseDAOService extends IBaseService {
	public QuizProblemResponse add(QuizProblemResponse record) throws ApplicationException;
	
	public QuizProblemResponse update(QuizProblemResponse record) throws ApplicationException;
	
	public QuizProblemResponse delete(Long id) throws ApplicationException, NoSuchModelException;
	
	public List<QuizProblemResponse> findAll() throws ApplicationException;
}
