package org.gauntlet.problems.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.amdatu.jta.Transactional;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.commons.util.Validator;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.gauntlet.core.model.JPABaseEntity;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.osgi.service.log.LogService;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.gauntlet.problems.api.model.ProblemPicture;
import org.gauntlet.problems.api.model.ProblemSource;
import org.gauntlet.problems.model.jpa.JPAProblem;
import org.gauntlet.problems.model.jpa.JPAProblemCategory;
import org.gauntlet.problems.model.jpa.JPAProblemDifficulty;
import org.gauntlet.problems.model.jpa.JPAProblemPicture;
import org.gauntlet.problems.model.jpa.JPAProblemSource;


@SuppressWarnings("restriction")
@Transactional
public class ProblemDAOImpl extends BaseServiceImpl implements IProblemDAOService {
	//Problems
	private volatile LogService logger;
	
	private volatile EntityManager em;
	
	@Override
	public LogService getLogger() {
		return logger;
	}

	public void setLogger(LogService logger) {
		this.logger = logger;
	}

	@Override
	public EntityManager getEm() {
		return em;
	}	
	
	@Override
	public List<Problem> findAll(int start, int end) throws ApplicationException {
		List<Problem> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPAProblem.class,start,end);
			result = JPAEntityUtil.copy(resultList, Problem.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAll() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPAProblem.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public Problem getByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAProblem jpaEntity = (JPAProblem) super.findByPrimaryKey(JPAProblem.class, pk);
		return JPAEntityUtil.copy(jpaEntity, Problem.class);
	}

