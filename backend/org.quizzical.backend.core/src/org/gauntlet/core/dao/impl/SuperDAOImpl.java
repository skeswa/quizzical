package org.gauntlet.core.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.osgi.service.log.LogService;
import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.ISuperDAO;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.model.SuperEntity;

public abstract class SuperDAOImpl<T extends SuperEntity> implements ISuperDAO<T> {

	public abstract LogService getLogger();


	public abstract EntityManager getEm();


	@Override
	public long countWithDynamicQuery(final CriteriaQuery<T> dynamicQuery)
		throws ApplicationException {

		return countWithDynamic(dynamicQuery);
	}

	
	public Long countWithDynamic(final CriteriaQuery<T> criteria)
	  {
	    final CriteriaBuilder builder=getEm().getCriteriaBuilder();
	    final CriteriaQuery<Long> countCriteria=builder.createQuery(Long.class);
	    countCriteria.select(builder.count(criteria.getRoots().iterator().next()));
	    final Predicate
	            groupRestriction=criteria.getGroupRestriction(),
	            fromRestriction=criteria.getRestriction();
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

	@Override
	@SuppressWarnings("unused")
	public T findByPrimaryKey(Class<T> className, Serializable primaryKey)
		throws NoSuchModelException, ApplicationException {

		return getEm().find(className, primaryKey);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List findWithDynamicQuery(CriteriaQuery<T> dynamicQuery)
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

	@Override
	@SuppressWarnings("rawtypes")
	public List findWithDynamicQuery(CriteriaQuery<T> dynamicQuery, int start, int end)
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
	
	@Override
	@SuppressWarnings("rawtypes")
	public List<T> findAll(Class<T> entityType, int start, int end)
		throws ApplicationException {
		List<T> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(entityType);
			Root<T> rootEntity = query.from(entityType);
			query.select(rootEntity);
			
			resultList = findWithDynamicQuery(query,start,end);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;
	}	
	
	@Override
	@SuppressWarnings("rawtypes")
	public int countAll(Class<T> entityType, int start, int end)
		throws ApplicationException {
		int res = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(entityType);
			Root<T> rootEntity = query.from(entityType);
			query.select(rootEntity);
			
			res = findWithDynamicQuery(query,start,end).size();
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	@SuppressWarnings("rawtypes")
	public long countAll(Class<T> entityType)
		throws ApplicationException {
		long res = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(entityType);
			Root<T> rootEntity = query.from(entityType);
			query.select(rootEntity);
			
			res = countWithDynamic(query);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}		
	
	@Override
	@SuppressWarnings("rawtypes")
	public List<T> findAll(Class<T> entityType)
		throws ApplicationException {
		List<T> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(entityType);
			Root<T> rootEntity = query.from(entityType);
			query.select(rootEntity);
			
			resultList = findWithDynamicQuery(query);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;
	}	
	
	
	@Override
	@SuppressWarnings("rawtypes")
	public T findWithAttribute(Class<T> entityClass, Class attrClass, String attrName, Object attrValue)
		throws ApplicationException {
		T res = null;
		try {
			CriteriaQuery<T> query = null;
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			assert builder != null : "query builder obj must not be null";
			
			query = builder.createQuery(entityClass);
			assert query != null : "query obj must not be null";
			
			Root<T> rootEntity = query.from(entityClass);
			ParameterExpression p = builder.parameter(attrClass);
			query.select(rootEntity).where(builder.equal(rootEntity.get(attrName), p));
			
			TypedQuery<T> typedQuery = getEm().createQuery(query);
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
	
	@Override
	@SuppressWarnings("rawtypes")
	public int countWithAttribute(Class<T> entityClass, Class attrClass, String attrName, Object attrValue)
		throws ApplicationException {
		int res = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(entityClass);
			Root<T> rootEntity = query.from(entityClass);
			ParameterExpression p = builder.parameter(attrClass);
			query.select(rootEntity).where(builder.equal(rootEntity.get(attrName), p));
			
			TypedQuery<T> typedQuery = getEm().createQuery(query);
			typedQuery.setParameter(p, attrValue);

			res = typedQuery.getResultList().size();
				
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}


	@Override
	public void flush() throws ApplicationException {
		try {
			getEm().flush();
		}
		catch (Exception e) {
			throw processException(e);
		}
	}

	@Override
	public ApplicationException processException(Exception e) {
		return new ApplicationException(e);
	}

	@Override
	@SuppressWarnings("unused")
	public T remove(Serializable primaryKey)
		throws NoSuchModelException, ApplicationException, NoSuchModelException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	@SuppressWarnings("unused")
	public T remove(Class<T> classType, Serializable primaryKey)
		throws NoSuchModelException, ApplicationException {

		T rec = this.findByPrimaryKey(classType, primaryKey);
		getEm().remove(primaryKey);
		
		return rec;
	}	

	@Override
	public T remove(T model) throws ApplicationException {

		getEm().remove(model);
		return model;
	}

	@Override
	public T update(T model) throws ApplicationException {
		//model.setDateLastUpdated(new Date());
		return getEm().merge(model);
	}
	

	@Override
	public T findByPrimaryKey(Serializable primaryKey)
			throws NoSuchModelException, ApplicationException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public T add(T model) throws ApplicationException {
		getEm().persist(model);
		getEm().flush();
		getEm().refresh(model);
		
		return model;
	}
}