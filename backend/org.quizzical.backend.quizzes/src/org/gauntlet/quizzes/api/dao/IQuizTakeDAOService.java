package org.gauntlet.quizzes.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizProblemAnswer;
import org.gauntlet.quizzes.api.model.QuizTake;

public interface IQuizTakeDAOService extends IBaseService {
	// QuizTakes
	public List<QuizTake> findAll(int start, int end) throws ApplicationException;
	
	public long countAll() throws ApplicationException;
	
	public QuizTake add(QuizTake record) throws ApplicationException;
	
	public QuizTake update(QuizTake record) throws ApplicationException;
	
	public QuizTake delete(Long id) throws ApplicationException, NoSuchModelException;
	
	public QuizTake getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public QuizTake getByCode(String code) throws ApplicationException;

	public QuizTake getByName(String name) throws ApplicationException;

	
	//
	public QuizTake addAnswer(Long quizTakeId, QuizProblemAnswer answer) throws ApplicationException, NoSuchModelException;

	public QuizTake addAnswers(Long quizTakeId, List<QuizProblemAnswer> answer) throws ApplicationException, NoSuchModelException;	
}
