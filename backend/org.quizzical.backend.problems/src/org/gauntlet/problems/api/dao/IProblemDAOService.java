package org.gauntlet.problems.api.dao;

import java.util.Collection;
import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemCategoryLesson;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.gauntlet.problems.api.model.ProblemPicture;
import org.gauntlet.problems.api.model.ProblemSource;
import org.gauntlet.problems.model.jpa.JPAProblemPicture;
import org.quizzical.backend.security.authorization.api.model.user.User;

public interface IProblemDAOService extends IBaseService {
	// Problems
	public List<Problem> findAll(int start, int end) throws ApplicationException;
	
	List<Problem> findAll(List<Long> ids) throws ApplicationException;
	
	public long countAll() throws ApplicationException;
	
	public Problem provide(Problem record) throws ApplicationException, NoSuchModelException;
	
	public Problem update(Problem record) throws ApplicationException, NoSuchModelException;
	
	public Problem delete(Long id) throws ApplicationException, NoSuchModelException;
	
	public void deleteAllBySourceId(Long sourceId) throws ApplicationException;
	
	public Problem getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public Problem getByCode(String code) throws ApplicationException;

	public Problem getByName(String name) throws ApplicationException;
	
	public Problem getBySourceAndPageNumberAndIndexAndCalcType(Long srcId, Integer pageNumber, Integer indexInPage, Boolean requiresCalculator) throws ApplicationException;
	
	public List<Problem> findByDifficulty(Long difficultyId, int start, int end) throws ApplicationException;
	
	public int countByDifficulty(Long difficultyId) throws ApplicationException;
	
	List<Problem> findAllByCategory(Long sourceId) throws ApplicationException;
	
	public List<Problem> findByCategory(Long categoryId, int start, int end) throws ApplicationException;
	
	List<Problem> findAllBySource(Long sourceId) throws ApplicationException;
	
	public int countByCategory(Long categoryId) throws ApplicationException;	
	
	public List<Problem> findByDifficultyAndCategoryNotInIn(final Boolean requiresCalc, final Long difficultyId, final Long categoryId, final Collection ids, final Integer randomOffset, final Integer limit)  throws ApplicationException;

	public long countByCalcAndDifficultyAndCategoryNotInIn(final Boolean requiresCalc, final Long difficultyId, final Long categoryId, final List<Long> ids)  throws ApplicationException;

	public List<Problem> findByCategoryNotInIn(final Long categoryId, final Collection ids, final Integer offset, final Integer limit)  
			throws ApplicationException;
	
	List<Problem> getAllUserQuizzedProblems(User user, List<Long> usedInQuizProblemIds, Integer limit)
			throws ApplicationException;

	List<Problem> getAllUserNonQuizzedProblems(User user, List<Long> usedInQuizProblemIds, Integer limit)
			throws ApplicationException;
	
	public List<Problem> getAllNonQAedProblems( Integer limit )
			throws ApplicationException;
	
	public void markAsQAed(Problem problem) throws ApplicationException, NoSuchModelException;
	
	//ProblemDifficulty
	public List<ProblemDifficulty> findAllProblemDifficulties(int start, int end) throws ApplicationException;
	
	public long countAllProblemDifficulties() throws ApplicationException;
	
	public ProblemDifficulty getProblemDifficultyByPrimary(Long pk)  throws ApplicationException, NoSuchModelException;
	
	public ProblemDifficulty provideProblemDifficulty(ProblemDifficulty record) throws ApplicationException;
	
	public ProblemDifficulty provideProblemDifficulty(String name) throws ApplicationException;
	
	public ProblemDifficulty getProblemDifficultyByCode(String code) throws ApplicationException;
	
	public ProblemDifficulty getProblemDifficultyByName(String name) throws ApplicationException;
	
	public ProblemDifficulty deleteProblemDifficulty(Long id) throws ApplicationException, NoSuchModelException;
	
	//ProblemCategory
	public List<ProblemCategory> findAllProblemCategories(int start, int end) throws ApplicationException;
	
	public long countAllProblemCategories() throws ApplicationException;
	
	public ProblemCategory getProblemCategoryByPrimary(Long pk)  throws ApplicationException, NoSuchModelException;
	
	public ProblemCategory provideProblemCategory(ProblemCategory record) throws ApplicationException;
	
	public ProblemCategory provideProblemCategory(String name) throws ApplicationException;
	
	public ProblemCategory getProblemCategoryByCode(String code) throws ApplicationException;
	
	public ProblemCategory getProblemCategoryByName(String name) throws ApplicationException;
	
	public ProblemCategory deleteProblemCategory(Long id) throws ApplicationException, NoSuchModelException;
	
	public ProblemCategoryLesson addProblemCategoryLesson(Long categoryId, Long lessonId) throws ApplicationException, NoSuchModelException;
	
	public void removeProblemCategoryLesson(Long categoryLessonId) throws ApplicationException, NoSuchModelException;
	
	//ProblemSource
	public List<ProblemSource> findAllProblemSources(int start, int end) throws ApplicationException;
	
	public long countAllProblemSources() throws ApplicationException;
	
	public ProblemSource getProblemSourceByPrimary(Long pk)  throws ApplicationException, NoSuchModelException;
	
	public ProblemSource provideProblemSource(ProblemSource record) throws ApplicationException;
	
	public ProblemSource provideProblemSource(String name) throws ApplicationException;
	
	public ProblemSource getProblemSourceByCode(String code) throws ApplicationException;
	
	public ProblemSource getProblemSourceByName(String name) throws ApplicationException;
	
	public ProblemSource deleteProblemSource(Long id) throws ApplicationException, NoSuchModelException;	
	
	
	//Problem Picture
	public ProblemPicture getProblemPictureByPrimary(Long pk)  throws ApplicationException, NoSuchModelException;

	ProblemPicture updateProblemPicture(ProblemPicture record) throws ApplicationException;
}
