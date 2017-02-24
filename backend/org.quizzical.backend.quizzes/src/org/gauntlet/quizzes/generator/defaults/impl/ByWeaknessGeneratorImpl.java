package org.gauntlet.quizzes.generator.defaults.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.testdesign.api.ITestDesignTemplateGeneratorService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplate;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentSubType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItem;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSection;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemResponseDAOService;
import org.gauntlet.quizzes.api.model.Constants;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizType;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorService;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;

public class ByWeaknessGeneratorImpl implements IQuizGeneratorService { 
	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	private volatile LogService logger;
	
	private volatile BundleContext ctx;

	private volatile IQuizDAOService quizDAOService;
	
	private volatile IProblemDAOService problemDAOService;
	
	private volatile IQuizProblemResponseDAOService problemResponseDAOService;
	
	private volatile ITestDesignTemplateGeneratorService testDesignTemplateGeneratorService;
	
	private volatile ITestUserAnalyticsDAOService testUserAnalyticsDAOService;
	
	private volatile ITestDesignTemplateContentTypeDAOService testDesignContentTypeDAOService;
	
	@Override
	public Quiz generate(User user, QuizGenerationParameters params) throws ApplicationException {
		//-- Get weakness categories
		final List<TestCategoryRating> performanceByCategories = testUserAnalyticsDAOService.findWeakestCategories(user, params.getQuizSize());
		final List<String> performanceCategoryCodes = performanceByCategories
				.stream()
	    		.map(rating -> {
	    			TestDesignTemplateContentSubType subType;
					try {
						subType = testDesignContentTypeDAOService.getContentSubTypeByPrimary(rating.getCategoryId());
						return subType.getCode();
					} catch (Exception e) {
					}
	    			return (String)null;
		    	})
	    		.collect(Collectors.toList());
		
		//-- Create quiz
		final QuizType quizType = quizDAOService.provideQuizType(new QuizType(
				Constants.QUIZ_TYPE_WEAKNESS_CODE,
				Constants.QUIZ_TYPE_WEAKNESS_NAME));
		final Date quizDateTime = Calendar.getInstance().getTime();
		final String quizCode = String.format(
				"Weakness on [%s] at %s on %s",
				performanceCategoryCodes,
				timeFormat.format(quizDateTime),
				dateFormat.format(quizDateTime));
		
		//-- Derive TestDesign based on Cat Weakness/Performance
		final TestDesignTemplate tdTemplate = testDesignTemplateGeneratorService.generateByCategoriesAndTestSize(user, performanceCategoryCodes, params.getQuizSize());
		
		TestDesignTemplateSection nonCalSec = null;
		TestDesignTemplateSection calSec = null;

		final List<TestDesignTemplateSection> sections = tdTemplate.getOrderedSections();
		nonCalSec = sections.get(0);
		calSec = sections.get(1);
		
		final Counter counterWithInfoCards = new Counter(0);
		final Counter counter = new Counter(0);
		
		//=== Problem Ids already correct and within time
		List<Long> excludeIds = problemResponseDAOService.getAllUserCorrectAndWithinTimeProblemIds(user);
		
		//=== NonCalc
		Map<Long,Problem> includedProblemIds = new HashMap<>();


		final List<QuizProblem> nonCalcQuizProblems = nonCalSec.getOrderedItems()
				.stream()
        		.map(item -> {
        			QuizProblem qp = null;
        			try {
						ProblemCategory cat = problemDAOService.getProblemCategoryByCode(item.getContentSubType().getCode());
						ProblemDifficulty diff = problemDAOService.getProblemDifficultyByCode(GeneratorUtil.getDifficultyCode(item.getDifficultyType()));
						
						final List<Long> allExcludedIds = Stream.concat(excludeIds.stream(), includedProblemIds.keySet().stream()).collect(Collectors.toList());
						long count = problemDAOService.countByCalcAndDifficultyAndCategoryNotInIn(false,diff.getId(), cat.getId(), allExcludedIds);
						if (count < 1)
							throw new RuntimeException(String.format("Test Item %s cannot match a problem with reqCalc=%b cat=%s, diff=%s not in [%s]",item.getCode(),false,cat.getCode(),diff.getCode(),includedProblemIds.keySet()));
						int randomOffset = (int)GeneratorUtil.generateRandowOffset(count);
						
						final Problem problem = fuzzyMatchProblem(false,includedProblemIds, item, cat, diff, randomOffset);
						
						if (problem != null) {
							includedProblemIds.put(problem.getId(),problem);
							qp = new QuizProblem(
									quizCode,
									counter.incr(),
									item.getSection().getOrdinal(),
									counterWithInfoCards.incr(),
									problem.getId(),
									problem);
						}
					} catch (Exception e) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						String stacktrace = sw.toString();
						System.out.println(e.getMessage());
						//logger.log(LogService.LOG_ERROR,stacktrace);
						//throw new RuntimeException(String.format("Error processing TestDesign item %s",item.getCode()));
					}
        			
        			return qp;
    	    	})
        		.filter(qp -> qp!=null)
        		.collect(Collectors.toList());
	
		
		
