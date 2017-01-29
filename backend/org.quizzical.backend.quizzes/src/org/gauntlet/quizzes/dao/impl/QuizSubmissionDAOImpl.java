package org.gauntlet.quizzes.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.amdatu.jta.Transactional;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.commons.util.Validator;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.gauntlet.core.model.JPABaseEntity;
import org.gauntlet.core.service.impl.AttrPair;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemResponseDAOService;
import org.gauntlet.quizzes.api.dao.IQuizScoringService;
import org.gauntlet.quizzes.api.dao.IQuizSubmissionDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;
import org.gauntlet.quizzes.api.model.QuizProblemType;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.gauntlet.quizzes.model.jpa.JPAQuizProblem;
import org.gauntlet.quizzes.model.jpa.JPAQuizProblemResponse;
import org.gauntlet.quizzes.model.jpa.JPAQuizSubmission;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.model.user.User;


@Transactional
public class QuizSubmissionDAOImpl extends BaseServiceImpl implements IQuizSubmissionDAOService {
	
	private volatile IQuizDAOService quizService;
	
	private volatile IQuizProblemDAOService quizProblemService;
	
	private volatile IProblemDAOService problemService;
	
	private volatile IQuizScoringService scoringService;
	
	@SuppressWarnings("unused")
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
	public List<QuizSubmission> findAll(User user, int start, int end) throws ApplicationException {
		List<QuizSubmission> result = new ArrayList<>();
		try {
			Set<AttrPair> attrs = new HashSet<>();
			attrs.add(new AttrPair(Long.class, "userId", user.getId()));
			
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
			JPAQuizSubmission newEntity = toJPAEntity(record);
			newEntity = (JPAQuizSubmission) super.merge(newEntity);
			
			QuizSubmission newDTO = toDTO(newEntity);
			return newDTO;
		}

		return existingDTO;
	}
	
	private QuizSubmission toDTO(JPAQuizSubmission newEntity) {
		QuizSubmission dto = JPAEntityUtil.copy(newEntity, QuizSubmission.class);
	   	final List<QuizProblemResponse> augmentedResponses = dto.getResponses()
	    		.parallelStream()
	    		.map(problemResponse -> {
					QuizProblem qp = null;
					try {
						qp = toQuizProblemDTO(problemResponse.getQuizProblemId());
					} 
					catch (NoSuchModelException e) {
						throw new IllegalArgumentException("Failed to find problem for provided quiz problem id.", e);					
					}
					catch (final ApplicationException e) {
						throw new RuntimeException(e);
					}
					problemResponse.setQuizProblem(qp);
					
					return problemResponse;
		    	})
	    		.collect(Collectors.toList());
	   	dto.setResponses(augmentedResponses);
		return dto;
	}
	
	public QuizProblem toQuizProblemDTO(Long pk) throws ApplicationException, NoSuchModelException {
		final JPAQuizProblem jpaEntity = (JPAQuizProblem) super.findByPrimaryKey(JPAQuizProblem.class, pk);
		final QuizProblem dto = JPAEntityUtil.copy(jpaEntity, QuizProblem.class);
		final Problem problem = problemService.getByPrimary(dto.getProblemId());
		dto.setProblem(problem);
		return dto;
	}

	private JPAQuizSubmission toJPAEntity(QuizSubmission quizSubmission) {
		final JPAQuizSubmission jpaQuizSubmission = JPAEntityUtil.copy(quizSubmission, JPAQuizSubmission.class);
    	final List<JPAQuizProblemResponse> responses = quizSubmission.getResponses()
    		.parallelStream()
    		.map(problemResponse -> {
	    		JPAQuizProblemResponse jpaEntity = null;
				try {
					final JPAQuizProblem jpaQuizProblem = (JPAQuizProblem) super.findByPrimaryKey(JPAQuizProblem.class, problemResponse.getQuizProblemId());
					jpaEntity = new JPAQuizProblemResponse(
							problemResponse.getResponse(),
							problemResponse.getCorrect(),
							problemResponse.getSkipped(),
							problemResponse.getSecondsElapsed(),
							jpaQuizProblem.getId());
					return jpaEntity;
				} 
				catch (NoSuchModelException e) {
					throw new IllegalArgumentException("Failed to find problem for provided quiz problem id.", e);					
				}
				catch (final ApplicationException e) {
					throw new RuntimeException(e);
				}
	    	})
    		.filter(pr -> {
    			JPAQuizProblem jpaQuizProblem = null;
				try {
					jpaQuizProblem = (JPAQuizProblem) super.findByPrimaryKey(JPAQuizProblem.class, pr.getQuizProblemId());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
    			return jpaQuizProblem.getType() == QuizProblemType.REGULAR;
    		})
    		.collect(Collectors.toList());
    	
    	jpaQuizSubmission.setResponses(responses);
		
		return jpaQuizSubmission;
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
		quizSubmission = scoringService.score(quizSubmission);
    	
    	//--Persist
    	quizSubmission = add(quizSubmission);
    	
    	//--Prep for UI
       	final List<QuizProblemResponse> correctedResponses = quizSubmission.getResponses()
        		.parallelStream()
        		.map(problemResponse -> {
    				problemResponse.getQuizProblem().setOrdinal(problemResponse.getQuizProblem().getQuizOrdinal());
    				return problemResponse;
    	    	})
        		.collect(Collectors.toList());
       	quizSubmission.setResponses(correctedResponses);
       	
    	return quizSubmission;
	}
	
	
}