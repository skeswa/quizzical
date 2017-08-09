package org.gauntlet.quizzes.generator.defaults.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

public class CategoryPracticeGeneratorImpl implements IQuizGeneratorService { 
	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	private volatile LogService logger;
	
	private volatile BundleContext ctx;

	private volatile IQuizDAOService quizDAOService;
	
	private volatile IProblemDAOService problemDAOService;
	
	private volatile IQuizProblemDAOService quizProblemService;
	
	
	@Override
	public Quiz generate(User user,  Long problemTypeId, QuizGenerationParameters params) throws ApplicationException {
		
		Quiz persistedQuiz = null;
		try {
			final QuizType quizType = quizDAOService.provideQuizType(new QuizType(
					Constants.QUIZ_TYPE_CATEGORY_CODE,
					Constants.QUIZ_TYPE_CATEGORY_NAME));
			final String quizCode = String.format(
					"cat-%s-%d-%s",
					params.getGeneratorType(),
					params.getQuizSize(),
					UUID.randomUUID().toString());
			final Date quizDateTime = Calendar.getInstance().getTime();
			final String quizName = String.format(
					"Category Practice at %s on %s",
					timeFormat.format(quizDateTime),
					dateFormat.format(quizDateTime));
			
			Map<Long,Problem> includedProblemIds = new HashMap<>();
			
			List<Problem> problems;
			
			final List<Long> userProblemIds = quizProblemService.getAllUserProblemIds(user);
			problems = problemDAOService.findByCategoryNotIn(params.getProblemCategoryId(), userProblemIds, 0, params.getQuizSize());  
			if (problems.size() < params.getQuizSize()) {
				final List<Problem> newProblems = problemDAOService.findByCategoryNotIn(params.getProblemCategoryId(), new ArrayList<Long>(), 0, params.getQuizSize()); 
				newProblems.stream()
					.forEach(prblm -> {
						if (problems.size() < params.getQuizSize() && !problems.contains(prblm))
						{
							problems.add(prblm);
						}
					});
			}
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
			
			persistedQuiz = quizDAOService.provide(user, quiz);
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
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			System.out.println(stacktrace);
			throw e;
		}
		return persistedQuiz;
	}
}