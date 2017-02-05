package org.gauntlet.quizzes.dao.impl;

import java.util.ArrayList;
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
import org.gauntlet.core.model.JPABaseEntity;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.gauntlet.quizzes.api.dao.IQuizProblemResponseDAOService;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;
import org.gauntlet.quizzes.model.jpa.JPAQuizProblem;
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
	public List<Long> getAllUserSkippedOrIncorrectProblemIds(User user, Integer limit) throws ApplicationException {
		List<Long> res;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAQuizProblemResponse> query = builder.createQuery(JPAQuizProblemResponse.class);
			Root<JPAQuizProblemResponse> rootEntity = query.from(JPAQuizProblemResponse.class);
			
			final Map<ParameterExpression,Object> pes = new HashMap<>();
			
			//userId
			ParameterExpression<Boolean> pbool = builder.parameter(Boolean.class);
			ParameterExpression<Long> pid = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.and(
					builder.equal(rootEntity.get("submission").get("quiz").get("userId"),pid),
					builder.or(builder.equal(rootEntity.get("correct"),pbool),builder.equal(rootEntity.get("correct"),pbool))
					));
			pes.put(pid, user.getId());
			pes.put(pbool, true);
			
			List<JPAQuizProblem> resultList = findWithDynamicQueryAndParams(query,pes,0,limit);
			
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