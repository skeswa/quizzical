package org.gauntlet.lessons.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.lessons.api.model.Lesson;
import org.gauntlet.lessons.api.model.LessonProblem;
import org.gauntlet.lessons.api.model.LessonStatus;
import org.gauntlet.lessons.api.model.LessonType;
import org.gauntlet.lessons.api.model.UserLesson;
import org.gauntlet.lessons.api.model.UserLessonPlan;
import org.gauntlet.lessons.model.jpa.JPALesson;
import org.gauntlet.lessons.model.jpa.JPALessonProblem;
import org.gauntlet.lessons.model.jpa.JPALessonStatus;
import org.gauntlet.lessons.model.jpa.JPALessonType;
import org.gauntlet.lessons.model.jpa.JPAUserLesson;
import org.gauntlet.lessons.model.jpa.JPAUserLessonPlan;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;


@SuppressWarnings("restriction")
@Transactional
public class LessonDAOImpl extends BaseServiceImpl implements ILessonsDAOService {
	//Lessonzes
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
	public List<Lesson> findAll() throws ApplicationException {
		List<Lesson> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPALesson.class);
			result = JPAEntityUtil.copy(resultList, Lesson.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public List<Lesson> findAll(int start, int end) throws ApplicationException {
		List<Lesson> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPALesson.class,start,end);
			result = JPAEntityUtil.copy(resultList, Lesson.class);
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
			res = super.countAll(JPALesson.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	
	
	@Override
	public Lesson getByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		Lesson res = null;
		try {
			JPALesson jpaEntity = (JPALesson) super.findByPrimaryKey(JPALesson.class, pk);
			res = JPAEntityUtil.copy(jpaEntity, Lesson.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public Lesson provide(Lesson record)
			  throws ApplicationException {
		Lesson recordEntity = getByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPALesson copiedEntity = JPAEntityUtil.copy(record, JPALesson.class);
			copiedEntity.getQuestions().stream()
				.forEach(qp -> {
					qp.setLesson(copiedEntity);
				});
			JPABaseEntity res = super.add(copiedEntity);
			
			recordEntity = JPAEntityUtil.copy(res, Lesson.class);
		}

		return recordEntity;
	}
	
	@Override
	public Lesson update(Lesson record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPALesson.class));
		Lesson dto = JPAEntityUtil.copy(res, Lesson.class);
		return dto;	
	}	
	
	@Override
	public Lesson delete(Long id) throws ApplicationException, NoSuchModelException {
		JPALesson jpaEntity = (JPALesson) super.findByPrimaryKey(JPALesson.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, Lesson.class);
	}
	
	@Override
	public Lesson getByCode(String code) throws ApplicationException {
		JPALesson jpaEntity = (JPALesson) super.findWithAttribute(JPALesson.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, Lesson.class);
	}


	@Override
	public Lesson getByName(String name) throws ApplicationException {
		JPALesson jpaEntity = (JPALesson) super.findWithAttribute(JPALesson.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, Lesson.class);
	}
	
	
	//UserLesson
	@Override 
	public List<UserLesson> findAllUserLessons(User user) throws ApplicationException {
		try {
			List<JPABaseEntity> resultList = super.findAllWithAttribute(JPAUserLesson.class, Long.class,"userId", user.getId());
			return JPAEntityUtil.copy(resultList, UserLesson.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
	}
	
	@Override 
	public List<UserLesson> findAllUserLessonsByType(User user, Long typeId) throws ApplicationException {
		List<UserLesson> result = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			ParameterExpression<Long> pType = builder.parameter(Long.class);
			
			ParameterExpression<Long> pUser = builder.parameter(Long.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<JPAUserLesson> cq = qb.createQuery(JPAUserLesson.class);
			Root<JPAUserLesson> rootEntity = cq.from(JPAUserLesson.class);
			cq.select(rootEntity).where(builder.and(
					builder.equal(rootEntity.get("userId"),pUser),
					builder.equal(rootEntity.get("lessonType").get("id"),pType)
					));
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pType, typeId);
			typedQuery.setParameter(pUser, user.getId());
			
			
			List<JPAUserLesson> resultList = typedQuery.getResultList();
			result = JPAEntityUtil.copy(resultList, UserLesson.class);		
		}
		catch (Exception e) {
			throw processException(e);
		}
		
		return result;
	}
	
	@Override 
	public UserLesson findUserLessonByType(User user, Long typeId) throws ApplicationException {
		UserLesson result = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			
			ParameterExpression<Long> pType = builder.parameter(Long.class);
			
			ParameterExpression<Long> pUser = builder.parameter(Long.class);
			
			CriteriaBuilder qb =  getEm().getCriteriaBuilder();
			CriteriaQuery<JPAUserLesson> cq = qb.createQuery(JPAUserLesson.class);
			Root<JPAUserLesson> rootEntity = cq.from(JPAUserLesson.class);
			cq.select(rootEntity).where(builder.and(
					builder.equal(rootEntity.get("userId"),pUser),
					builder.equal(rootEntity.get("lessonType").get("id"),pType)
					));
			
			TypedQuery typedQuery = getEm().createQuery(cq);
			typedQuery.setParameter(pType, typeId);
			typedQuery.setParameter(pUser, user.getId());
			
			
			JPAUserLesson resultList = (JPAUserLesson) typedQuery.getSingleResult();
			result = JPAEntityUtil.copy(resultList, UserLesson.class);		
		}
		catch (Exception e) {
			throw processException(e);
		}
		
		return result;
	}
	
	@Override
	public long countAllUserLessons(User user) throws ApplicationException {
		long res = 0;
		try {
			res = super.countWithAttribute(JPAUserLesson.class,Long.class,"userId",user.getId());
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public UserLesson getUserLessonByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAUserLesson jpaEntity = (JPAUserLesson) super.findByPrimaryKey(JPAUserLesson.class, pk);
		return JPAEntityUtil.copy(jpaEntity, UserLesson.class);
	}
	
	@Override 
	public UserLesson provideUserLesson(UserLesson record) throws ApplicationException {
		UserLesson recordEntity = getUserLessonByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAUserLesson.class));
			recordEntity = JPAEntityUtil.copy(res, UserLesson.class);
		}

