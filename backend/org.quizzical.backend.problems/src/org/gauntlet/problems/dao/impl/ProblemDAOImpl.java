package org.gauntlet.problems.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
import org.gauntlet.core.service.impl.AttrPair;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemCategoryLesson;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.gauntlet.problems.api.model.ProblemPicture;
import org.gauntlet.problems.api.model.ProblemSource;
import org.gauntlet.problems.api.model.ProblemType;
import org.gauntlet.problems.model.jpa.JPAProblem;
import org.gauntlet.problems.model.jpa.JPAProblemCategory;
import org.gauntlet.problems.model.jpa.JPAProblemCategoryLesson;
import org.gauntlet.problems.model.jpa.JPAProblemDifficulty;
import org.gauntlet.problems.model.jpa.JPAProblemPicture;
import org.gauntlet.problems.model.jpa.JPAProblemSource;
import org.gauntlet.problems.model.jpa.JPAProblemType;


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
			List<JPABaseEntity> resultList = super.findAll(JPAProblem.class,"sourceIndexWithinPage",start,end);
			result = JPAEntityUtil.copy(resultList, Problem.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	
	@Override
	public List<Problem> findAll(List<Long> ids)  
			throws ApplicationException {
		List<Problem> result = null;
		try {
			if (ids.isEmpty())
				ids.add(-1L);
			
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> cq = qb.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = cq.from(JPAProblem.class);
			cq.select(rootEntity).where(rootEntity.get("id").in(pIn));
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pIn, ids);
			
			
			List<JPAProblem> resultList = typedQuery.getResultList();
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
		JPAProblem jpaEntity = getByPrimary_(pk);
		return JPAEntityUtil.copy(jpaEntity, Problem.class);
	}
	
	public JPAProblem getByPrimary_(Long pk) throws ApplicationException, NoSuchModelException {
		return  (JPAProblem) super.findByPrimaryKey(JPAProblem.class, pk);
	}

	@Override
	public Problem provide(Problem record)
			  throws ApplicationException, NoSuchModelException {
		Problem existingProblem = getByCode(record.getCode());
		if (Validator.isNull(existingProblem))
		{
			System.out.println(String.format("New problem %s_%d_%d",record.getSource(),record.getSourcePageNumber(),record.getSourceIndexWithinPage()));
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblem.class));
			existingProblem = JPAEntityUtil.copy(res, Problem.class);
		}
		else {
			System.out.println(String.format("Updating problem %s_%d_%d",record.getSource(),record.getSourcePageNumber(),record.getSourceIndexWithinPage()));
			record.setId(existingProblem.getId());
			existingProblem = update(record);
		}

		return existingProblem;
	}
	
	@Override
	public Problem update(Problem record) throws ApplicationException, NoSuchModelException {
		JPAProblem prblm = (JPAProblem) super.findByPrimaryKey(JPAProblem.class,record.getId());

		prblm.setSource((JPAProblemSource) super.findByPrimaryKey(JPAProblemSource.class,record.getSource().getId()));
		prblm.setSourcePageNumber(record.getSourcePageNumber());
		prblm.setSourceIndexWithinPage(record.getSourceIndexWithinPage());
		prblm.setCategory((JPAProblemCategory)super.findByPrimaryKey(JPAProblemCategory.class,record.getCategory().getId()));
		prblm.setDifficulty((JPAProblemDifficulty)super.findByPrimaryKey(JPAProblemDifficulty.class,record.getDifficulty().getId()));
		prblm.setRequiresCalculator(record.getRequiresCalculator());
		prblm.setMultipleChoice(record.isMultipleChoice());
		prblm.setAnswerInRange(record.getAnswerInRange());
		prblm.setAnswer(record.getAnswer());
		
		JPAProblemPicture pic = (JPAProblemPicture) super.findByPrimaryKey(JPAProblemPicture.class,prblm.getQuestionPicture().getId());
		pic.setPicture(record.getQuestionPicture().getPicture());
		super.update(pic);
		
		prblm.getQuestionPicture().setPicture(record.getQuestionPicture().getPicture());
		prblm.getAnswerPicture().setPicture(record.getAnswerPicture().getPicture());
		
		JPABaseEntity res = super.update(prblm);
		Problem dto = JPAEntityUtil.copy(res, Problem.class);
		return dto;	
	}	
	
	@Override
	public void markAsQAed(Problem problem) throws ApplicationException, NoSuchModelException {
		JPAProblem problemEntity = getByPrimary_(problem.getId());
		problemEntity.setQaEd(true);
		super.update(problemEntity);
	}
	
	@Override
	public Problem delete(Long id) throws ApplicationException, NoSuchModelException {
		JPAProblem jpaEntity = (JPAProblem) super.findByPrimaryKey(JPAProblem.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, Problem.class);
	}
	
	@Override
	public void deleteAllBySourceId(Long sourceId) throws ApplicationException {
		List<Problem> rs = findAllBySource(sourceId);
		rs.stream()
			.forEach(p -> {
				try {
					delete(p.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
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
	public List<Problem> findAllByCategory(Long categoryId) throws ApplicationException {
		List<Problem> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> query = builder.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = query.from(JPAProblem.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.gt(rootEntity.get("category").get("id"),p));
			query.select(rootEntity);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			pes.put(p, categoryId);
			
			resultList = findWithDynamicQueryAndParams(query,pes);
			resultList = JPAEntityUtil.copy(resultList, Problem.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;		
	}	
	
	@Override 
	public List<Problem> findAllByType(Long typeId) throws ApplicationException {
		List<Problem> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> query = builder.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = query.from(JPAProblem.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("type").get("id"),p));

			Map<ParameterExpression,Object> pes = new HashMap<>();
			pes.put(p, typeId);
			
			resultList = findWithDynamicQueryAndParams(query,pes);
			resultList = JPAEntityUtil.copy(resultList, Problem.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;		
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
	public List<Problem> findAllBySource(Long sourceId) throws ApplicationException {
		List<Problem> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> query = builder.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = query.from(JPAProblem.class);
			
			Map<ParameterExpression, Object> pes = new HashMap<>();
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("source").get("id"),p));
			pes.put(p, sourceId);
			
			resultList = findWithDynamicQueryAndParams(query,pes);
			
			resultList = JPAEntityUtil.copy(resultList, Problem.class);
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
	public List<Problem> findByDifficultyAndCategoryNotInIn(final Long problemTypeId, final Boolean requiresCalc, final Long difficultyId, final Long categoryId, final Collection ids, final Integer offset, final Integer limit)  
			throws ApplicationException {
		List<Problem> result = null;
		try {
			if (ids.isEmpty())
				ids.add(-1L);
			
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			ParameterExpression<Long> pType = builder.parameter(Long.class);
			
			ParameterExpression<Long> pDiff = builder.parameter(Long.class);
			
			ParameterExpression<Long> pCat = builder.parameter(Long.class);
			
			ParameterExpression<Boolean> pCalc = builder.parameter(Boolean.class);
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> cq = qb.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = cq.from(JPAProblem.class);
			cq.select(rootEntity).where(builder.and(
					builder.equal(rootEntity.get("type").get("id"),pType),
					builder.equal(rootEntity.get("category").get("id"),pCat),
					builder.equal(rootEntity.get("difficulty").get("id"),pDiff),
					builder.equal(rootEntity.get("requiresCalculator"),pCalc),
					builder.not(rootEntity.get("id").in(pIn))
					));
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pType, problemTypeId);
			typedQuery.setParameter(pDiff, difficultyId);
			typedQuery.setParameter(pCat, categoryId);
			typedQuery.setParameter(pCalc, requiresCalc);
			typedQuery.setParameter(pIn, ids);
			
			typedQuery.setFirstResult(offset);
			typedQuery.setMaxResults(limit);
			
			
			List<JPAProblem> resultList = typedQuery.getResultList();
			result = JPAEntityUtil.copy(resultList, Problem.class);		
		}
		catch (Exception e) {
			throw processException(e);
		}
		
		return result;
	}
	
	@Override
	public List<Problem> findByAnyDifficultyAndCategoryNotIn(final Boolean requiresCalc, final Long categoryId, final Collection ids, final Integer offset, final Integer limit)  
			throws ApplicationException {
		List<Problem> result = null;
		try {
			if (ids.isEmpty())
				ids.add(-1L);
			
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			ParameterExpression<Long> pDiff = builder.parameter(Long.class);
			
			ParameterExpression<Long> pCat = builder.parameter(Long.class);
			
			ParameterExpression<Boolean> pCalc = builder.parameter(Boolean.class);
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> cq = qb.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = cq.from(JPAProblem.class);
			cq.select(rootEntity).where(builder.and(
					builder.equal(rootEntity.get("category").get("id"),pCat),
					builder.equal(rootEntity.get("requiresCalculator"),pCalc),
					builder.not(rootEntity.get("id").in(pIn))
					));
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pCat, categoryId);
			typedQuery.setParameter(pCalc, requiresCalc);
			typedQuery.setParameter(pIn, ids);
			
			typedQuery.setFirstResult(offset);
			typedQuery.setMaxResults(limit);
			
			
			List<JPAProblem> resultList = typedQuery.getResultList();
			result = JPAEntityUtil.copy(resultList, Problem.class);		
		}
		catch (Exception e) {
			throw processException(e);
		}
		
		return result;
	}
	
	@Override
	public List<Problem> findByCategoryNotIn(final Long categoryId, final Collection ids, final Integer offset, final Integer limit)  
			throws ApplicationException {
		List<Problem> result = null;
		try {
			if (ids.isEmpty())
				ids.add(-1L);
			
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			ParameterExpression<Long> pCat = builder.parameter(Long.class);
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> cq = qb.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = cq.from(JPAProblem.class);
			cq.select(rootEntity).where(builder.and(
					builder.equal(rootEntity.get("category").get("id"),pCat),
					builder.not(rootEntity.get("id").in(pIn))
					));
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pCat, categoryId);
			typedQuery.setParameter(pIn, ids);
			if (offset >= 0)
				typedQuery.setFirstResult(offset);
			if (limit >= 0)
				typedQuery.setMaxResults(limit);
			
			
			List<JPAProblem> resultList = typedQuery.getResultList();
			result = JPAEntityUtil.copy(resultList, Problem.class);		
		}
		catch (Exception e) {
			throw processException(e);
		}
		
		return result;
	}
	
	@Override
	public List<Problem> findByDifficultyNotIn(final Long difficultyId, final Collection ids, final Integer offset, final Integer limit)  
			throws ApplicationException {
		List<Problem> result = null;
		try {
			if (ids.isEmpty())
				ids.add(-1L);
			
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			ParameterExpression<Long> pCat = builder.parameter(Long.class);
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> cq = qb.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = cq.from(JPAProblem.class);
			cq.select(rootEntity).where(builder.and(
					builder.equal(rootEntity.get("difficulty").get("id"),pCat),
					builder.not(rootEntity.get("id").in(pIn))
					));
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pCat, difficultyId);
			typedQuery.setParameter(pIn, ids);
			if (offset >= 0)
				typedQuery.setFirstResult(offset);
			if (limit >= 0)
				typedQuery.setMaxResults(limit);
			
			
			List<JPAProblem> resultList = typedQuery.getResultList();
			result = JPAEntityUtil.copy(resultList, Problem.class);		
		}
		catch (Exception e) {
			throw processException(e);
		}
		
		return result;
	}
	
	@Override 
	public long countByCalcAndDifficultyAndCategoryNotInIn(final Long problemTypeId,final Boolean requiresCalc, final Long difficultyId, final Long categoryId, final List<Long> ids)  
			throws ApplicationException {
		long count = 0;
		try {
			if (ids.isEmpty())
				ids.add(-1L);
			
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			ParameterExpression<Long> pType = builder.parameter(Long.class);
			
			ParameterExpression<Long> pDiff = builder.parameter(Long.class);
			
			ParameterExpression<Long> pCat = builder.parameter(Long.class);
			
			ParameterExpression<Boolean> pCalc = builder.parameter(Boolean.class);
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<Long> cq = qb.createQuery(Long.class);
			Root<JPAProblem> rootEntity = cq.from(JPAProblem.class);
			cq.select(qb.count(rootEntity));
			cq.where(builder.and(
					builder.equal(rootEntity.get("type").get("id"),pType),
					builder.equal(rootEntity.get("category").get("id"),pCat),
					builder.equal(rootEntity.get("difficulty").get("id"),pDiff),
					builder.equal(rootEntity.get("requiresCalculator"),pCalc),
					builder.not(rootEntity.get("id").in(pIn))
					));
			
			TypedQuery typedQuery = getEm().createQuery(cq);

			typedQuery.setParameter(pType, problemTypeId);
			typedQuery.setParameter(pDiff, difficultyId);
			typedQuery.setParameter(pCat, categoryId);
			typedQuery.setParameter(pCalc, requiresCalc);
			typedQuery.setParameter(pIn, ids);
			
			count = (long) typedQuery.getSingleResult();
		}
		catch (Exception e) {
			throw processException(e);
		}
		return count;		
	}	
	
	@Override 
	public long countByCategoryNotIn(final Long categoryId, final List<Long> ids)  
			throws ApplicationException {
		long count = 0;
		try {
			if (ids.isEmpty())
				ids.add(-1L);
			
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			ParameterExpression<Long> pDiff = builder.parameter(Long.class);
			
			ParameterExpression<Long> pCat = builder.parameter(Long.class);
			
			ParameterExpression<Boolean> pCalc = builder.parameter(Boolean.class);
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<Long> cq = qb.createQuery(Long.class);
			Root<JPAProblem> rootEntity = cq.from(JPAProblem.class);
			cq.select(qb.count(rootEntity));
			cq.where(builder.and(
					builder.equal(rootEntity.get("category").get("id"),pCat),
					builder.equal(rootEntity.get("difficulty").get("id"),pDiff),
					builder.equal(rootEntity.get("requiresCalculator"),pCalc),
					builder.not(rootEntity.get("id").in(pIn))
					));
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pCat, categoryId);
			typedQuery.setParameter(pIn, ids);
			
			count = (long) typedQuery.getSingleResult();
		}
		catch (Exception e) {
			throw processException(e);
		}
		return count;		
	}	
	
	
	@Override
	public List<Problem> getAllUserQuizzedProblems(User user, List<Long> usedInQuizProblemIds, Integer limit)  
			throws ApplicationException {
		List<Problem> result = null;
		try {
			if (usedInQuizProblemIds.isEmpty())
				usedInQuizProblemIds.add(-1L);
			
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> cq = qb.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = cq.from(JPAProblem.class);
			
			cq.select(rootEntity).where(rootEntity.get("id").in(pIn));
			
			
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pIn, usedInQuizProblemIds);
			
			typedQuery.setFirstResult(0);
			typedQuery.setMaxResults(limit);
			
			
			List<JPAProblem> resultList = typedQuery.getResultList();
			result = JPAEntityUtil.copy(resultList, Problem.class);		
		}
		catch (Exception e) {
			throw processException(e);
		}
		
		return result;
	}
	
	@Override
	public List<Problem> getAllUserNonQuizzedProblems(Long problemTypeId, User user, List<Long> usedInQuizProblemIds, Integer limit)  
			throws ApplicationException {
		List<Problem> result = null;
		try {
			if (usedInQuizProblemIds.isEmpty())
				usedInQuizProblemIds.add(-1L);
			
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			
			ParameterExpression<Collection> pIn = builder.parameter(Collection.class);
			ParameterExpression<Long> pType = builder.parameter(Long.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> cq = qb.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = cq.from(JPAProblem.class);

			cq.where(builder.and(
					builder.equal(rootEntity.get("type").get("id"),pType),
					builder.not(rootEntity.get("id").in(pIn))
					));
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pIn, usedInQuizProblemIds);
			typedQuery.setParameter(pType, problemTypeId);
			
			typedQuery.setFirstResult(0);
			typedQuery.setMaxResults(limit);
			
			List<JPAProblem> resultList = typedQuery.getResultList();
			result = JPAEntityUtil.copy(resultList, Problem.class);		
		}
		catch (Exception e) {
			throw processException(e);
		}
		
		return result;
	}
	
	@Override
	public List<Problem> getAllNonQAedProblems( Integer limit )
			throws ApplicationException {
		List<Problem> result = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			
			ParameterExpression<Boolean> pQaEd = builder.parameter(Boolean.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<JPAProblem> cq = qb.createQuery(JPAProblem.class);
			Root<JPAProblem> rootEntity = cq.from(JPAProblem.class);
			cq.select(rootEntity).where(builder.notEqual(rootEntity.get("qaEd"),pQaEd));
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pQaEd, true);
			
			typedQuery.setFirstResult(0);
			typedQuery.setMaxResults(limit);
			
			List<JPAProblem> resultList = typedQuery.getResultList();
			result = JPAEntityUtil.copy(resultList, Problem.class);		
		}
		catch (Exception e) {
			throw processException(e);
		}
		
		return result;		
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
		ProblemDifficulty recordEntity = getProblemDifficultyByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemDifficulty.class));
			recordEntity = JPAEntityUtil.copy(res, ProblemDifficulty.class);
		}

		return recordEntity;	
	}
	
	@Override
	public ProblemDifficulty provideProblemDifficulty(String name) throws ApplicationException {
		ProblemDifficulty recordEntity = getProblemDifficultyByCode(name);
		if (Validator.isNull(recordEntity))
		{
			final ProblemSource record = new ProblemSource(name,name);
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemDifficulty.class));
			recordEntity = JPAEntityUtil.copy(res, ProblemDifficulty.class);
		}

		return recordEntity;			
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
			final List<JPABaseEntity> resultList = super.findAll(JPAProblemCategory.class,start,end);
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
		ProblemCategory recordEntity = getProblemCategoryByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemCategory.class));
			recordEntity = JPAEntityUtil.copy(res, ProblemCategory.class);
		}

		return recordEntity;	
	}
	
	@Override
	public ProblemCategory provideProblemCategory(String name) throws ApplicationException {
		ProblemCategory recordEntity = getProblemCategoryByCode(name);
		if (Validator.isNull(recordEntity))
		{
			final ProblemSource record = new ProblemSource(name,name);
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemCategory.class));
			recordEntity = JPAEntityUtil.copy(res, ProblemCategory.class);
		}

		return recordEntity;			
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
	
	public ProblemCategoryLesson addProblemCategoryLesson(Long categoryId, Long lessonId) throws ApplicationException, NoSuchModelException {
		JPAProblemCategory jpaEntity = (JPAProblemCategory) super.findByPrimaryKey(JPAProblemCategory.class, categoryId);
		JPAProblemCategoryLesson catLesson = new JPAProblemCategoryLesson(jpaEntity,lessonId);
		catLesson = (JPAProblemCategoryLesson) super.add(catLesson);
		return JPAEntityUtil.copy(catLesson, ProblemCategoryLesson.class);
	}
	
	public void removeProblemCategoryLesson(Long categoryLessonId) throws ApplicationException, NoSuchModelException {
		JPAProblemCategoryLesson jpaEntity = (JPAProblemCategoryLesson) super.findByPrimaryKey(JPAProblemCategoryLesson.class, categoryLessonId);
		super.remove(jpaEntity);
	}
	
	@Override 
	public List<ProblemCategory> findAllProblemCategoriesByProblemType(Long problemTypeId) throws ApplicationException {
		List<Problem> problems = findAllByType(problemTypeId);
		
		
		Map<Long,ProblemCategory> result = new HashMap<>();
		try {
			problems.stream()
				.forEach(p -> {
					result.put(p.getCategory().getId(), JPAEntityUtil.copy(p.getCategory(), ProblemCategory.class));
				});
		}
		catch (Exception e) {
			throw processException(e);
		}
		return new ArrayList(result.values());		
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
		ProblemSource recordEntity = getProblemSourceByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemSource.class));
			recordEntity = JPAEntityUtil.copy(res, ProblemSource.class);
		}

		return recordEntity;	
	}
	
	@Override
	public ProblemSource provideProblemSource(String name) throws ApplicationException {
		ProblemSource recordEntity = getProblemSourceByCode(name);
		if (Validator.isNull(recordEntity))
		{
			final ProblemSource record = new ProblemSource(name,name);
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemSource.class));
			recordEntity = JPAEntityUtil.copy(res, ProblemSource.class);
		}

		return recordEntity;			
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
	public ProblemPicture updateProblemPicture(ProblemPicture record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAProblemPicture.class));
		ProblemPicture dto = JPAEntityUtil.copy(res, ProblemPicture.class);
		return dto;	
	}
	
	//ProblemType
	@Override 
	public List<ProblemType> findAllProblemTypes(int start, int end) throws ApplicationException {
		List<ProblemType> result = new ArrayList<>();
		try {
			final List<JPABaseEntity> resultList = super.findAll(JPAProblemType.class,start,end);
			result = JPAEntityUtil.copy(resultList, ProblemType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllProblemTypes() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPAProblemType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public ProblemType getProblemTypeByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAProblemType jpaEntity = (JPAProblemType) super.findByPrimaryKey(JPAProblemType.class, pk);
		return JPAEntityUtil.copy(jpaEntity, ProblemType.class);
	}
	
	@Override 
	public ProblemType provideProblemType(ProblemType record) throws ApplicationException {
		ProblemType recordEntity = getProblemTypeByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemType.class));
			recordEntity = JPAEntityUtil.copy(res, ProblemType.class);
		}

		return recordEntity;	
	}
	
	@Override
	public ProblemType provideProblemType(String name) throws ApplicationException {
		ProblemType recordEntity = getProblemTypeByCode(name);
		if (Validator.isNull(recordEntity))
		{
			final ProblemSource record = new ProblemSource(name,name);
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAProblemType.class));
			recordEntity = JPAEntityUtil.copy(res, ProblemType.class);
		}

		return recordEntity;			
	}
	
	
	public ProblemType updateProblemType(JPAProblemType record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAProblemType.class));
		ProblemType dto = JPAEntityUtil.copy(res, ProblemType.class);
		return dto;	
	}	
	
	public ProblemType getProblemTypeByCode(String code) throws ApplicationException{
		JPAProblemType jpaEntity = (JPAProblemType) super.findWithAttribute(JPAProblemType.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, ProblemType.class);		
	}
	
	public ProblemType getProblemTypeByName(String code) throws ApplicationException{
		JPAProblemType jpaEntity = (JPAProblemType) super.findWithAttribute(JPAProblemType.class, String.class,"name", code);
		return JPAEntityUtil.copy(jpaEntity, ProblemType.class);		
	}
	
	public ProblemType deleteProblemType(Long id) throws ApplicationException, NoSuchModelException {
		JPAProblemType jpaEntity = (JPAProblemType) super.findByPrimaryKey(JPAProblemType.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, ProblemType.class);
	}	
	
	@Override
	public void createDefaults() throws ApplicationException {
	}

}