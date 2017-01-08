package org.gauntlet.quizzes.generator.api;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;

public interface IQuizGeneratorManagerService {
	public Quiz generate(QuizGenerationParameters params) throws ApplicationException;
}
