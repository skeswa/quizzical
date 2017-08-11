package org.gauntlet.quizzes.generator.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemType;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.analytics.api.model.TestCategoryAttempt;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;
import org.quizzical.backend.analytics.api.model.TestUserAnalytics;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentSubType;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.generator.api.Constants;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorManagerService;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorService;
import org.gauntlet.quizzes.generator.api.UserHasNotTakenDiagnosticTestException;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;


@SuppressWarnings("restriction")
public class QuizGeneratorManagerImpl implements IQuizGeneratorManagerService {
	private volatile LogService logger;
	
	private volatile BundleContext ctx;
	
	private volatile IQuizDAOService quizService;
	
	private volatile IUserDAOService userService;
	
	private volatile IProblemDAOService problemService;
	
	private volatile ITestDesignTemplateContentTypeDAOService testDesignTemplateContentTypeDAOService;
	
	private volatile ITestUserAnalyticsDAOService testUserAnalyticsDAOService;
	
    private Map<String, ServiceReference> references = new HashMap<String, ServiceReference>();
	
	protected void addGenerator(final ServiceReference ref) {
		final String type = (String) ref.getProperty(Constants.GENERATOR_TYPE_PARAM);
		references.put(type, ref);
	}
	
	protected void removeGenerator(final ServiceReference ref) {
		final String type = ctx.getProperty(Constants.GENERATOR_TYPE_PARAM);
		references.remove(type);
	}
	
	
	@Override
	public Quiz generate(User user, QuizGenerationParameters params) throws ApplicationException {
		//refresh user
		user = userService.getByEmail(user.getEmailAddress());
		
		//
		ProblemType problemType = null;
		try {
			problemType = problemService.getProblemTypeByPrimary(user.getCurrentProblemTypeId());
		} catch (NoSuchModelException e) {
		}
		
		ServiceReference generatorRef = references.get(params.getGeneratorType());
		if (params.getGeneratorType().equals(Constants.GENERATOR_TYPE_BY_SOURCE)) {
			generatorRef = references.get(Constants.GENERATOR_TYPE_BY_SOURCE);
		}
		else if (problemType != null && problemType.getNonSAT() || user.getMakeNextRunUnpracticed()) {
			if (!categoriesOfWeaknessExists(user) || user.getMakeNextRunUnpracticed())
				generatorRef = references.get(org.gauntlet.quizzes.api.model.Constants.QUIZ_TYPE_NON_SAT_UNPRACTICED_CODE);
		   else 
			   generatorRef = references.get(org.gauntlet.quizzes.api.model.Constants.QUIZ_TYPE_NON_SAT_WEAKNESS_CODE);
		}
		else {
			if (!(user.getQa() || user.getAdmin()) && (!quizService.userHasTakenDiagnoticTest(user) && 
					!params.getGeneratorType().equals(Constants.GENERATOR_TYPE_REALISTIC_TEST)))
				throw new UserHasNotTakenDiagnosticTestException(user.getCode());

			
			//Check if next quiz is a practice test
			if (Constants.GENERATOR_TYPE_BY_SOURCE.equals(params.getGeneratorType()))
				generatorRef = references.get(Constants.GENERATOR_TYPE_BY_SOURCE);
			else if (user.getMakeNextRunAPracticeTest())
				generatorRef = references.get(Constants.GENERATOR_TYPE_PRACTICE_TEST);
			else if (user.getMakeNextRunLeastRecentlyPractice())
				generatorRef = references.get(org.gauntlet.quizzes.api.model.Constants.QUIZ_TYPE_LRU_CODE);
			else if (user.getMakeNextRunPracticeSkippedOrIncorrect())
				generatorRef = references.get(org.gauntlet.quizzes.api.model.Constants.QUIZ_TYPE_SKIPPED_OR_INCORRECT_CODE);
			else if (user.getMakeNextRunOnCategory() != null && user.getMakeNextRunOnCategory() > 0) {
				params.setProblemCategoryId(user.getMakeNextRunOnCategory());
				generatorRef = references.get(org.gauntlet.quizzes.api.model.Constants.QUIZ_TYPE_CATEGORY_CODE);
			}
			else if (user.getMakeNextRunUnpracticed())
				generatorRef = references.get(org.gauntlet.quizzes.api.model.Constants.QUIZ_TYPE_UNPRACTICED_CODE);
			else if (user.getQa())
				generatorRef = references.get(Constants.GENERATOR_TYPE_QA_CHECK);
		}
		
		final IQuizGeneratorService generator = (IQuizGeneratorService) ctx.getService(generatorRef);
		
		Quiz quiz = null;
		try {
			quiz = generator.generate(user,user.getCurrentProblemTypeId(), params);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		//Update user
		if (user.getMakeNextRunAPracticeTest()) {
			user.setMakeNextRunAPracticeTest(false);
		}
		else if (user.getMakeNextRunLeastRecentlyPractice()) {
			user.setMakeNextRunLeastRecentlyPractice(false);
		}
		else if (user.getMakeNextRunPracticeSkippedOrIncorrect()) {
			user.setMakeNextRunPracticeSkippedOrIncorrect(false);
		}
		else if (user.getMakeNextRunOnCategory() != null && user.getMakeNextRunOnCategory() > 0) {
			user.setMakeNextRunOnCategory(-1L);
		}
		
		user.setMakeNextRunUnpracticed(!user.getMakeNextRunUnpracticed());
		userService.update(user);	
			
		
		return quiz;
	}

	private boolean categoriesOfWeaknessExists(User user) throws ApplicationException {
		List<ProblemCategory> weakCats = problemService.findAllProblemCategoriesByProblemType(user.getCurrentProblemTypeId());
		List<String> catNames = weakCats.stream()
				.map(c -> {
					return c.getName();
				})
				.collect(Collectors.toList());
				
		List<TestCategoryRating> performanceByCategories = testUserAnalyticsDAOService.findWeakestCategories(user, catNames, -1);
		return !performanceByCategories.isEmpty();
	}
}