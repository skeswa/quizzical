package org.gauntlet.quizzes.generator.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.gauntlet.core.api.ApplicationException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;
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
		
		if (!(user.getQa() || user.getAdmin()) && (!quizService.userHasTakenDiagnoticTest(user) && 
				!params.getGeneratorType().equals(Constants.GENERATOR_TYPE_REALISTIC_TEST)))
			throw new UserHasNotTakenDiagnosticTestException(user.getCode());
		

		ServiceReference generatorRef = references.get(params.getGeneratorType());		
		
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
		
		final IQuizGeneratorService generator = (IQuizGeneratorService) ctx.getService(generatorRef);
		
		Quiz quiz = null;
		try {
			quiz = generator.generate(user,params);
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
}