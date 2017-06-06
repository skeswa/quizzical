package org.gauntlet.quizzes.dao.impl;

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
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.Constants;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizType;
import org.gauntlet.quizzes.model.jpa.JPAQuiz;
import org.gauntlet.quizzes.model.jpa.JPAQuizType;


@SuppressWarnings("restriction")
@Transactional
public class QuizDAOImpl extends BaseServiceImpl implements IQuizDAOService {
	//Quizzes
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
	public List<Quiz> findAll(int start, int end) throws ApplicationException {
		List<Quiz> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPAQuiz.class,start,end);
			result = JPAEntityUtil.copy(resultList, Quiz.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public List<Quiz> findAll(User user, int start, int end) throws ApplicationException {
		List<Quiz> result = new ArrayList<>();
		try {
			Set<AttrPair> attrs = new HashSet<AttrPair>();
			attrs.add(new AttrPair(Long.class, "userId", user.getId()));
			
			List<JPABaseEntity> resultList = findAllWithAttributes(JPAQuiz.class,attrs);
			result = JPAEntityUtil.copy(resultList, Quiz.class);
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
			res = super.countAll(JPAQuiz.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public long countAll(User user) throws ApplicationException {
		long res = 0;
		try {
			Set<AttrPair> attrs = new HashSet<>();
			attrs.add(new AttrPair(Long.class, "userId", user.getId()));
			res = super.countWithAttributes(attrs,JPAQuiz.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}
	
	@Override
	public Quiz getByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		Quiz res = null;
		try {
			JPAQuiz jpaEntity = (JPAQuiz) super.findByPrimaryKey(JPAQuiz.class, pk);
			res = JPAEntityUtil.copy(jpaEntity, Quiz.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public Quiz provide(User user, Quiz record)
			  throws ApplicationException {
		Quiz recordEntity = getByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPAQuiz copiedEntity = JPAEntityUtil.copy(record, JPAQuiz.class);
			if (record.getQuizType() != null) {
				JPAQuizType qt = null;
				try {
					qt = getQuizTypeByPrimary_(record.getQuizType().getId());
				} catch (NoSuchModelException e) {
					throw new ApplicationException(e);
				}
				copiedEntity.setQuizType(qt);
			}
			copiedEntity.getQuestions().stream()
				.forEach(qp -> {
					qp.setQuiz(copiedEntity);
				});
			JPABaseEntity res = super.add(copiedEntity);
			
			recordEntity = JPAEntityUtil.copy(res, Quiz.class);
		}

		return recordEntity;
	}
	
	@Override
	public Quiz update(Quiz record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAQuiz.class));
		Quiz dto = JPAEntityUtil.copy(res, Quiz.class);
		return dto;	
	}	
	
	@Override
	public Quiz delete(Long id) throws ApplicationException, NoSuchModelException {
		JPAQuiz jpaEntity = (JPAQuiz) super.findByPrimaryKey(JPAQuiz.class, id);
		
		//Only remove quiz if not linked to a lesson
		if (!isLessonType(jpaEntity)) {
			super.remove(jpaEntity);
		}
		
		return JPAEntityUtil.copy(jpaEntity, Quiz.class);
	}
	
	@Override
	public Quiz forceDelete(Long id) throws ApplicationException, NoSuchModelException {
		JPAQuiz jpaEntity = (JPAQuiz) super.findByPrimaryKey(JPAQuiz.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, Quiz.class);
	}

	private boolean isLessonType(JPAQuiz jpaEntity) {
		return jpaEntity.getQuizType() != null && jpaEntity.getQuizType().getCode().equals(Constants.QUIZ_TYPE_LESSON_CODE);
	}
	
	@Override
	public Quiz getByCode(String code) throws ApplicationException {
		JPAQuiz jpaEntity = (JPAQuiz) super.findWithAttribute(JPAQuiz.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, Quiz.class);
	}


	@Override
	public Quiz getByName(String name) throws ApplicationException {
		JPAQuiz jpaEntity = (JPAQuiz) super.findWithAttribute(JPAQuiz.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, Quiz.class);
	}
	
	@Override 
	public List<Quiz> findByQuizType(User user, 
			Long typeId, int start, int end) throws ApplicationException {
		List<Quiz> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAQuiz> query = builder.createQuery(JPAQuiz.class);
			Root<JPAQuiz> rootEntity = query.from(JPAQuiz.class);
			
			final Map<ParameterExpression,Object> pes = new HashMap<>();
			
			//quizType
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("quizType").get("id"),p));
			pes.put(p, typeId);
			
			//userId
			p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("userId"),p));
			pes.put(p, user.getId());
			
			resultList = findWithDynamicQueryAndParams(query,pes,start,end);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;		
	}
	
	@Override 
	public List<Quiz> findByUser(User user) throws ApplicationException {
		List<Quiz> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAQuiz> query = builder.createQuery(JPAQuiz.class);
			Root<JPAQuiz> rootEntity = query.from(JPAQuiz.class);
			
			final Map<ParameterExpression,Object> pes = new HashMap<>();
			
			//userId
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("userId"),p));
			pes.put(p, user.getId());
			