	@Override
	public Problem provide(Problem record)
			  throws ApplicationException {
		Problem existingProblem = getByCode(record.getCode());
		if (Validator.isNull(existingProblem))
		{
			System.out.println(String.format("New problem %s_%d_%d",record.getSource(),record.getSourcePageNumber(),record.getSourceIndexWithinPage()));
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblem.class));
			existingProblem = JPAEntityUtil.copy(res, Problem.class);
		}
		else {
			System.out.println(String.format("Updating problem %s_%d_%d",record.getSource(),record.getSourcePageNumber(),record.getSourceIndexWithinPage()));
			return update(record);
		}

		return existingProblem;
	}
	
	@Override
	public Problem update(Problem record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAProblem.class));
		Problem dto = JPAEntityUtil.copy(res, Problem.class);
		return dto;	
	}	
	
	@Override
	public Problem delete(Long id) throws ApplicationException, NoSuchModelException {
		JPAProblem jpaEntity = (JPAProblem) super.findByPrimaryKey(JPAProblem.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, Problem.class);
	}
	
	@Override
	public Problem getByCode(String code) throws ApplicationException {
		JPAProblem jpaEntity = (JPAProblem) super.findWithAttribute(JPAProblem.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, Problem.class);
	}


	@Override
	public Problem getByName(String name) throws ApplicationException {
		JPAProblem jpaEntity = (JPAProblem) super.findWithAttribute(JPAProblem.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, Problem.class);
	}
	
	@Override
	public Problem getBySourceAndPageNumberAndIndexAndCalcType(Long srcId, Integer pageNumber, Integer indexInPage, Boolean requiresCalculator) throws ApplicationException {
		Problem result = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> query = builder.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = query.from(JPAProblem.class);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			
			ParameterExpression<Long> pSrc = builder.parameter(Long.class);
			pes.put(pSrc, srcId);
			
			ParameterExpression<Integer> pPage = builder.parameter(Integer.class);
			pes.put(pPage, pageNumber);
			
			ParameterExpression<Integer> pIndexInPage = builder.parameter(Integer.class);
			pes.put(pIndexInPage, indexInPage);
			
			query.select(rootEntity).where(builder.and(
					builder.equal(rootEntity.get("source").get("id"),pSrc),
					builder.equal(rootEntity.get("sourcePageNumber"),pageNumber),
					builder.equal(rootEntity.get("sourceIndexWithinPage"),indexInPage),
					builder.equal(rootEntity.get("requiresCalculator"),requiresCalculator)
					));
			query.select(rootEntity);
			
			
			List resultList = findWithDynamicQueryAndParams(query,pes,0,1);
			JPAProblem jpaEntity = (!resultList.isEmpty())?(JPAProblem)resultList.get(0):null;
			result = JPAEntityUtil.copy(jpaEntity, Problem.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override 
	public List<Problem> findByDifficulty(Long difficultyId, int start, int end) throws ApplicationException {
		List<Problem> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> query = builder.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = query.from(JPAProblem.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.gt(rootEntity.get("difficulty").get("id"),p));
			query.select(rootEntity);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			pes.put(p, difficultyId);
			
			resultList = findWithDynamicQueryAndParams(query,pes,start,end);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;		
	}
	
	@Override 
	public int countByDifficulty(Long difficultyId) throws ApplicationException {
		int count = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> query = builder.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = query.from(JPAProblem.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.gt(rootEntity.get("difficulty").get("id"),p));
			query.select(rootEntity);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			pes.put(p, difficultyId);
			
			count = countWithDynamicQueryAndParams(query,pes);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return count;		
	}	
	
	@Override 
	public List<Problem> findByCategory(Long categoryId, int start, int end) throws ApplicationException {
		List<Problem> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> query = builder.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = query.from(JPAProblem.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("category").get("id"),p));
			query.select(rootEntity);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			pes.put(p, categoryId);
			
			final List result = findWithDynamicQueryAndParams(query,pes,start,end);
			resultList = JPAEntityUtil.copy(result, Problem.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;		
	}
	
	@Override 
	public int countByCategory(Long categoryId) throws ApplicationException {
		int count = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> query = builder.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = query.from(JPAProblem.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.gt(rootEntity.get("category").get("id"),p));
			query.select(rootEntity);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			pes.put(p, categoryId);
			
			count = countWithDynamicQueryAndParams(query,pes);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return count;		
	}		
	
	@Override
	public List<Problem> findByDifficultyAndCategoryNotInIn(final Long difficultyId, final Long categoryId, final Collection ids, final Integer offset, final Integer limit)  
			throws ApplicationException {
		List<Problem> result = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> query = builder.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = query.from(JPAProblem.class);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			
			ParameterExpression<Long> pDiff = builder.parameter(Long.class);
			pes.put(pDiff, difficultyId);
			
			ParameterExpression<Long> pCat = builder.parameter(Long.class);
			pes.put(pCat, categoryId);
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			pes.put(pIn, ids);
			
			query.select(rootEntity).where(builder.and(
					builder.equal(rootEntity.get("category").get("id"),pCat),
					builder.equal(rootEntity.get("difficulty").get("id"),pDiff),
					builder.not(rootEntity.get("id").in(pIn))
					));
			query.select(rootEntity);
			
			
			List<JPAProblem> resultList = findWithDynamicQueryAndParams(query,pes,offset,limit);
			result = JPAEntityUtil.copy(resultList, Problem.class);		
		}
		catch (Exception e) {
			throw processException(e);
		}
		
		return result;
	}
	
	@Override 
	public int countByDifficultyAndCategoryNotInIn(final Long difficultyId, final Long categoryId, final Collection ids)  
			throws ApplicationException {
		int count = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> query = builder.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = query.from(JPAProblem.class);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			
			ParameterExpression<Long> pDiff = builder.parameter(Long.class);
			pes.put(pDiff, difficultyId);
			
			ParameterExpression<Long> pCat = builder.parameter(Long.class);
			pes.put(pCat, categoryId);
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			pes.put(pIn, ids);
			
			query.select(rootEntity).where(builder.and(
					builder.equal(rootEntity.get("category").get("id"),pCat),
					builder.equal(rootEntity.get("difficulty").get("id"),pDiff),
					builder.not(rootEntity.get("id").in(pIn))
					));
			query.select(rootEntity);
			
			count = countWithDynamicQueryAndParams(query,pes);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return count;		
	}	
	
	
	//ProblemDifficulty
	@Override 
	public List<ProblemDifficulty> findAllProblemDifficulties(int start, int end) throws ApplicationException {
		List<ProblemDifficulty> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPAProblemDifficulty.class,start,end);
			result = JPAEntityUtil.copy(resultList, ProblemDifficulty.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllProblemDifficulties() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPAProblemDifficulty.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public ProblemDifficulty getProblemDifficultyByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAProblemDifficulty jpaEntity = (JPAProblemDifficulty) super.findByPrimaryKey(JPAProblemDifficulty.class, pk);
		return JPAEntityUtil.copy(jpaEntity, ProblemDifficulty.class);
	}
	
	@Override 
	public ProblemDifficulty provideProblemDifficulty(ProblemDifficulty record) throws ApplicationException {
		ProblemDifficulty existingCountry = getProblemDifficultyByCode(record.getCode());
		if (Validator.isNull(existingCountry))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemDifficulty.class));
			existingCountry = JPAEntityUtil.copy(res, ProblemDifficulty.class);
		}

		return existingCountry;	
	}
	
	@Override
	public ProblemDifficulty provideProblemDifficulty(String name) throws ApplicationException {
		ProblemDifficulty existingCountry = getProblemDifficultyByCode(name);
		if (Validator.isNull(existingCountry))
		{
			final ProblemSource record = new ProblemSource(name,name);
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemDifficulty.class));
			existingCountry = JPAEntityUtil.copy(res, ProblemDifficulty.class);
		}

		return existingCountry;			
	}
	
	public ProblemDifficulty updateProblemDifficulty(JPAProblemDifficulty record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAProblemDifficulty.class));
		ProblemDifficulty dto = JPAEntityUtil.copy(res, ProblemDifficulty.class);
		return dto;	
	}	
	
	public ProblemDifficulty getProblemDifficultyByCode(String code) throws ApplicationException{
		JPAProblemDifficulty jpaEntity = (JPAProblemDifficulty) super.findWithAttribute(JPAProblemDifficulty.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, ProblemDifficulty.class);		
	}
	
	public ProblemDifficulty getProblemDifficultyByName(String code) throws ApplicationException{
		JPAProblemDifficulty jpaEntity = (JPAProblemDifficulty) super.findWithAttribute(JPAProblemDifficulty.class, String.class,"name", code);
		return JPAEntityUtil.copy(jpaEntity, ProblemDifficulty.class);		
	}
	
	public ProblemDifficulty deleteProblemDifficulty(Long id) throws ApplicationException, NoSuchModelException {
		JPAProblemDifficulty jpaEntity = (JPAProblemDifficulty) super.findByPrimaryKey(JPAProblemDifficulty.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, ProblemDifficulty.class);
	}
		
	
	//ProblemCategory
	@Override 
	public List<ProblemCategory> findAllProblemCategories(int start, int end) throws ApplicationException {
		List<ProblemCategory> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPAProblemCategory.class,start,end);
			result = JPAEntityUtil.copy(resultList, ProblemCategory.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllProblemCategories() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPAProblemCategory.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public ProblemCategory getProblemCategoryByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAProblemCategory jpaEntity = (JPAProblemCategory) super.findByPrimaryKey(JPAProblemCategory.class, pk);
		return JPAEntityUtil.copy(jpaEntity, ProblemCategory.class);
	}
	
	@Override 
	public ProblemCategory provideProblemCategory(ProblemCategory record) throws ApplicationException {
		ProblemCategory existingCountry = getProblemCategoryByCode(record.getCode());
		if (Validator.isNull(existingCountry))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemCategory.class));
			existingCountry = JPAEntityUtil.copy(res, ProblemCategory.class);
		}

		return existingCountry;	
	}
	
	@Override
	public ProblemCategory provideProblemCategory(String name) throws ApplicationException {
		ProblemCategory existingCountry = getProblemCategoryByCode(name);
		if (Validator.isNull(existingCountry))
		{
			final ProblemSource record = new ProblemSource(name,name);
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemCategory.class));
			existingCountry = JPAEntityUtil.copy(res, ProblemCategory.class);
		}

		return existingCountry;			
	}
	
	
	public ProblemCategory updateProblemCategory(JPAProblemCategory record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAProblemCategory.class));
		ProblemCategory dto = JPAEntityUtil.copy(res, ProblemCategory.class);
		return dto;	
	}	
	
	public ProblemCategory getProblemCategoryByCode(String code) throws ApplicationException{
		JPAProblemCategory jpaEntity = (JPAProblemCategory) super.findWithAttribute(JPAProblemCategory.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, ProblemCategory.class);		
	}
	
	public ProblemCategory getProblemCategoryByName(String code) throws ApplicationException{
		JPAProblemCategory jpaEntity = (JPAProblemCategory) super.findWithAttribute(JPAProblemCategory.class, String.class,"name", code);
		return JPAEntityUtil.copy(jpaEntity, ProblemCategory.class);		
	}
	
	public ProblemCategory deleteProblemCategory(Long id) throws ApplicationException, NoSuchModelException {
		JPAProblemCategory jpaEntity = (JPAProblemCategory) super.findByPrimaryKey(JPAProblemCategory.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, ProblemCategory.class);
	}	
	
	//ProblemSource
	@Override 
	public List<ProblemSource> findAllProblemSources(int start, int end) throws ApplicationException {
		List<ProblemSource> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPAProblemSource.class,start,end);
			result = JPAEntityUtil.copy(resultList, ProblemSource.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllProblemSources() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPAProblemSource.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public ProblemSource getProblemSourceByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAProblemSource jpaEntity = (JPAProblemSource) super.findByPrimaryKey(JPAProblemSource.class, pk);
		return JPAEntityUtil.copy(jpaEntity, ProblemSource.class);
	}
	
	@Override 
	public ProblemSource provideProblemSource(ProblemSource record) throws ApplicationException {
		ProblemSource existingCountry = getProblemSourceByCode(record.getCode());
		if (Validator.isNull(existingCountry))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemSource.class));
			existingCountry = JPAEntityUtil.copy(res, ProblemSource.class);
		}

		return existingCountry;	
	}
	
	@Override
	public ProblemSource provideProblemSource(String name) throws ApplicationException {
		ProblemSource existingCountry = getProblemSourceByCode(name);
		if (Validator.isNull(existingCountry))
		{
			final ProblemSource record = new ProblemSource(name,name);
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemSource.class));
			existingCountry = JPAEntityUtil.copy(res, ProblemSource.class);
		}

		return existingCountry;			
	}
	
	public ProblemSource updateProblemSource(JPAProblemSource record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAProblemSource.class));
		ProblemSource dto = JPAEntityUtil.copy(res, ProblemSource.class);
		return dto;	
	}	
	
	public ProblemSource getProblemSourceByCode(String code) throws ApplicationException{
		JPAProblemSource jpaEntity = (JPAProblemSource) super.findWithAttribute(JPAProblemSource.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, ProblemSource.class);		
	}
	
	public ProblemSource getProblemSourceByName(String code) throws ApplicationException{
		JPAProblemSource jpaEntity = (JPAProblemSource) super.findWithAttribute(JPAProblemSource.class, String.class,"name", code);
		return JPAEntityUtil.copy(jpaEntity, ProblemSource.class);		
	}
	
	public ProblemSource deleteProblemSource(Long id) throws ApplicationException, NoSuchModelException {
		JPAProblemSource jpaEntity = (JPAProblemSource) super.findByPrimaryKey(JPAProblemSource.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, ProblemSource.class);
	}
	
	
	//Problem Picture
	@Override
	public ProblemPicture getProblemPictureByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAProblemPicture jpaEntity = (JPAProblemPicture) super.findByPrimaryKey(JPAProblemPicture.class, pk);
		return JPAEntityUtil.copy(jpaEntity, ProblemPicture.class);
	}	
	
	@Override
	public void createDefaults() throws ApplicationException {
	}

}