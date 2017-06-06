package org.gauntlet.quizzes.scoring.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.FractionFormat;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentSubType;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizScoringService;
import org.gauntlet.quizzes.api.model.Constants;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizProblemResponse;
import org.gauntlet.quizzes.api.model.QuizProblemType;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.analytics.api.model.TestCategoryAttempt;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;
import org.quizzical.backend.analytics.api.model.TestUserAnalytics;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;


@SuppressWarnings("restriction")
public class QuizScoringServiceImpl implements IQuizScoringService {
	private volatile LogService logger;
	
	private volatile BundleContext ctx;
	
	private volatile IQuizDAOService quizService;
	
	private volatile IQuizProblemDAOService quizProblemService;
	
	private volatile IProblemDAOService problemService;
	
	private volatile ITestDesignTemplateContentTypeDAOService testDesignTemplateContentTypeDAOService;
	
	private volatile ITestUserAnalyticsDAOService testUserAnalyticsDAOService;
	
	private volatile IUserDAOService userService;


	@Override
	public QuizSubmission score(User user, QuizSubmission quizSubmission, boolean ensureBaline) throws ApplicationException, NoSuchModelException {
		TestUserAnalytics tua = null;
		//Ensure baseline
		if (ensureBaline) 
			tua  = ensureAnalyticsBaseline(user);
		
		
		final Map<Long,TestCategoryRating> categoryRatingsMap = new ConcurrentHashMap<>();
		
	   	final Long quizId = quizSubmission.getQuizId();
    	final Quiz quiz = quizService.getByPrimary(quizId);

    	quizSubmission.setQuiz(quiz);
    	quizSubmission.setCode(String.format("%s-%d", quizId, System.currentTimeMillis()));
    	
    	// Evaluate the which quiz problem responses are correct.
    	final List<QuizProblemResponse> augmentedResponses = quizSubmission.getResponses()
    		.stream()
    		.filter(qpr -> {
    			boolean filter = false;
    			try {
					QuizProblem qp = quizProblemService.getByPrimary(qpr.getQuizProblemId());
					filter = qp.getType() == QuizProblemType.REGULAR;
				} catch (Exception e) {
				}
    			return filter;
    			})
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
					}
					else {
						final String description = String.format("Rating on Quiz %s Category %s", quiz.getCode(), subType.getCode());
						rating = new TestCategoryRating(subType.getId(), subType.getCode(), description);
					}
					categoryRatingsMap.put(subType.getId(), rating);
					
					if (problemResponse.getSkipped())
						attempt = new TestCategoryAttempt(quizProblem.getQuiz().getId(), quizProblemId, quizSubmission.getDateCreated(),false,true,problemResponse.getSecondsElapsed());
					else {
						final Boolean correct = gradeProblem(problemResponse, problem);
						attempt = new TestCategoryAttempt(quizProblem.getQuiz().getId(), quizProblemId, quizSubmission.getDateCreated(),correct,false,problemResponse.getSecondsElapsed());
					}
					rating.addAttempt(attempt);
					
					final QuizProblemResponse newQuizProblemResponse = new QuizProblemResponse(
							QuizProblemResponse.code(quizProblemId, problemResponse.getResponse()),
							problemResponse.getResponse(),
							problemResponse.getSkipped(),
							attempt.getSuccessful(),
							problemResponse.getReported(),
							problemResponse.getSecondsElapsed(),
							quizProblemId);

