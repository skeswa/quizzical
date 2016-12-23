package org.gauntlet.quizzes.generator.defaults.impl;

import java.util.ArrayList;
import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorService;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;


@SuppressWarnings("restriction")
public class ByProblemCategoryGeneratorImpl implements IQuizGeneratorService {
	private volatile LogService logger;
	
	private volatile BundleContext ctx;
	
	private volatile IProblemDAOService problemDAOService;
	
	@Override
	public Quiz generate(QuizGenerationParameters params) throws ApplicationException {
		Quiz quiz = new Quiz();
		List<Problem> problems = problemDAOService.findByCategory(params.getProblemCategoryId(), 0, params.getQuizSize());
		List<QuizProblem> qProblems = new ArrayList<>();
		for (Problem p : problems) {
			QuizProblem qp = new QuizProblem();
			qp.setProblemId(p.getId());
			qProblems.add(qp);
		}
		quiz.setQuestions(qProblems);
		
		return quiz;
	}
}