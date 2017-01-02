package org.gauntlet.quizzes.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import org.amdatu.jta.Transactional;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.commons.util.Validator;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.gauntlet.core.model.JPABaseEntity;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.osgi.service.log.LogService;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemResponseDAOService;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.gauntlet.quizzes.model.jpa.JPAQuizSubmission;


@Transactional
public class QuizSubmissionDAOImpl extends BaseServiceImpl implements IQuizSubmissionDAOService {
	
	@SuppressWarnings("unused")
	private volatile IQuizDAOService quizService;
	
	private volatile IQuizProblemDAOService quizProblemService;
	
	private volatile IProblemDAOService problemService;
	
	private volatile IQuizProblemResponseDAOService quizProblemResponseService;
	
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
		final QuizSubmission existingDTO = getByCode(record.getCode());
		
		if (Validator.isNull(existingDTO)) {
			final JPAQuizSubmission newEntity = JPAEntityUtil.copy(record, JPAQuizSubmission.class);
			super.add(newEntity);
			final QuizSubmission newDTO = JPAEntityUtil.copy(newEntity, QuizSubmission.class);
			return newDTO;
		}

		return existingDTO;
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
	public QuizSubmission submit(QuizSubmission quizSubmission) throws ApplicationException, NoSuchModelException {
	   	final Long quizId = quizSubmission.getQuizId();
    	final Quiz quiz = quizService.getByPrimary(quizId);

    	quizSubmission.setQuiz(quiz);
    	quizSubmission.setCode(String.format("%s-%d", quizId, System.currentTimeMillis()));
    	
    	// Evaluate the which quiz problem responses are correct.
    	final List<QuizProblemResponse> augmentedResponses = quizSubmission.getResponses()
    		.parallelStream()
    		.map(problemResponse -> {
	    		final Long quizProblemId = problemResponse.getQuizProblemId();
				try {
					final QuizProblem quizProblem = quizProblemService.getByPrimary(quizProblemId);
					@SuppressWarnings("unused")
					final Problem problem = problemService.getByPrimary(quizProblem.getProblemId());
					
					final Boolean correct = problemResponse.getResponse().equalsIgnoreCase(problem.getAnswer());
					
					QuizProblemResponse newQuizProblemResponse = new QuizProblemResponse(
							QuizProblemResponse.code(quizProblemId, problemResponse.getResponse()),
							problemResponse.getResponse(),
							problemResponse.getSkipped(),
							correct,
							problemResponse.getSecondsElapsed(),
							quizProblemId);
					newQuizProblemResponse.setCorrect(correct);

					//newQuizProblemResponse = quizProblemResponseService.add(newQuizProblemResponse);
					
					return newQuizProblemResponse;
				} catch (final NoSuchModelException e) {
					throw new IllegalArgumentException("Failed to find problem for provided quiz problem id.", e);
				} catch (final ApplicationException e) {
					throw new RuntimeException(e);
				}
	    	})
    		.collect(Collectors.toList());
    	
    	quizSubmission.setResponses(augmentedResponses);
    	
    	return add(quizSubmission);
	}
	
	
}