			resultList = findWithDynamicQueryAndParams(query,pes);
			return JPAEntityUtil.copy(resultList, Quiz.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
	}
	
	@Override 
	public int countByQuizTypeCode(User user, String typeCode) throws ApplicationException {
		int count = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAQuiz> query = builder.createQuery(JPAQuiz.class);
			Root<JPAQuiz> rootEntity = query.from(JPAQuiz.class);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			
			ParameterExpression<String> p = builder.parameter(String.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("quizType").get("code"),p));
			pes.put(p, typeCode);
			
			//userId
			ParameterExpression<Long> pl = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("userId"),pl));
			pes.put(pl, user.getId());
			
			count = countWithDynamicQueryAndParams(query,pes);
		}
		catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			System.out.println(e.getMessage());
			throw processException(e);
		}
		return count;		
	}	
	
	@Override 
	public int countByQuizType(Long typeId) throws ApplicationException {
		int count = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAQuiz> query = builder.createQuery(JPAQuiz.class);
			Root<JPAQuiz> rootEntity = query.from(JPAQuiz.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.gt(rootEntity.get("quizType").get("id"),p));
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
	
	
	
	//QuizType
	@Override 
	public List<QuizType> findAllQuizTypes(int start, int end) throws ApplicationException {
		List<QuizType> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPAQuizType.class,start,end);
			result = JPAEntityUtil.copy(resultList, QuizType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllQuizTypes() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPAQuizType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public QuizType getQuizTypeByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAQuizType jpaEntity = getQuizTypeByPrimary_(pk);
		return JPAEntityUtil.copy(jpaEntity, QuizType.class);
	}
	
	public JPAQuizType getQuizTypeByPrimary_(Long pk) throws ApplicationException, NoSuchModelException {
		return (JPAQuizType) super.findByPrimaryKey(JPAQuizType.class, pk);
	}
	
	@Override 
	public QuizType provideQuizType(QuizType record) throws ApplicationException {
		QuizType recordEntity = getQuizTypeByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAQuizType.class));
			recordEntity = JPAEntityUtil.copy(res, QuizType.class);
		}

		return recordEntity;	
	}
	
	public QuizType updateQuizType(JPAQuizType record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAQuizType.class));
		QuizType dto = JPAEntityUtil.copy(res, QuizType.class);
		return dto;	
	}	
	
	public QuizType getQuizTypeByCode(String code) throws ApplicationException{
		JPAQuizType jpaEntity = (JPAQuizType) super.findWithAttribute(JPAQuizType.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, QuizType.class);		
	}
	
	public QuizType getQuizTypeByName(String code) throws ApplicationException{
		JPAQuizType jpaEntity = (JPAQuizType) super.findWithAttribute(JPAQuizType.class, String.class,"name", code);
		return JPAEntityUtil.copy(jpaEntity, QuizType.class);		
	}
	
	public QuizType deleteQuizType(Long id) throws ApplicationException, NoSuchModelException {
		JPAQuizType jpaEntity = (JPAQuizType) super.findByPrimaryKey(JPAQuizType.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, QuizType.class);
	}	

	@Override
	public void createDefaults() throws ApplicationException {
	}	
	
	//-- Misc
	@Override
	public void truncate() throws ApplicationException {
		super.truncate("gnt_quiz");
	}

	@Override
	public boolean userHasTakenDiagnoticTest(User user) throws ApplicationException {
		if (user.getRequiresDiagnosticTest() != null && !user.getRequiresDiagnosticTest())
			return true;
		else if (user.getMakeNextRunAPracticeTest()!= null && user.getMakeNextRunAPracticeTest())
			return false;
		
		final int count =countByQuizTypeCode(user, Constants.QUIZ_TYPE_GENERATED_CODE);
		return count > 0;
	}
	
	@Override 
	public List<Quiz> findQuizzesTakenToday(User user) throws ApplicationException {
		List<Quiz> resultList = null;
		try {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime midnight = now.toLocalDate().atStartOfDay();
			Date midnightDate = Date.from(midnight.atZone(ZoneId.systemDefault()).toInstant());
			
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAQuiz> query = builder.createQuery(JPAQuiz.class);
			Root<JPAQuiz> rootEntity = query.from(JPAQuiz.class);
			
			final Map<ParameterExpression,Object> pes = new HashMap<>();
			
			//dateCreated
			ParameterExpression<Date> p = builder.parameter(Date.class);
			query.select(rootEntity).where(builder.greaterThanOrEqualTo(rootEntity.<Date>get("dateCreated"),p));
			pes.put(p, midnightDate);
			
			
			resultList = findWithDynamicQueryAndParams(query,pes);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;		
	}
}