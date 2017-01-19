package org.gauntlet.problems.defaults;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.QuizType;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.model.user.User;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;


public class Controller {
	private volatile LogService logger;
	private volatile IProblemDAOService problemService;
	private volatile IQuizDAOService quizService;
	private volatile IUserDAOService userService;
	
	private volatile ITestDesignTemplateDAOService testDesignService;
	private volatile ITestDesignTemplateContentTypeDAOService contentTypeService;
	
	
	private void start() throws Exception {
		/**
		 * 
		 * Types
		 * 
		 */
		new TestDesignTemplateGenerator(testDesignService, contentTypeService).generate();
		
		
		/**
		 * 
		 * Misc
		 * 
		 */
		
		//-- Quiz related
		final QuizType qt = new QuizType("Pop Quiz","Pop Quiz");
		quizService.provideQuizType(qt);
		
		//-- Users
		//MK
		User user = new User();
		user.setCode("mk");
		user.setFirstName("Mandi");
		user.setEmailAddress("mandisakeswa999@gmail.com");
		
		user.setPasswordEncrypted(true);
		user.setPassword("aceit");
		
		userService.provide(user);
		
		//Test
		user = new User();
		user.setCode("tester");
		user.setFirstName("Tester");
		user.setEmailAddress("test@me.io");
		
		user.setPasswordEncrypted(true);
		user.setPassword("test");
		
		userService.provide(user);
	}
}
