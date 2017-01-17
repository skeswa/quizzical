package org.gauntlet.quizzes.generator.impl;

import java.util.HashMap;
import java.util.Map;

import org.gauntlet.core.api.ApplicationException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.model.user.User;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.generator.api.Constants;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorManagerService;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorService;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;


@SuppressWarnings("restriction")
public class QuizGeneratorManagerImpl implements IQuizGeneratorManagerService {
	private volatile LogService logger;
	
	private volatile BundleContext ctx;
	
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
		final ServiceReference generatorRef = references.get(params.getGeneratorType());
		final IQuizGeneratorService generator = (IQuizGeneratorService) ctx.getService(generatorRef);
		return generator.generate(user,params);
	}
}