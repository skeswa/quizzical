package org.gauntlet.quizzes.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.amdatu.jta.Transactional;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.model.jpa.JPAQuizProblem;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;

@Transactional
public class QuizProblemDAOImpl extends BaseServiceImpl implements IQuizProblemDAOService {
	//Quizzes
	private volatile LogService logger;
	
	private volatile EntityManager em;
	
	private volatile IProblemDAOService problemDAOService;
	
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
		final QuizProblem dto = JPAEntityUtil.copy(jpaEntity, QuizProblem.class);
		final Problem pdto = problemDAOService.getByPrimary(dto.getProblemId());
		dto.setProblem(pdto);
		return dto;
	}
	
	
	@Override
	public List<Long> getAllUserProblemIds(User user) throws ApplicationException {
		List<Long> res;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAQuizProblem> query = builder.createQuery(JPAQuizProblem.class);
			Root<JPAQuizProblem> rootEntity = query.from(JPAQuizProblem.class);
			
			final Map<ParameterExpression,Object> pes = new HashMap<>();
			
			//userId
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("quiz").get("userId"),p));
			pes.put(p, user.getId());
			
			List<JPAQuizProblem> resultList = findWithDynamicQueryAndParams(query,pes);
			
			res = resultList.stream()
				.map(qp -> qp.getProblemId())
				.collect(Collectors.toList());
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;			
	}

	@Override
	public void createDefaults() throws ApplicationException {}
}