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
import org.gauntlet.quizzes.api.model.QuizTake;
import org.gauntlet.quizzes.model.jpa.JPAQuizProblemAnswer;
import org.gauntlet.quizzes.model.jpa.JPAQuizTake;


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
	public List<QuizTake> findAll(int start, int end) throws ApplicationException {
		List<QuizTake> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPAQuizTake.class,start,end);
			result = JPAEntityUtil.copy(resultList, QuizTake.class);
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
			res = super.countAll(JPAQuizTake.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public QuizTake getByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAQuizTake jpaEntity = (JPAQuizTake) super.findByPrimaryKey(JPAQuizTake.class, pk);
		return JPAEntityUtil.copy(jpaEntity, QuizTake.class);
	}

	@Override
	public QuizTake add(QuizTake record)
			  throws ApplicationException {
		QuizTake existingCountry = getByCode(record.getCode());
		if (Validator.isNull(existingCountry))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAQuizTake.class));
			existingCountry = JPAEntityUtil.copy(res, QuizTake.class);
		}

		return existingCountry;
	}
	
	@Override
	public QuizTake update(QuizTake record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAQuizTake.class));
		QuizTake dto = JPAEntityUtil.copy(res, QuizTake.class);
		return dto;	
	}	
	
	@Override
	public QuizTake delete(Long id) throws ApplicationException, NoSuchModelException {
		JPAQuizTake jpaEntity = (JPAQuizTake) super.findByPrimaryKey(JPAQuizTake.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, QuizTake.class);
	}
	
	@Override
	public QuizTake getByCode(String code) throws ApplicationException {
		JPAQuizTake jpaEntity = (JPAQuizTake) super.findWithAttribute(JPAQuizTake.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, QuizTake.class);
	}


	@Override
	public QuizTake getByName(String name) throws ApplicationException {
		JPAQuizTake jpaEntity = (JPAQuizTake) super.findWithAttribute(JPAQuizTake.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, QuizTake.class);
	}

	@Override
	public void createDefaults() throws ApplicationException {
		// TODO Auto-generated method stub
	}

	@Override
	public QuizTake addAnswer(Long quizTakeId, QuizProblemAnswer answer) throws NoSuchModelException, ApplicationException {
		JPAQuizTake jpaEntity = (JPAQuizTake) super.findByPrimaryKey(JPAQuizTake.class, quizTakeId);
		JPAQuizProblemAnswer jpaProblemAnswer = JPAEntityUtil.copy(answer, JPAQuizProblemAnswer.class);
		jpaEntity.getAnswers().add(jpaProblemAnswer);
		
		jpaEntity = (JPAQuizTake) update(jpaEntity);
		
		return JPAEntityUtil.copy(jpaEntity, QuizTake.class);
	}
	
	public QuizTake addAnswers(Long quizTakeId, List<QuizProblemAnswer> answers) throws NoSuchModelException, ApplicationException {
		JPAQuizTake jpaEntity = (JPAQuizTake) super.findByPrimaryKey(JPAQuizTake.class, quizTakeId);
		List<JPAQuizProblemAnswer> jpaProblemAnswers = JPAEntityUtil.copy(answers, JPAQuizProblemAnswer.class);
		jpaEntity.getAnswers().addAll(jpaProblemAnswers);
		
		jpaEntity = (JPAQuizTake) update(jpaEntity);
		
		return JPAEntityUtil.copy(jpaEntity, QuizTake.class);
	}
}