		return recordEntity;	
	}
	
	
	@Override
	public UserLesson updateUserLesson(UserLesson record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAUserLesson.class));
		UserLesson dto = JPAEntityUtil.copy(res, UserLesson.class);
		return dto;	
	}	
	
	@Override
	public UserLesson getUserLessonByCode(String code) throws ApplicationException{
		JPAUserLesson jpaEntity = (JPAUserLesson) super.findWithAttribute(JPAUserLesson.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, UserLesson.class);		
	}
	
	@Override
	public UserLesson getUserLessonByName(String name) throws ApplicationException{
		JPAUserLesson jpaEntity = (JPAUserLesson) super.findWithAttribute(JPAUserLesson.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, UserLesson.class);		
	}
	
	
	public UserLesson deleteUserLesson(Long id) throws ApplicationException, NoSuchModelException {
		JPAUserLesson jpaEntity = (JPAUserLesson) super.findByPrimaryKey(JPAUserLesson.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, UserLesson.class);
	}	

	@Override
	public void createDefaults() throws ApplicationException {
	}	
	
	//LessonProblem
	@Override 
	public LessonProblem provideLessonProblem(LessonProblem record) throws ApplicationException {
		LessonProblem recordEntity = getLessonProblemByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPALessonProblem.class));
			recordEntity = JPAEntityUtil.copy(res, LessonProblem.class);
		}

		return recordEntity;	
	}
	
	
	public LessonProblem getLessonProblemByCode(String code) throws ApplicationException{
		JPALessonProblem jpaEntity = (JPALessonProblem) super.findWithAttribute(JPALessonProblem.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, LessonProblem.class);		
	}
	

	
	@Override
	public LessonProblem deleteLessonProblem(Long id) throws ApplicationException, NoSuchModelException {
		JPALessonProblem jpaEntity = (JPALessonProblem) super.findByPrimaryKey(JPALessonProblem.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, LessonProblem.class);
	}
	
	//LessonType
	@Override 
	public int countByLessonTypeCode(User user, String typeCode) throws ApplicationException {
		int count = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAUserLesson> query = builder.createQuery(JPAUserLesson.class);
			Root<JPAUserLesson> rootEntity = query.from(JPAUserLesson.class);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			
			ParameterExpression<String> p = builder.parameter(String.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("lessonType").get("code"),p));
			pes.put(p, typeCode);
			
			//userId
			ParameterExpression<Long> pl = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("userId"),pl));
			pes.put(pl, user.getId());
			
			count = countWithDynamicQueryAndParams(query,pes);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return count;		
	}	
	
	@Override 
	public List<UserLesson>  findAllUserLessonsByLessonType(User user, Long typeId) throws ApplicationException {
		List<UserLesson> result = new ArrayList<>();
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAUserLesson> query = builder.createQuery(JPAUserLesson.class);
			Root<JPAUserLesson> rootEntity = query.from(JPAUserLesson.class);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("lessonType").get("id"),p));
			pes.put(p, typeId);
			
			//userId
			ParameterExpression<Long> pl = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("userId"),pl));
			pes.put(pl, user.getId());
			
			List resultList = super.findWithDynamicQueryAndParams(query,pes);
			result = JPAEntityUtil.copy(resultList, UserLesson.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}	
	
	@Override 
	public int countByLessonType(Long typeId) throws ApplicationException {
		int count = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAUserLesson> query = builder.createQuery(JPAUserLesson.class);
			Root<JPAUserLesson> rootEntity = query.from(JPAUserLesson.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.gt(rootEntity.get("lessonType").get("id"),p));
			query.select(rootEntity);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			pes.put(p, typeId);
			
			count = countWithDynamicQueryAndParams(query,pes);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return count;		
	}	
	
	@Override 
	public List<LessonType> findAllLessonTypes(int start, int end) throws ApplicationException {
		List<LessonType> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPALessonType.class,start,end);
			result = JPAEntityUtil.copy(resultList, LessonType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllLessonTypes() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPALessonType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public LessonType getLessonTypeByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPALessonType jpaEntity = (JPALessonType) super.findByPrimaryKey(JPALessonType.class, pk);
		return JPAEntityUtil.copy(jpaEntity, LessonType.class);
	}
	
	@Override 
	public LessonType provideLessonType(LessonType record) throws ApplicationException {
		LessonType recordEntity = getLessonTypeByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPALessonType.class));
			recordEntity = JPAEntityUtil.copy(res, LessonType.class);
		}

		return recordEntity;	
	}
	
	public LessonType updateLessonType(JPALessonType record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPALessonType.class));
		LessonType dto = JPAEntityUtil.copy(res, LessonType.class);
		return dto;	
	}	
	
	@Override
	public LessonType getLessonTypeByCode(String code) throws ApplicationException{
		JPALessonType jpaEntity = getLessonTypeByCode_(code);
		return JPAEntityUtil.copy(jpaEntity, LessonType.class);		
	}
	
	private JPALessonType getLessonTypeByCode_(String code) throws ApplicationException{
		return (JPALessonType) super.findWithAttribute(JPALessonType.class, String.class,"code", code);
	}
	
	@Override
	public LessonType getLessonTypeByName(String code) throws ApplicationException{
		JPALessonType jpaEntity = (JPALessonType) super.findWithAttribute(JPALessonType.class, String.class,"name", code);
		return JPAEntityUtil.copy(jpaEntity, LessonType.class);		
	}
	
	@Override
	public LessonType deleteLessonType(Long id) throws ApplicationException, NoSuchModelException {
		JPALessonType jpaEntity = (JPALessonType) super.findByPrimaryKey(JPALessonType.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, LessonType.class);
	}	
	
	//LessonStatus
	@Override 
	public int countByLessonStatusCode(User user, String statusCode) throws ApplicationException {
		int count = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAUserLesson> query = builder.createQuery(JPAUserLesson.class);
			Root<JPAUserLesson> rootEntity = query.from(JPAUserLesson.class);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			
			ParameterExpression<String> p = builder.parameter(String.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("lessonStatus").get("code"),p));
			pes.put(p, statusCode);
			
			//userId
			ParameterExpression<Long> pl = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("userId"),pl));
			pes.put(pl, user.getId());
			
			count = countWithDynamicQueryAndParams(query,pes);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return count;		
	}	
	
	@Override 
	public int countByLessonStatus(Long typeId) throws ApplicationException {
		int count = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAUserLesson> query = builder.createQuery(JPAUserLesson.class);
			Root<JPAUserLesson> rootEntity = query.from(JPAUserLesson.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.gt(rootEntity.get("lessonStatus").get("id"),p));
			query.select(rootEntity);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			pes.put(p, typeId);
			
			count = countWithDynamicQueryAndParams(query,pes);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return count;		
	}	
	
	@Override 
	public List<UserLesson>  findAllUserLessonsByLessonStatus(User user, Long statusId) throws ApplicationException {
		List<UserLesson> result = new ArrayList<>();
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAUserLesson> query = builder.createQuery(JPAUserLesson.class);
			Root<JPAUserLesson> rootEntity = query.from(JPAUserLesson.class);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("lessonStatus").get("id"),p));
			pes.put(p, statusId);
			
			//userId
			ParameterExpression<Long> pl = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("userId"),pl));
			pes.put(pl, user.getId());
			
			List resultList = super.findWithDynamicQueryAndParams(query,pes);
			result = JPAEntityUtil.copy(resultList, UserLesson.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}	
	
	@Override 
	public List<LessonStatus> findAllLessonStatuses(int start, int end) throws ApplicationException {
		List<LessonStatus> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPALessonStatus.class,start,end);
			result = JPAEntityUtil.copy(resultList, LessonStatus.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllLessonStatuses() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPALessonStatus.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public LessonStatus getLessonStatusByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPALessonStatus jpaEntity = (JPALessonStatus) super.findByPrimaryKey(JPALessonStatus.class, pk);
		return JPAEntityUtil.copy(jpaEntity, LessonStatus.class);
	}
	
	@Override 
	public LessonStatus provideLessonStatus(LessonStatus record) throws ApplicationException {
		LessonStatus recordEntity = getLessonStatusByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPALessonStatus.class));
			recordEntity = JPAEntityUtil.copy(res, LessonStatus.class);
		}

		return recordEntity;	
	}
	
	public LessonStatus updateLessonStatus(JPALessonStatus record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPALessonStatus.class));
		LessonStatus dto = JPAEntityUtil.copy(res, LessonStatus.class);
		return dto;	
	}	
	
	@Override
	public LessonStatus getLessonStatusByCode(String code) throws ApplicationException{
		JPALessonStatus jpaEntity = getLessonStatusByCode_(code);
		return JPAEntityUtil.copy(jpaEntity, LessonStatus.class);		
	}
	
	public JPALessonStatus getLessonStatusByCode_(String code) throws ApplicationException{
		return (JPALessonStatus) super.findWithAttribute(JPALessonStatus.class, String.class,"code", code);
	}
	
	@Override
	public LessonStatus getLessonStatusByName(String code) throws ApplicationException{
		JPALessonStatus jpaEntity = (JPALessonStatus) super.findWithAttribute(JPALessonStatus.class, String.class,"name", code);
		return JPAEntityUtil.copy(jpaEntity, LessonStatus.class);		
	}
	
	@Override
	public LessonStatus deleteLessonStatus(Long id) throws ApplicationException, NoSuchModelException {
		JPALessonStatus jpaEntity = (JPALessonStatus) super.findByPrimaryKey(JPALessonStatus.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, LessonStatus.class);
	}	
	
	//UserLessonPlan
	@Override 
	public List<UserLessonPlan> findAllUserLessonPlans(User user) throws ApplicationException {
		try {
			List<JPAUserLessonPlan> resultList =  (List<JPAUserLessonPlan>) super.findWithAttribute(JPAUserLessonPlan.class, Long.class,"userId", user.getId());
			return JPAEntityUtil.copy(resultList, UserLessonPlan.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
	}
	
	@Override 
	public List<UserLessonPlan> findAllUserLessonPlans() throws ApplicationException {
		try {
			List<JPABaseEntity> resultList =  super.findAll(JPAUserLessonPlan.class);
			return JPAEntityUtil.copy(resultList, UserLessonPlan.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
	}
	
	@Override
	public long countAllUserLessonPlans(User user) throws ApplicationException {
		long res = 0;
		try {
			res = super.countWithAttribute(JPAUserLessonPlan.class,Long.class,"userId",user.getId());
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public UserLessonPlan getUserLessonPlanByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAUserLessonPlan jpaEntity = (JPAUserLessonPlan) super.findByPrimaryKey(JPAUserLessonPlan.class, pk);
		return JPAEntityUtil.copy(jpaEntity, UserLessonPlan.class);
	}
	
	@Override 
	public UserLessonPlan provideUserLessonPlan(UserLessonPlan record) throws ApplicationException {
		UserLessonPlan recordEntity = getUserLessonPlanByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAUserLessonPlan.class));
			recordEntity = JPAEntityUtil.copy(res, UserLessonPlan.class);
		}

		return recordEntity;	
	}
	
	@Override 
	public UserLesson provideLessonAsCurrentToPlan(UserLesson userLesson, Long planPk) throws ApplicationException, NoSuchModelException {
		JPAUserLessonPlan planEntity = (JPAUserLessonPlan) super.findByPrimaryKey(JPAUserLessonPlan.class, planPk);
		
		JPAUserLesson ulEntity = (JPAUserLesson) super.add(JPAEntityUtil.copy(userLesson, JPAUserLesson.class));
		ulEntity.setPlan(planEntity);
		
		ulEntity.setLessonFinished(false);
		ulEntity.setLessonType(getLessonTypeByCode_(org.gauntlet.lessons.api.model.Constants.LESSON_TYPE_CURRENT));
		ulEntity.setLessonStatus(getLessonStatusByCode_(org.gauntlet.lessons.api.model.Constants.LESSON_STATUS_NEW));

		planEntity.setCurrentLesson(ulEntity);
		
		
		return JPAEntityUtil.copy(planEntity.getCurrentLesson(), UserLesson.class);	
	}

	@Override 
	public UserLesson provideLessonAsUpcomingToPlan(UserLesson userLesson, Long planPk) throws ApplicationException, NoSuchModelException {
		JPAUserLessonPlan planEntity = (JPAUserLessonPlan) super.findByPrimaryKey(JPAUserLessonPlan.class, planPk);
		JPAUserLesson ulEntity = (JPAUserLesson) super.add(JPAEntityUtil.copy(userLesson, JPAUserLesson.class));

		planEntity.getUpcomingLessons().add(ulEntity);
		
		updateUserLessonPlan_(planEntity);
		
		return JPAEntityUtil.copy(planEntity.getCurrentLesson(), UserLesson.class);	
	}	
	
	@Override
	public UserLessonPlan updateUserLessonPlan(UserLessonPlan record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAUserLessonPlan.class));
		UserLessonPlan dto = JPAEntityUtil.copy(res, UserLessonPlan.class);
		return dto;	
	}	
	
	public JPAUserLessonPlan updateUserLessonPlan_(JPAUserLessonPlan record) throws ApplicationException {
		return (JPAUserLessonPlan) super.update(record);
	}	
	
	@Override
	public UserLessonPlan getUserLessonPlanByCode(String code) throws ApplicationException{
		JPAUserLessonPlan jpaEntity = (JPAUserLessonPlan) super.findWithAttribute(JPAUserLessonPlan.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, UserLessonPlan.class);		
	}
	
	@Override
	public UserLessonPlan getUserLessonPlanByName(String name) throws ApplicationException{
		JPAUserLessonPlan jpaEntity = (JPAUserLessonPlan) super.findWithAttribute(JPAUserLessonPlan.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, UserLessonPlan.class);		
	}
	
	@Override
	public UserLessonPlan getUserLessonPlanByUserPk(Long userPk) throws ApplicationException{
		JPAUserLessonPlan jpaEntity = (JPAUserLessonPlan) super.findWithAttribute(JPAUserLessonPlan.class, Long.class,"userId", userPk);
		return JPAEntityUtil.copy(jpaEntity, UserLessonPlan.class);		
	}
	
	
	public UserLessonPlan deleteUserLessonPlan(Long id) throws ApplicationException, NoSuchModelException {
		JPAUserLessonPlan jpaEntity = (JPAUserLessonPlan) super.findByPrimaryKey(JPAUserLessonPlan.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, UserLessonPlan.class);
	}	
	
	@Override
	public void resetUserLessonPlan(User user) throws ApplicationException, NoSuchModelException  {
		JPAUserLessonPlan jpaEntity = (JPAUserLessonPlan) super.findWithAttribute(JPAUserLessonPlan.class, Long.class,"userId", user.getId());
		
		jpaEntity.setCurrentLesson(null);
		jpaEntity.getUpcomingLessons().stream()
			.forEach(l -> {
				try {
					l.setPlan(null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		
		jpaEntity.getUpcomingLessons().clear();
		
		super.update(jpaEntity);
	}
}