package org.gauntlet.quizzes.generator.defaults.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.dao.IQuizProblemDAOService;
import org.gauntlet.quizzes.api.model.Constants;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.api.model.QuizProblem;
import org.gauntlet.quizzes.api.model.QuizType;
import org.gauntlet.quizzes.generator.api.IQuizGeneratorService;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;

public class QuizForQAGeneratorImpl implements IQuizGeneratorService { 
	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	private volatile LogService logger;
	
	private volatile BundleContext ctx;

	private volatile IQuizDAOService quizDAOService;
	
	private volatile IProblemDAOService problemDAOService;
	
	private volatile IQuizProblemDAOService quizProblemService;
	
	
	@Override
	public Quiz generate(User user,  Long problemTypeId, QuizGenerationParameters params) throws ApplicationException {
		
		final QuizType quizType = quizDAOService.provideQuizType(new QuizType(
				Constants.QUIZ_TYPE_LRU_CODE,
				Constants.QUIZ_TYPE_LRU_NAME));
		final String quizCode = String.format(
				"lru-%s-%d-%d",
				params.getGeneratorType(),
				params.getQuizSize(),
				System.currentTimeMillis());
		final Date quizDateTime = Calendar.getInstance().getTime();
		final String quizName = String.format(
				"LRU at %s on %s",
				timeFormat.format(quizDateTime),
				dateFormat.format(quizDateTime));
		
		Map<Long,Problem> includedProblemIds = new HashMap<>();
		
		final List<Long> userProblemIds = quizProblemService.getAllUserProblemIds(user);
		final List<Problem> problems = problemDAOService.getAllNonQAedProblems(params.getQuizSize());
		final List<QuizProblem> unorderedQuizProblems = problems
				.stream()
				.map(problem -> {
					includedProblemIds.put(problem.getId(), problem);
					return new QuizProblem(
							quizCode,
							problem.getSourceIndexWithinPage(),
							problem.getSourceIndexWithinPage(),
							problem.getSourceIndexWithinPage(),
							problem.getId(),
							problem);
					
				})
				.collect(Collectors.toList());
			
		final Quiz quiz = new Quiz();
		quiz.setUserId(user.getId());
		quiz.setCode(quizCode);
		quiz.setName(quizName);
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
}