					return newQuizProblemResponse;
				} catch (final NoSuchModelException e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					String stacktrace = sw.toString();
					System.out.println(e.getMessage());
					throw new IllegalArgumentException("Failed to find problem for provided quiz problem id.", e);
				} catch (final ApplicationException e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					String stacktrace = sw.toString();
					System.out.println(e.getMessage());
					throw new RuntimeException(e);
				} catch (final Exception e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					String stacktrace = sw.toString();
					System.out.println(e.getMessage());
					throw new RuntimeException(e);
				}
	    	})
    		.collect(Collectors.toList());
    	
    	quizSubmission.setResponses(augmentedResponses);
    	
    	//Process analytics
		final String name_ = String.format("User(%d) analytics", quiz.getUserId()); 
		if (quizSubmission.getQuiz().getQuizType().getCode().equals(Constants.QUIZ_TYPE_DIAGNOSTIC_CODE) ||
			!user.getRequiresDiagnosticTest()) {
			//Baseline across all categories
			final List<TestDesignTemplateContentSubType> subTypes = testDesignTemplateContentTypeDAOService.findAllContentSubTypes();
			TestCategoryRating rating = null;
			for (TestDesignTemplateContentSubType subType : subTypes) {
				if (!categoryRatingsMap.containsKey(subType.getId())) {
					final String description = String.format("Rating on Quiz %s Category %s", quiz.getCode(), subType.getCode());
					rating = new TestCategoryRating(subType.getId(), subType.getCode(), description);
					rating.setRating(0);
					rating.setRatingSubmissions(Collections.emptyList());
				}
				else {//Add to analytics
					rating = categoryRatingsMap.get(subType.getId());
				}
				tua.addRating(rating);
			}
		}
		testUserAnalyticsDAOService.updateRatings(tua.getCode(),categoryRatingsMap);
    	
		return quizSubmission;
	}


	private TestUserAnalytics ensureAnalyticsBaseline(User user) throws ApplicationException {
		//Ensure base lining
		final String name_ = String.format("User(%d) analytics", user.getId());
		
		TestUserAnalytics tua = new TestUserAnalytics( user.getId(), name_);
		tua = testUserAnalyticsDAOService.provide(tua);
		
		final List<TestDesignTemplateContentSubType> subTypes = testDesignTemplateContentTypeDAOService.findAllContentSubTypes();
		TestCategoryRating rating = null;
		int cnt = 1;
		for (TestDesignTemplateContentSubType subType : subTypes) {
			try {
				rating = testUserAnalyticsDAOService.getCategoryRatingByName(tua.getId(), subType.getCode());
			} catch(NoResultException e) {			
			} catch (Exception e) {
			}
			if (rating == null) {
				final String description = String.format("Rating(%s) on Category %s", user.getCode(),subType.getCode());
				rating = new TestCategoryRating(subType.getId(), subType.getCode(), description);
				rating.setRating(0);
				TestCategoryAttempt attempt = new TestCategoryAttempt(-1L, -1L,new Date(),false,false,-1);
				rating.setRatingSubmissions(Collections.emptyList());
				tua.addRating(rating);
			}
		}
		
		testUserAnalyticsDAOService.update(tua);
		
		
		if (user.getReadyForReset()) {
			user.setReadyForReset(false);
			userService.update(user);
		}
		
		return tua;
	}


	private Boolean gradeProblem(final QuizProblemResponse problemResponse, final Problem problem) {
		if (problemResponse.getSkipped())
			return null;
		
		if (problem.isMultipleChoice()) {
			return problemResponse.getResponse().trim().equalsIgnoreCase(problem.getAnswer());
		}
		else {
			//Single or  answer in range
			Double respDblValue = null;
			Double answerDblValue = null;
			final FractionFormat ff = new FractionFormat();
			try {
				Fraction resp = ff.parse(problemResponse.getResponse().trim());
				respDblValue = resp.doubleValue();
			}
			catch(ParseException e) {
				//Maybe it's answer in double format
				try {
					respDblValue = Double.valueOf(problemResponse.getResponse().trim());
				} catch (NumberFormatException e1) {
					e.printStackTrace();
					throw e1;
				}
			}

			
			//Range answer
			if (problem.getAnswerInRange() != null && problem.getAnswerInRange()) {
				final String[] luAnswers = problem.getAnswer().split(";");
				Double lowerAnswer = null;
				Double upperAnswer = null;
				
				//lower
				try {
					Fraction answer = ff.parse(luAnswers[0].trim());
					lowerAnswer = answer.doubleValue();
				}
				catch(ParseException e) {
					//Maybe it's answer in double format
					try {
						lowerAnswer = Double.valueOf(luAnswers[0].trim());
					} catch (NumberFormatException e1) {
						e.printStackTrace();
						throw e1;
					}
				}

				//upper
				try {
					Fraction answer = ff.parse(luAnswers[1].trim());
					upperAnswer = answer.doubleValue();
				}
				catch(ParseException e) {
					//Maybe it's answer in double format
					try {
						upperAnswer = Double.valueOf(luAnswers[1].trim());
					} catch (NumberFormatException e1) {
						e.printStackTrace();
						throw e1;
					}
				}
				return round(respDblValue,2) >= round(lowerAnswer,2) && round(respDblValue,2) <= round(upperAnswer,2);
			}
			//One value answer
			else {
				try {
					Fraction answer = ff.parse(problem.getAnswer().trim());
					answerDblValue = answer.doubleValue();
				}
				catch(ParseException e) {
					//Maybe it's answer in double format
					try {
						answerDblValue = Double.valueOf(problem.getAnswer().trim());
					} catch (NumberFormatException e1) {
						e.printStackTrace();
						throw e1;
					}
				}
				return round(respDblValue,2) == round(answerDblValue,2);
			}
		}
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}