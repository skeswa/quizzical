package org.gauntlet.quizzes.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.amdatu.jta.Transactional;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.gauntlet.core.model.JPABaseEntity;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.osgi.service.log.LogService;
import org.gauntlet.quizzes.api.dao.IQuizProblemResponseDAOService;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;
import org.gauntlet.quizzes.model.jpa.JPAQuizProblemResponse;


@SuppressWarnings("restriction")
@Transactional
public class QuizProblemResponseDAOImpl extends BaseServiceImpl implements IQuizProblemResponseDAOService {
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
	public List<QuizProblemResponse> findAll() throws ApplicationException {
		List<QuizProblemResponse> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPAQuizProblemResponse.class);
			result = JPAEntityUtil.copy(resultList, QuizProblemResponse.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}

	@Override
	public QuizProblemResponse add(QuizProblemResponse record)
			  throws ApplicationException {
		JPAQuizProblemResponse jpaRes = (JPAQuizProblemResponse) super.add(JPAEntityUtil.copy(record, JPAQuizProblemResponse.class));
		return JPAEntityUtil.copy(jpaRes, QuizProblemResponse.class);
	}
	
	@Override
	public QuizProblemResponse update(QuizProblemResponse record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAQuizProblemResponse.class));
		QuizProblemResponse dto = JPAEntityUtil.copy(res, QuizProblemResponse.class);
		return dto;	
	}	
	
	@Override
	public QuizProblemResponse delete(Long id) throws ApplicationException, NoSuchModelException {
		JPAQuizProblemResponse jpaEntity = (JPAQuizProblemResponse) super.findByPrimaryKey(JPAQuizProblemResponse.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, QuizProblemResponse.class);
	}
	
	
	@Override
	public void createDefaults() throws ApplicationException {
	}	
}