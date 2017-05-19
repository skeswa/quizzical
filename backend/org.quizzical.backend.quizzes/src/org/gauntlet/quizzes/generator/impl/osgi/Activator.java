package org.gauntlet.quizzes.generator.impl.osgi;

import java.util.Properties;

import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemResponseDAOService;
import org.gauntlet.quizzes.generator.api.Constants;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorManagerService;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorService;
import org.gauntlet.quizzes.generator.defaults.impl.ByProblemCategoryGeneratorImpl;
import org.gauntlet.quizzes.generator.defaults.impl.ByProblemSourceGeneratorImpl;
import org.gauntlet.quizzes.generator.defaults.impl.ByWeaknessGeneratorImpl;
import org.gauntlet.quizzes.generator.defaults.impl.CategoryPracticeGeneratorImpl;
import org.gauntlet.quizzes.generator.defaults.impl.DiagnosticTestGeneratorImpl;
import org.gauntlet.quizzes.generator.defaults.impl.LRUGeneratorImpl;
import org.gauntlet.quizzes.generator.defaults.impl.PracticeTestGeneratorImpl;
import org.gauntlet.quizzes.generator.defaults.impl.QuizForQAGeneratorImpl;
import org.gauntlet.quizzes.generator.defaults.impl.SkippedOrIncorrectGeneratorImpl;
import org.gauntlet.quizzes.generator.defaults.impl.UnpracticedGeneratorImpl;
import org.gauntlet.quizzes.generator.impl.QuizGeneratorManagerImpl;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.testdesign.api.ITestDesignTemplateGeneratorService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;

public class Activator extends DependencyActivatorBase {

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {

	}

	@Override
	public void init(BundleContext arg0, DependencyManager dm) throws Exception {
		dm.add(createComponent().setInterface(IQuizGeneratorManagerService.class.getName(), null)
				.setImplementation(QuizGeneratorManagerImpl.class)
				.add(createServiceDependency().setService(IQuizGeneratorService.class)
						.setCallbacks("addGenerator", "removeGenerator")
						.setRequired(false))
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IUserDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false)));
		
		
		//--
		Properties properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, Constants.GENERATOR_TYPE_BY_CATEGORY);
		Component component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(ByProblemCategoryGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(ITestDesignTemplateDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(ITestDesignTemplateContentTypeDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		dm.add(component);
		
		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, Constants.GENERATOR_TYPE_BY_SOURCE);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(ByProblemSourceGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		dm.add(component);
		
		//--
/*		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, Constants.GENERATOR_TYPE_REALISTIC_TEST);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(PracticeTestGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(ITestDesignTemplateDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		dm.add(component);*/
		
		//--
		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, Constants.GENERATOR_TYPE_REALISTIC_TEST);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(DiagnosticTestGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(ITestDesignTemplateDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		dm.add(component);
		
		//--
		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, Constants.GENERATOR_TYPE_WEAKNESS_TEST);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(ByWeaknessGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IQuizProblemResponseDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(ITestDesignTemplateGeneratorService.class).setRequired(true))
				.add(createServiceDependency().setService(ITestUserAnalyticsDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(ITestDesignTemplateContentTypeDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		dm.add(component);
		
		//--
		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, Constants.GENERATOR_TYPE_PRACTICE_TEST);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(PracticeTestGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(ITestDesignTemplateDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		dm.add(component);
		
		
		//--
		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, org.gauntlet.quizzes.api.model.Constants.QUIZ_TYPE_LRU_CODE);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(LRUGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IQuizProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		dm.add(component);
		
		//--
		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, org.gauntlet.quizzes.api.model.Constants.QUIZ_TYPE_SKIPPED_OR_INCORRECT_CODE);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(SkippedOrIncorrectGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IQuizProblemResponseDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		
		//--
		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, Constants.GENERATOR_TYPE_QA_CHECK);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(QuizForQAGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IQuizProblemResponseDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		
		//--
		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, org.gauntlet.quizzes.api.model.Constants.QUIZ_TYPE_UNPRACTICED_CODE);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(UnpracticedGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IQuizProblemResponseDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		dm.add(component);

		//--
		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, org.gauntlet.quizzes.api.model.Constants.QUIZ_TYPE_CATEGORY_CODE);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(CategoryPracticeGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IQuizProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		dm.add(component);
	}
}
