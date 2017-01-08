package org.gauntlet.quizzes.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.quizzes.api.model.QuizSubmission;

public interface IQuizTakeDAOService extends IBaseService {
	// QuizTakes
	public List<QuizSubmission> findAll(int start, int end) throws ApplicationException;
	
	public long countAll() throws ApplicationException;
	
	public QuizSubmission add(QuizSubmission record) throws ApplicationException;
	
	public QuizSubmission update(QuizSubmission record) throws ApplicationException;
	
	public QuizSubmission delete(Long id) throws ApplicationException, NoSuchModelException;
	
	public QuizSubmission getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public QuizSubmission getByCode(String code) throws ApplicationException;

	public QuizSubmission getByName(String name) throws ApplicationException;	
}
