package org.gauntlet.problems.defaults;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.gauntlet.problems.api.model.ProblemSource;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.QuizType;
import org.osgi.service.log.LogService;


public class Controller {
	private volatile LogService logger;
	private volatile IProblemDAOService problemService;
	private volatile IQuizDAOService quizService;
	
	private void start() throws ApplicationException {
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
	}
}
