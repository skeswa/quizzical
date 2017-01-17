package org.gauntlet.problems.defaults;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.gauntlet.problems.api.model.ProblemSource;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.QuizType;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.model.user.User;


public class Controller {
	private volatile LogService logger;
	private volatile IProblemDAOService problemService;
	private volatile IQuizDAOService quizService;
	private volatile IUserDAOService userService;
	
	private void start() throws Exception {
		//-- Problems
		ProblemSource ps = new ProblemSource("MW4NSAT","MW4NSAT");
		problemService.provideProblemSource(ps);
		
		ProblemCategory pc = new ProblemCategory("Heart of Algebra/Linear Equations","Heart of Algebra/Linear Equations");
		problemService.provideProblemCategory(pc);
		
		ProblemDifficulty pd = new ProblemDifficulty("Easy","Easy", "#00ff00");
		problemService.provideProblemDifficulty(pd);		
		
		
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
