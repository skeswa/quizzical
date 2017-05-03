package org.gauntlet.lessons.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.gauntlet.core.service.impl.AttrPair;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.lessons.api.model.Constants;
import org.gauntlet.lessons.api.model.Lesson;
import org.gauntlet.lessons.api.model.LessonProblem;
import org.gauntlet.lessons.api.model.UserLesson;
import org.gauntlet.lessons.api.model.UserLessonPlan;
import org.gauntlet.lessons.model.jpa.JPALesson;
import org.gauntlet.lessons.model.jpa.JPALessonProblem;
import org.gauntlet.lessons.model.jpa.JPAUserLesson;
import org.gauntlet.lessons.model.jpa.JPAUserLessonPlan;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.gauntlet.quizzes.model.jpa.JPAQuizProblem;


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
			List<JPAUserLesson> resultList =  (List<JPAUserLesson>) super.findWithAttribute(JPAUserLesson.class, Long.class,"userId", user.getId());
			return JPAEntityUtil.copy(resultList, UserLesson.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
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
	
	public UserLesson getUserLessonByCode(String code) throws ApplicationException{
		JPAUserLesson jpaEntity = (JPAUserLesson) super.findWithAttribute(JPAUserLesson.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, UserLesson.class);		
	}
	
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
	

	
	public LessonProblem deleteLessonProblem(Long id) throws ApplicationException, NoSuchModelException {
		JPALessonProblem jpaEntity = (JPALessonProblem) super.findByPrimaryKey(JPALessonProblem.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, LessonProblem.class);
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
	
	public UserLessonPlan getUserLessonPlanByCode(String code) throws ApplicationException{
		JPAUserLessonPlan jpaEntity = (JPAUserLessonPlan) super.findWithAttribute(JPAUserLessonPlan.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, UserLessonPlan.class);		
	}
	
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
		jpaEntity.getUpcomingLessons().clear();
		
		super.update(jpaEntity);
	}
}