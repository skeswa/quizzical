package org.gauntlet.core.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.core.model.JPABaseEntity;
import org.osgi.service.log.LogService;


public abstract class BaseServiceImpl implements IBaseService {
	public abstract LogService getLogger();
	
	public abstract EntityManager getEm();
	
	@SuppressWarnings("rawtypes")
	public List findWithDynamicQueryAndParams(CriteriaQuery dynamicQuery, Map<ParameterExpression,Object> paramMap, int start, int end)
		throws ApplicationException {
		List resultList = null;
		try {
			TypedQuery typedQuery = getEm().createQuery(dynamicQuery);
			for (ParameterExpression pe : paramMap.keySet()) {
				typedQuery.setParameter(pe, paramMap.get(pe));
			}
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(end-start);
			resultList = typedQuery.getResultList();
				
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;
	}	
	
	@SuppressWarnings("rawtypes")
	public int countWithDynamicQueryAndParams(CriteriaQuery dynamicQuery, Map<ParameterExpression,Object> paramMap)
		throws ApplicationException {
		int res = 0;
		try {
			TypedQuery typedQuery = getEm().createQuery(dynamicQuery);
			for (ParameterExpression pe : paramMap.keySet()) {
				typedQuery.setParameter(pe, paramMap.get(pe));
			}
			res = typedQuery.getResultList().size();
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}		
	
	public long countWithDynamicQuery(final CriteriaQuery<? extends JPABaseEntity> dynamicQuery)
		throws ApplicationException {

		return countWithDynamic(dynamicQuery);
	}

	
	public Long countWithDynamic(final CriteriaQuery<? extends JPABaseEntity> criteria)
	  {
	    final CriteriaBuilder builder=getEm().getCriteriaBuilder();
	    final CriteriaQuery<Long> countCriteria=builder.createQuery(Long.class);
	    countCriteria.select(builder.count(criteria.getRoots().iterator().next()));
	    final Predicate groupRestriction= criteria.getGroupRestriction();
	    final Predicate fromRestriction = criteria.getRestriction();
	    if(groupRestriction != null){
	      countCriteria.having(groupRestriction);
	    }
	    if(fromRestriction != null){
	      countCriteria.where(fromRestriction);
	    }
	    countCriteria.groupBy(criteria.getGroupList());
	    countCriteria.distinct(criteria.isDistinct());
	    return getEm().createQuery(countCriteria).getSingleResult();
	  }	
	
	
	public Long getRowCount(final CriteriaQuery<? extends JPABaseEntity> criteriaQuery){
		Root<?> root = (Root<?>) criteriaQuery.getRoots().iterator().next();
		final CriteriaBuilder criteriaBuilder=getEm().getCriteriaBuilder();
	    CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
	    Root<?> entityRoot = countCriteria.from(root.getJavaType());
	    //entityRoot.alias(root.getAlias());
	    doJoins(root.getJoins(),entityRoot);
	    countCriteria.select(criteriaBuilder.count(entityRoot));
	    countCriteria.where(criteriaQuery.getRestriction());
	    return this.getEm().createQuery(countCriteria).getSingleResult();
	}

	private void doJoins(Set<? extends Join<?, ?>> joins,Root<?> root_){
	    for(Join<?,?> join: joins){
	        Join<?,?> joined = root_.join(join.getAttribute().getName(),join.getJoinType());
	        doJoins(join.getJoins(), joined);
	    }
	}

	private void doJoins(Set<? extends Join<?, ?>> joins,Join<?,?> root_){
	    for(Join<?,?> join: joins){
	        Join<?,?> joined = root_.join(join.getAttribute().getName(),join.getJoinType());
	        doJoins(join.getJoins(),joined);
	    }
	}

	@SuppressWarnings("unused")
	public JPABaseEntity findByPrimaryKey(Class<? extends JPABaseEntity> className, Serializable primaryKey)
		throws NoSuchModelException, ApplicationException {

		return getEm().find(className, primaryKey);
	}

	@SuppressWarnings("rawtypes")
	public List findWithDynamicQuery(CriteriaQuery<? extends JPABaseEntity> dynamicQuery)
		throws ApplicationException {
		List resultList = null;
		try {
			TypedQuery typedQuery = getEm().createQuery(dynamicQuery);
			resultList = typedQuery.getResultList();
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	public List findWithDynamicQuery(CriteriaQuery<? extends JPABaseEntity> dynamicQuery, int start, int end)
		throws ApplicationException {
		List resultList = null;
		try {
			TypedQuery typedQuery = getEm().createQuery(dynamicQuery);
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(end-start);
			resultList = typedQuery.getResultList();
				
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;
	}
	
	@SuppressWarnings("rawtypes")
	public List<JPABaseEntity> findAll(Class<? extends JPABaseEntity> entityType, int start, int end)
		throws ApplicationException {
		List<JPABaseEntity> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPABaseEntity> query = (CriteriaQuery<JPABaseEntity>) builder.createQuery(entityType);
			Root<JPABaseEntity> rootEntity = (Root<JPABaseEntity>) query.from(entityType);
			query.select(rootEntity);
			
			resultList = findWithDynamicQuery(query,start,end);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;
	}	
	
	@SuppressWarnings("rawtypes")
	public int countAll(Class<? extends JPABaseEntity> entityType, int start, int end)
		throws ApplicationException {
		int res = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPABaseEntity> query = (CriteriaQuery<JPABaseEntity>) builder.createQuery(entityType);
			Root<JPABaseEntity> rootEntity = (Root<JPABaseEntity>) query.from(entityType);
			query.select(rootEntity);
			
			res = findWithDynamicQuery(query,start,end).size();
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@SuppressWarnings("rawtypes")
	public long countAll(Class<? extends JPABaseEntity> entityType)
		throws ApplicationException {
		long res = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPABaseEntity> query = (CriteriaQuery<JPABaseEntity>) builder.createQuery(entityType);
			Root<JPABaseEntity> rootEntity = (Root<JPABaseEntity>) query.from(entityType);
			query.select(rootEntity);
			
			res = getRowCount(query);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}
	

	@SuppressWarnings("rawtypes")
	public List<JPABaseEntity> findAll(Class<? extends JPABaseEntity> entityType)
		throws ApplicationException {
		List<JPABaseEntity> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPABaseEntity> query = (CriteriaQuery<JPABaseEntity>) builder.createQuery(entityType);
			Root<JPABaseEntity> rootEntity = (Root<JPABaseEntity>) query.from(entityType);
			query.select(rootEntity);
			
			resultList = findWithDynamicQuery(query);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;
	}	
	
	
	@SuppressWarnings("rawtypes")
	public JPABaseEntity findWithAttribute(Class<? extends JPABaseEntity> entityClass, Class attrClass, String attrName, Object attrValue)
		throws ApplicationException {
		JPABaseEntity res = null;
		try {
			CriteriaQuery<JPABaseEntity> query = null;
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			assert builder != null : "query builder obj must not be null";
			
			query = (CriteriaQuery<JPABaseEntity>) builder.createQuery(entityClass);
			assert query != null : "query obj must not be null";
			
			Root<JPABaseEntity> rootEntity = (Root<JPABaseEntity>) query.from(entityClass);
			TypedQuery<JPABaseEntity> typedQuery = getEm().createQuery(query);
			
			ParameterExpression p = builder.parameter(attrClass);
			query.select(rootEntity).where(builder.equal(rootEntity.get(attrName), p));
			assert typedQuery != null : "typedQuery obj must not be null";
			typedQuery.setParameter(p, attrValue);

			res = typedQuery.getSingleResult();
				
		}
		catch (NoResultException e) {
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@SuppressWarnings("rawtypes")
	public JPABaseEntity findWithAttributes(Class<? extends JPABaseEntity> entityClass, Set<AttrPair> attrs)
		throws ApplicationException {
		JPABaseEntity res = null;
		try {
			CriteriaQuery<JPABaseEntity> query = null;
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			assert builder != null : "query builder obj must not be null";
			
			query = (CriteriaQuery<JPABaseEntity>) builder.createQuery(entityClass);
			assert query != null : "query obj must not be null";
			
			Root<JPABaseEntity> rootEntity = (Root<JPABaseEntity>) query.from(entityClass);
			
			TypedQuery<JPABaseEntity> typedQuery = getEm().createQuery(query);
			assert typedQuery != null : "typedQuery obj must not be null";
			
			for (AttrPair ap : attrs) {
				final ParameterExpression p = builder.parameter(ap.getAttrClass());
				query.select(rootEntity).where(builder.equal(rootEntity.get(ap.getAttrName()), p));
				
				typedQuery.setParameter(p, ap.getAttrValue());
			}

			res = typedQuery.getSingleResult();
				
		}
		catch (NoResultException e) {
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}		
	
	@SuppressWarnings("rawtypes")
	public List<JPABaseEntity> findAllWithAttributes(Class<? extends JPABaseEntity> entityClass, Set<AttrPair> attrs)
		throws ApplicationException {
		List<JPABaseEntity> res = null;
		try {
			CriteriaQuery<JPABaseEntity> query = null;
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			assert builder != null : "query builder obj must not be null";
			
			query = (CriteriaQuery<JPABaseEntity>) builder.createQuery(entityClass);
			assert query != null : "query obj must not be null";
			
			Root<JPABaseEntity> rootEntity = (Root<JPABaseEntity>) query.from(entityClass);
			
			TypedQuery<JPABaseEntity> typedQuery = getEm().createQuery(query);
			assert typedQuery != null : "typedQuery obj must not be null";
			
			for (AttrPair ap : attrs) {
				final ParameterExpression p = builder.parameter(ap.getAttrClass());
				query.select(rootEntity).where(builder.equal(rootEntity.get(ap.getAttrName()), p));
				
				typedQuery.setParameter(p, ap.getAttrName());
			}

			res = typedQuery.getResultList();
				
		}
		catch (NoResultException e) {
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}			
	
	@SuppressWarnings("rawtypes")
	public int countWithAttributes(Set<AttrPair> attrs, Class<? extends JPABaseEntity> entityClass)
		throws ApplicationException {
		int res = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPABaseEntity> query = (CriteriaQuery<JPABaseEntity>) builder.createQuery(entityClass);
			Root<JPABaseEntity> rootEntity = (Root<JPABaseEntity>) query.from(entityClass);
			
			TypedQuery<JPABaseEntity> typedQuery = getEm().createQuery(query);

			for (AttrPair ap : attrs) {
				final ParameterExpression p = builder.parameter(ap.getAttrClass());
				query.select(rootEntity).where(builder.equal(rootEntity.get(ap.getAttrName()), p));
				
				typedQuery.setParameter(p, ap.getAttrName());
			}
			
			res = typedQuery.getResultList().size();
				
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}
	
	@SuppressWarnings("rawtypes")
	public int countWithAttribute(Class<? extends JPABaseEntity> entityClass, Class attrClass, String attrName, Object attrValue)
		throws ApplicationException {
		int res = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPABaseEntity> query = (CriteriaQuery<JPABaseEntity>) builder.createQuery(entityClass);
			Root<JPABaseEntity> rootEntity = (Root<JPABaseEntity>) query.from(entityClass);
			ParameterExpression p = builder.parameter(attrClass);
			query.select(rootEntity).where(builder.equal(rootEntity.get(attrName), p));
			
			TypedQuery<JPABaseEntity> typedQuery = getEm().createQuery(query);
			typedQuery.setParameter(p, attrValue);

			res = typedQuery.getResultList().size();
				
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}


	public void flush() throws ApplicationException {
		try {
			getEm().flush();
		}
		catch (Exception e) {
			throw processException(e);
		}
	}

	public ApplicationException processException(Exception e) {
		return new ApplicationException(e);
	}

	@SuppressWarnings("unused")
	public JPABaseEntity remove(Serializable primaryKey)
		throws NoSuchModelException, ApplicationException, NoSuchModelException {

		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("unused")
	public JPABaseEntity remove(Class<? extends JPABaseEntity> classType, Serializable primaryKey)
		throws NoSuchModelException, ApplicationException {

		JPABaseEntity rec = this.findByPrimaryKey(classType, primaryKey);
		getEm().remove(primaryKey);
		
		return rec;
	}	

	public JPABaseEntity remove(JPABaseEntity model) throws ApplicationException {

		getEm().remove(model);
		return model;
	}

	public JPABaseEntity update(JPABaseEntity model) throws ApplicationException {
		JPABaseEntity managedEntity = getEm().merge(model);
		managedEntity.setDateLastUpdated(new Date());
		return managedEntity;
	}
	

	public JPABaseEntity findByPrimaryKey(Serializable primaryKey)
			throws NoSuchModelException, ApplicationException {
		throw new UnsupportedOperationException();
	}
	
	public JPABaseEntity add(JPABaseEntity model) throws ApplicationException {
		//model.setId(null);
		//model.setVersion(null);
		getEm().persist(model);
		//getEm().merge(model);
		//getEm().refresh(model);
		
		return model;
	}
	
	public JPABaseEntity merge(JPABaseEntity model) throws ApplicationException {
		model = getEm().merge(model);
		return model;
	}
}