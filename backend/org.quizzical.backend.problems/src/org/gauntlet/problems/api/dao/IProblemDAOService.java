package org.gauntlet.problems.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.gauntlet.problems.api.model.ProblemPicture;
import org.gauntlet.problems.api.model.ProblemSource;

public interface IProblemDAOService extends IBaseService {
	// Problems
	public List<Problem> findAll(int start, int end) throws ApplicationException;
	
	public long countAll() throws ApplicationException;
	
	public Problem provide(Problem record) throws ApplicationException;
	
	public Problem update(Problem record) throws ApplicationException;
	
	public Problem delete(Long id) throws ApplicationException, NoSuchModelException;
	
	public Problem getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public Problem getByCode(String code) throws ApplicationException;

	public Problem getByName(String name) throws ApplicationException;
	
	public Problem getBySourceAndPageNumberAndIndex(Long srcId, Integer pageNumber, Integer indexInPage) throws ApplicationException;
	
	public List<Problem> findByDifficulty(Long difficultyId, int start, int end) throws ApplicationException;
	
	public int countByDifficulty(Long difficultyId) throws ApplicationException;
	
	public List<Problem> findByCategory(Long categoryId, int start, int end) throws ApplicationException;
	
	public int countByCategory(Long categoryId) throws ApplicationException;	

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
	
}