		//=== Calc
		final List<QuizProblem> calcQuizProblems = calSec.getOrderedItems()
				.stream()
        		.map(item -> {
        			QuizProblem qp = null;
        			long count = 0;
						try {
							final ProblemCategory cat = problemDAOService.getProblemCategoryByCode(item.getContentSubType().getCode());
							final ProblemDifficulty diff = problemDAOService.getProblemDifficultyByCode(GeneratorUtil.getDifficultyCode(item.getDifficultyType()));
							
							final List<Long> allExcludedIds = Stream.concat(excludeIds.stream(), includedProblemIds.keySet().stream()).collect(Collectors.toList());
							count = problemDAOService.countByCalcAndDifficultyAndCategoryNotInIn(true,diff.getId(), cat.getId(), allExcludedIds);
							if (count < 1)
								throw new RuntimeException(String.format("Test Item %s cannot match a problem with reqCalc=%b cat=%s, diff=%s not in [%s]",item.getCode(),true,cat.getCode(),diff.getCode(),includedProblemIds.keySet()));
							int randomOffset = (int)GeneratorUtil.generateRandowOffset(count);
							
							final Problem problem = fuzzyMatchProblem(true,includedProblemIds, item, cat, diff, randomOffset);
							
							if (problem != null) {
								includedProblemIds.put(problem.getId(),problem);
								qp = new QuizProblem(
										quizCode,
										counter.incr(),
										item.getSection().getOrdinal(),
										counterWithInfoCards.incr(),
										problem.getId(),
										problem); 
							}
						} catch (Exception e) {
							StringWriter sw = new StringWriter();
							e.printStackTrace(new PrintWriter(sw));
							String stacktrace = sw.toString();
							System.out.println(e.getMessage());
							//logger.log(LogService.LOG_ERROR,stacktrace);
							//throw new RuntimeException(String.format("Error processing TestDesign item %s",item.getCode()));
						}
				
        			
        			return qp;
    	    	})
        		.filter(qp -> qp!=null)
        		.collect(Collectors.toList());	
		final List<QuizProblem> unorderedQuizProblems = Stream.concat(nonCalcQuizProblems.stream(), calcQuizProblems.stream()).collect(Collectors.toList());
			
		final Quiz quiz = new Quiz();
		quiz.setUserId(user.getId());
		quiz.setCode(quizCode);
		quiz.setName(quizCode);
		quiz.setQuizType(quizType);
		quiz.setQuestions(unorderedQuizProblems);
		
		final Quiz persistedQuiz = quizDAOService.provide(user, quiz);
		persistedQuiz.getQuestions()
			.stream()
			.forEach(question -> {
				question.setProblem(includedProblemIds.get(question.getProblemId()));
				question.setQuiz(quiz);
			});
		
		Collections.sort(persistedQuiz.getQuestions(), new Comparator<QuizProblem>() {
			@Override
			public int compare(QuizProblem o1, QuizProblem o2) {
				if  (o1.getOrdinal() < o2.getOrdinal())
					return -1;
				else if (o1.getOrdinal() > o2.getOrdinal())
					return  1;
				else 
					return 0;//they must be the same
			}
		});
		
		return persistedQuiz;
	}

	private Problem fuzzyMatchProblem(Boolean allowsCalc, Map<Long, Problem> includedProblemIds, TestDesignTemplateItem item,
			ProblemCategory cat, ProblemDifficulty diff, int randomOffset) throws ApplicationException {
		
		List<Problem> problems = problemDAOService.findByDifficultyAndCategoryNotInIn(allowsCalc,diff.getId(), cat.getId(), new ArrayList<Long>(includedProblemIds.keySet()),randomOffset,1);
		if (problems.isEmpty()) {
			allowsCalc = !allowsCalc;
			problems = problemDAOService.findByDifficultyAndCategoryNotInIn(allowsCalc,diff.getId(), cat.getId(), new ArrayList<Long>(includedProblemIds.keySet()),randomOffset,1);
		}
		if (problems.iterator().hasNext()) {
			final Problem problem = problems.iterator().next();
			return problem;
		}
		else
			return null;
	}
}