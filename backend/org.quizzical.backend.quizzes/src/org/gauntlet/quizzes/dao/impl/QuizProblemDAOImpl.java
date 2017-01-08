package org.gauntlet.quizzes.dao.impl;

import javax.persistence.EntityManager;

import org.amdatu.jta.Transactional;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.model.jpa.JPAQuizProblem;
import org.osgi.service.log.LogService;

@Transactional
public class QuizProblemDAOImpl extends BaseServiceImpl implements IQuizProblemDAOService {
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
	public QuizProblem getByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		final JPAQuizProblem jpaEntity = (JPAQuizProblem) super.findByPrimaryKey(JPAQuizProblem.class, pk);
		return JPAEntityUtil.copy(jpaEntity, QuizProblem.class);
	}

	@Override
	public void createDefaults() throws ApplicationException {}
}