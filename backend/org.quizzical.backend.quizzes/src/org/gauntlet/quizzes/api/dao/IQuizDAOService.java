package org.gauntlet.quizzes.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizType;
import org.quizzical.backend.security.authorization.api.model.user.User;

public interface IQuizDAOService extends IBaseService {
	// Quizzes
	public List<Quiz> findAll(int start, int end) throws ApplicationException;
	
	public List<Quiz> findAll(User user, int start, int end) throws ApplicationException;	
	
	public List<Quiz> findByUser(User user) throws ApplicationException;
	
	public long countAll() throws ApplicationException;
	
	public long countAll(User user) throws ApplicationException;
	
	public Quiz provide(User user, Quiz record) throws ApplicationException;
	
	public Quiz update(Quiz record) throws ApplicationException;
	
	public Quiz delete(Long id) throws ApplicationException, NoSuchModelException;
	
	public Quiz getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public Quiz getByCode(String code) throws ApplicationException;

	public Quiz getByName(String name) throws ApplicationException;
	
	public List<Quiz> findByQuizType(User user, Long difficultyId, int start, int end) throws ApplicationException;
	
	public int countByQuizType(Long difficultyId) throws ApplicationException;
	
	public int countByQuizTypeCode(User user, String typeCode) throws ApplicationException;
	
	public List<Quiz> findQuizzesTakenToday(User user) throws ApplicationException;

	public Quiz forceDelete(Long id) throws ApplicationException, NoSuchModelException;

	//QuizType
	public List<QuizType> findAllQuizTypes(int start, int end) throws ApplicationException;
	
	public long countAllQuizTypes() throws ApplicationException;
	
	public QuizType getQuizTypeByPrimary(Long pk)  throws ApplicationException, NoSuchModelException;
	
	public QuizType provideQuizType(QuizType record) throws ApplicationException;
	
	public QuizType getQuizTypeByCode(String code) throws ApplicationException;
	
	public QuizType getQuizTypeByName(String name) throws ApplicationException;
	
	public QuizType deleteQuizType(Long id) throws ApplicationException, NoSuchModelException;
	
	//Misc
	public void truncate() throws ApplicationException;

	public boolean userHasTakenDiagnoticTest(User user) throws ApplicationException;


}
