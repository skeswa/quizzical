package org.gauntlet.core.api.dao;



import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.model.BaseEntity;

public interface IBaseDAO<T extends BaseEntity> {
	/**
	 * Returns the number of rows that match the dynamic query.
	 *
	 * @param  dynamicQuery the dynamic query
	 * @return the number of rows that match the dynamic query
	 * @throws ApplicationException if a system exception occurred
	 */
	public long countWithDynamicQuery(CriteriaQuery<T> dynamicQuery)
		throws ApplicationException;
	
	
	public T findByPrimaryKey(Serializable primaryKey)
			throws NoSuchModelException, ApplicationException;

	
	public T findByPrimaryKey(Class<T> className, Serializable primaryKey)
			throws NoSuchModelException, ApplicationException;

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param  dynamicQuery the dynamic query
	 * @return the matching rows
	 * @throws ApplicationException if a system exception occurred
	 */
	@SuppressWarnings("rawtypes")
	public List findWithDynamicQuery(CriteriaQuery<T> dynamicQuery)
		throws ApplicationException;

	/**
	 * Performs a dynamic query on the database and returns a range of the
	 * matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end -
	 * start</code> instances. <code>start</code> and <code>end</code> are not
	 * primary keys, they are indexes in the result set. Thus, <code>0</code>
	 * refers to the first result in the set. Setting both <code>start</code>
	 * and <code>end</code> to {@link
	 * com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full
	 * result set.
	 * </p>
	 *
	 * @param  dynamicQuery the dynamic query
	 * @param  start the lower bound of the range of matching rows
	 * @param  end the upper bound of the range of matching rows (not inclusive)
	 * @return the range of matching rows
	 * @throws ApplicationException if a system exception occurred
	 * @see    com.liferay.portal.kernel.dao.orm.QueryUtil#list(
	 *         com.liferay.portal.kernel.dao.orm.Query,
	 *         com.liferay.portal.kernel.dao.orm.Dialect, int, int)
	 */
	@SuppressWarnings("rawtypes")
	public List findWithDynamicQuery(CriteriaQuery<T> dynamicQuery, int start, int end)
		throws ApplicationException;
	
	@SuppressWarnings("rawtypes")
	public List findWithDynamicQueryAndParams(CriteriaQuery dynamicQuery, Map<ParameterExpression,Object> paramMap, int start, int end)
		throws ApplicationException;	

	@SuppressWarnings("rawtypes")
	public int countWithDynamicQueryAndParams(CriteriaQuery dynamicQuery, Map<ParameterExpression,Object> paramMap)
			throws ApplicationException;	

	@SuppressWarnings("rawtypes")
	public T findWithAttribute(Class<T> entityClass, Class attrClass, String attrName, Object attrValue)
			throws ApplicationException;
	
	@SuppressWarnings("rawtypes")
	List<T> findAll(Class<T> entityType, int start, int end)
			throws ApplicationException;
	
	@SuppressWarnings("rawtypes")
	List<T> findAll(Class<T> entityType)
			throws ApplicationException;	
	
	@SuppressWarnings("rawtypes")
	int countAll(Class<T> entityType, int start, int end)
			throws ApplicationException;
	
	@SuppressWarnings("rawtypes")
	long countAll(Class<T> entityType)
			throws ApplicationException;	

	@SuppressWarnings("rawtypes")
	int countWithAttribute(Class<T> entityClass, Class attrClass,
			String attrName, Object attrValue) throws ApplicationException;
	
	public void flush() throws ApplicationException;


	public ApplicationException processException(Exception e);


	/**
	 * Removes the model instance with the primary key from the database. Also
	 * notifies the appropriate model listeners.
	 *
	 * @param  primaryKey the primary key of the model instance to remove
	 * @return the model instance that was removed
	 * @throws NoSuchModelException if an instance of this model with the
	 *         primary key could not be found
	 * @throws ApplicationException if a system exception occurred
	 */
	public T remove(Serializable primaryKey)
		throws NoSuchModelException, ApplicationException;

	/**
	 * Removes the model instance from the database. Also notifies the
	 * appropriate model listeners.
	 *
	 * @param  model the model instance to remove
	 * @return the model instance that was removed
	 * @throws ApplicationException if a system exception occurred
	 */
	public T remove(T model) throws ApplicationException;
	
	public T remove(Class<T> classType, Serializable primaryKey)
			throws NoSuchModelException, ApplicationException;


	/**
	 * Updates the model instance in the database or adds it if it does not yet
	 * exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * Typically not called directly, use local service update model methods
	 * instead. For example, {@link
	 * com.liferay.portal.service.UserLocalServiceUtil#updateUser(
	 * com.liferay.portal.model.User)}.
	 * </p>
	 *
	 * @param  model the model instance to update
	 * @return the model instance that was updated
	 * @throws ApplicationException if a system exception occurred
	 */
	public T update(T model) throws ApplicationException;


	public T add(T model) throws ApplicationException;
}