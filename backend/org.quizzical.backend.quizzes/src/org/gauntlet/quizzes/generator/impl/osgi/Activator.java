package org.gauntlet.quizzes.generator.impl.osgi;

import java.util.Properties;

import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.generator.api.Constants;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorManagerService;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorService;
import org.gauntlet.quizzes.generator.defaults.impl.ByProblemCategoryGeneratorImpl;
import org.gauntlet.quizzes.generator.defaults.impl.PracticeTestGeneratorImpl;
import org.gauntlet.quizzes.generator.impl.QuizGeneratorManagerImpl;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
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
		
		//--
		properties = new Properties();
		properties.put(Constants.GENERATOR_TYPE_PARAM, Constants.GENERATOR_TYPE_REALISTIC_TEST);
		component = dm.createComponent()
				.setInterface(IQuizGeneratorService.class.getName(), properties)
				.setImplementation(PracticeTestGeneratorImpl.class)
				.add(createServiceDependency().setService(IQuizDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(IProblemDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(ITestDesignTemplateDAOService.class).setRequired(true))
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
	            ;
		dm.add(component);
	}
}
