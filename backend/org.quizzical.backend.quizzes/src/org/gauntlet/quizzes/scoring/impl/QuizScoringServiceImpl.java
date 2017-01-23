package org.gauntlet.quizzes.scoring.impl;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.FractionFormat;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.model.user.User;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentSubType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentType;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizScoringService;
import org.gauntlet.quizzes.api.model.Constants;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;
import org.gauntlet.quizzes.api.model.QuizSubmission;

import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.analytics.api.model.TestCategoryAttempt;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;
import org.quizzical.backend.analytics.api.model.TestUserAnalytics;


@SuppressWarnings("restriction")
public class QuizScoringServiceImpl implements IQuizScoringService {
	private volatile LogService logger;
	
	private volatile BundleContext ctx;
	
	private volatile IQuizDAOService quizService;
	
	private volatile IQuizProblemDAOService quizProblemService;
	
	private volatile IProblemDAOService problemService;
	
	private volatile ITestDesignTemplateContentTypeDAOService testDesignTemplateContentTypeDAOService;
	
	private volatile ITestUserAnalyticsDAOService testUserAnalyticsDAOService;


	@Override
	public QuizSubmission score(QuizSubmission quizSubmission) throws ApplicationException, NoSuchModelException {
		final Map<Long,TestCategoryRating> categoryRatingsMap = new ConcurrentHashMap<>();
		
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
					final Problem problem = problemService.getByPrimary(quizProblem.getProblemId());
					
					TestCategoryAttempt attempt = null;
					
					final TestDesignTemplateContentSubType subType = testDesignTemplateContentTypeDAOService.getTestDesignTemplateContentSubTypeByCode(problem.getCategory().getCode());
					TestCategoryRating rating = null;
					if (categoryRatingsMap.containsKey(subType.getId())) {
						rating = categoryRatingsMap.get(subType.getId());
						categoryRatingsMap.put(subType.getId(), rating);
					}
					else {
						final String code = String.format("Rating on Quiz %s Categoryt %s", quiz.getCode(), subType.getCode());
						rating = new TestCategoryRating(subType.getId(), code, code);
						
					}
					
					if (problemResponse.getSkipped())
						attempt = new TestCategoryAttempt(problem.getId(), quizSubmission.getDateCreated(),false,true);
					else {
						final Boolean correct = gradeProblem(problemResponse, problem);
						attempt = new TestCategoryAttempt(problem.getId(), quizSubmission.getDateCreated(),correct,false);
					}
					rating.addAttempt(attempt);
					
					final QuizProblemResponse newQuizProblemResponse = new QuizProblemResponse(
							QuizProblemResponse.code(quizProblemId, problemResponse.getResponse()),
							problemResponse.getResponse(),
							problemResponse.getSkipped(),
							attempt.getSuccessful(),
							problemResponse.getSecondsElapsed(),
							quizProblemId);

					return newQuizProblemResponse;
				} catch (final NoSuchModelException e) {
					throw new IllegalArgumentException("Failed to find problem for provided quiz problem id.", e);
				} catch (final ApplicationException e) {
					throw new RuntimeException(e);
				}
	    	})
    		.collect(Collectors.toList());
    	
    	quizSubmission.setResponses(augmentedResponses);
    	
    	//Process analytics
		final String code_ = String.format("User(%d) analytics", quiz.getUserId()); 
		if (quizSubmission.getQuiz().getQuizType().getCode() == Constants.QUIZ_TYPE_DIAGNOTIC_CODE) {
	    	final TestUserAnalytics tua = new TestUserAnalytics(code_, code_);
			//Baseline across all categories
			final List<TestDesignTemplateContentType> subTypes = testDesignTemplateContentTypeDAOService.findAll();
			TestCategoryRating rating = null;
			for (TestDesignTemplateContentType subType : subTypes) {
				if (!categoryRatingsMap.containsKey(subType.getId())) {
					final String code = String.format("Rating on Quiz %s Categoryt %s", quiz.getCode(), subType.getCode());
					rating = new TestCategoryRating(subType.getId(), code, code);
					rating.setRating(0);
					rating.setAttempts(Collections.emptyList());
				}
				else {//Add to analytics
					rating = categoryRatingsMap.get(subType.getId());
				}
				tua.addRating(rating);
			}
		}
		else {
			final TestUserAnalytics tua = testUserAnalyticsDAOService.getByCode(code_);
			tua.getRatings().stream().map(rating -> {
				try {
					if (categoryRatingsMap.containsKey(rating.getCategoryId())) {
						final TestCategoryRating newRating = categoryRatingsMap.get(rating.getCategoryId());
						rating.getAttempts().addAll(newRating.getAttempts());
						rating.calculateRating();
					}
					return rating;
				} catch (final Exception e) {
					throw new RuntimeException(e);
				} 
	    	});
		}
    	
		return quizSubmission;
	}


	private Boolean gradeProblem(final QuizProblemResponse problemResponse, final Problem problem) {
		if (problemResponse.getSkipped())
			return null;
		
		if (problem.isMultipleChoice()) {
			return problemResponse.getResponse().trim().equalsIgnoreCase(problem.getAnswer());
		}
		else {//Single answer
			Double respDblValue = null;
			Double answerDblValue = null;
			final FractionFormat ff = new FractionFormat();
			try {
				Fraction resp = ff.parse(problemResponse.getResponse().trim());
				respDblValue = resp.doubleValue();
			}
			catch(ParseException e) {
				return false;
			}
			try {
				Fraction resp = ff.parse(problem.getAnswer().trim());
			}
			catch(ParseException e) {
				return false;
			}
			return respDblValue.equals(answerDblValue);
		}
	}
}