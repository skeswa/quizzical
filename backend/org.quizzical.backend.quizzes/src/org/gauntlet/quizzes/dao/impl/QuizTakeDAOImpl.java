package org.gauntlet.quizzes.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.amdatu.jta.Transactional;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.commons.util.Validator;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.gauntlet.core.model.JPABaseEntity;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.osgi.service.log.LogService;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizTakeDAOService;
import org.gauntlet.quizzes.api.model.QuizProblemAnswer;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.gauntlet.quizzes.model.jpa.JPAQuizProblemAnswer;
import org.gauntlet.quizzes.model.jpa.JPAQuizSubmission;


@SuppressWarnings("restriction")
@Transactional
public class QuizTakeDAOImpl extends BaseServiceImpl implements IQuizTakeDAOService {
	
	private volatile IQuizDAOService quizService;
	
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
	public List<QuizSubmission> findAll(int start, int end) throws ApplicationException {
		List<QuizSubmission> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPAQuizSubmission.class,start,end);
			result = JPAEntityUtil.copy(resultList, QuizSubmission.class);
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
			res = super.countAll(JPAQuizSubmission.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public QuizSubmission getByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAQuizSubmission jpaEntity = (JPAQuizSubmission) super.findByPrimaryKey(JPAQuizSubmission.class, pk);
		return JPAEntityUtil.copy(jpaEntity, QuizSubmission.class);
	}

	@Override
	public QuizSubmission add(QuizSubmission record)
			  throws ApplicationException {
		QuizSubmission existingCountry = getByCode(record.getCode());
		if (Validator.isNull(existingCountry))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAQuizSubmission.class));
			existingCountry = JPAEntityUtil.copy(res, QuizSubmission.class);
		}

		return existingCountry;
	}
	
	@Override
	public QuizSubmission update(QuizSubmission record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAQuizSubmission.class));
		QuizSubmission dto = JPAEntityUtil.copy(res, QuizSubmission.class);
		return dto;	
	}	
	
	@Override
	public QuizSubmission delete(Long id) throws ApplicationException, NoSuchModelException {
		JPAQuizSubmission jpaEntity = (JPAQuizSubmission) super.findByPrimaryKey(JPAQuizSubmission.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, QuizSubmission.class);
	}
	
	@Override
	public QuizSubmission getByCode(String code) throws ApplicationException {
		JPAQuizSubmission jpaEntity = (JPAQuizSubmission) super.findWithAttribute(JPAQuizSubmission.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, QuizSubmission.class);
	}


	@Override
	public QuizSubmission getByName(String name) throws ApplicationException {
		JPAQuizSubmission jpaEntity = (JPAQuizSubmission) super.findWithAttribute(JPAQuizSubmission.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, QuizSubmission.class);
	}

	@Override
	public void createDefaults() throws ApplicationException {
		// TODO Auto-generated method stub
	}

	@Override
	public QuizSubmission addAnswer(Long quizTakeId, QuizProblemAnswer answer) throws NoSuchModelException, ApplicationException {
		JPAQuizSubmission jpaEntity = (JPAQuizSubmission) super.findByPrimaryKey(JPAQuizSubmission.class, quizTakeId);
		JPAQuizProblemAnswer jpaProblemAnswer = JPAEntityUtil.copy(answer, JPAQuizProblemAnswer.class);
		jpaEntity.getAnswers().add(jpaProblemAnswer);
		
		jpaEntity = (JPAQuizSubmission) update(jpaEntity);
		
		return JPAEntityUtil.copy(jpaEntity, QuizSubmission.class);
	}
	
	public QuizSubmission addAnswers(Long quizTakeId, List<QuizProblemAnswer> answers) throws NoSuchModelException, ApplicationException {
		JPAQuizSubmission jpaEntity = (JPAQuizSubmission) super.findByPrimaryKey(JPAQuizSubmission.class, quizTakeId);
		List<JPAQuizProblemAnswer> jpaProblemAnswers = JPAEntityUtil.copy(answers, JPAQuizProblemAnswer.class);
		jpaEntity.getAnswers().addAll(jpaProblemAnswers);
		
		jpaEntity = (JPAQuizSubmission) update(jpaEntity);
		
		return JPAEntityUtil.copy(jpaEntity, QuizSubmission.class);
	}
}