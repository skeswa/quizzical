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
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItemDifficultyType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSection;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemResponseDAOService;
import org.gauntlet.quizzes.api.model.Constants;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizType;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorService;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;

public class NonSATByWeaknessGeneratorImpl implements IQuizGeneratorService { 
	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	private volatile LogService logger;
	
	private volatile BundleContext ctx;

	private volatile IQuizDAOService quizDAOService;
	
	private volatile IProblemDAOService problemDAOService;
	
	private volatile IQuizProblemDAOService quizProblemDAOService;
	
	private volatile IQuizProblemResponseDAOService problemResponseDAOService;
	
	private volatile ITestDesignTemplateGeneratorService testDesignTemplateGeneratorService;
	
	private volatile ITestUserAnalyticsDAOService testUserAnalyticsDAOService;
	
	private volatile ITestDesignTemplateContentTypeDAOService testDesignContentTypeDAOService;
	
	@Override
	public Quiz generate(User user, Long problemTypeId, QuizGenerationParameters params) throws ApplicationException {
		//-- Get weakness categories
		List<ProblemCategory> weakCats = problemDAOService.findAllProblemCategoriesByProblemType(user.getCurrentProblemTypeId());
		List<String> catNames = weakCats.stream()
				.map(c -> {
					return c.getName();
				})
				.collect(Collectors.toList());
				
		List<TestCategoryRating> performanceByCategories = testUserAnalyticsDAOService.findWeakestCategories(user, catNames, -1);
		
		
		//----
		performanceByCategories = performanceByCategories.stream()
				.filter(c -> {
					//--
					boolean res = false;
					try {
						List<Long> ids = quizProblemDAOService.findUnpractisedProblemsByUserAndCategory(user, c.getName());
						res = !ids.isEmpty();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return res;
				})
				.collect(Collectors.toList());
		
		//----
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
	    		.limit(2)
	    		.collect(Collectors.toList());
		
		final Date quizDateTime = Calendar.getInstance().getTime();
		final String quizCode = String.format(
				"NonSAT Weakness on [%d] topics at %s on %s",
				performanceCategoryCodes.size(),
				timeFormat.format(quizDateTime),
				dateFormat.format(quizDateTime));
		
		//---- Problem Ids already correct and within time
		List<Long> excludeIds = problemResponseDAOService.getAllUserCorrectAndWithinTimeProblemIds(user);
		
		final Counter counterWithInfoCards = new Counter(0);
		final Counter counter = new Counter(0);
		List<QuizProblem> unorderedQuizProblems = new ArrayList<>();
		
		Map<Long,Problem> includedProblemIds = new HashMap<>();
		
		
		// Loop through codes until enough problems are gathered
		while (!performanceByCategories.isEmpty() && 
				unorderedQuizProblems.size() < params.getQuizSize()) {
			final List<TestCategoryRating> ratingCats = performanceByCategories.stream()
				.limit(2)
				.collect(Collectors.toList());
			
			performanceByCategories.removeAll(ratingCats);
			
			final List<String> ratingCatCodes = ratingCats
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
			
			//-- Derive TestDesign based on Cat Weakness/Performance
			final TestDesignTemplate tdTemplate = testDesignTemplateGeneratorService.generateByCategoriesAndTestSize(user, ratingCatCodes, 4);
			
			TestDesignTemplateSection nonCalSec = null;
			TestDesignTemplateSection calSec = null;

			final List<TestDesignTemplateSection> sections = tdTemplate.getOrderedSections();
			calSec = sections.get(1);
			
			
			//=== Calc
			calSec.getOrderedItems()
					.stream()
					.limit(params.getQuizSize()-unorderedQuizProblems.size())
	        		.forEach(item -> {
	        			QuizProblem qp = null;
	        			long count = 0;
							try {
								final ProblemCategory cat = problemDAOService.getProblemCategoryByCode(item.getContentSubType().getCode());
								final List<Long> allExcludedIds = Stream.concat(excludeIds.stream(), includedProblemIds.keySet().stream()).collect(Collectors.toList());
								count = problemDAOService.countByCategoryNotIn(cat.getId(), allExcludedIds);								int randomOffset = (int)GeneratorUtil.generateRandowOffset(count);
								
								final Problem problem = fuzzyMatchProblem(problemTypeId, true,includedProblemIds, item, cat, randomOffset);
								
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
						if (qp != null)
							unorderedQuizProblems.add(qp);
	    	    	});
		}
		
		
		//-- Create quiz
		final QuizType quizType = quizDAOService.provideQuizType(new QuizType(
				Constants.QUIZ_TYPE_WEAKNESS_CODE,
				Constants.QUIZ_TYPE_WEAKNESS_NAME));
			
		final Quiz quiz = new Quiz();
		quiz.setUserId(user.getId());
		quiz.setCode(quizCode);
		//quiz.setName(quizCode);
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

	private Problem fuzzyMatchProblem(Long problemTypeId, Boolean allowsCalc, Map<Long, Problem> includedProblemIds, TestDesignTemplateItem item,
			ProblemCategory cat, int randomOffset) throws ApplicationException {
		
		List<Problem> problems = problemDAOService.findByCategoryNotIn(cat.getId(),new ArrayList<Long>(includedProblemIds.keySet()),randomOffset,1);
		if (problems.iterator().hasNext()) {
			final Problem problem = problems.iterator().next();
			return problem;
		}
		else
			return null;
	}
	
	private void ensureProblemDifficulty(String quizCode, User user, Map<Long, Problem> includedProblemIds, List<QuizProblem> quizProblems, ProblemDifficulty diff, int percentage) throws ApplicationException {
		int diffPercCount = new Double(Math.ceil(((double)percentage/(double)100)*((double)quizProblems.size()))).intValue();
		final List<Long> ids = quizProblemDAOService.findUnpractisedProblemsByUserAndDifficulty(user,diff.getCode(),diffPercCount);

		ids.stream()
			.forEach(id -> {
				QuizProblem qpToReplace = quizProblems.get(ids.indexOf(id));
				Problem p;
				try {
					p = problemDAOService.getByPrimary(id);
					includedProblemIds.remove(qpToReplace.getProblemId());
					
					qpToReplace.setProblem(p);
					qpToReplace.updateCode(quizCode);
					
					includedProblemIds.put(p.getId(),p);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
	}	
}