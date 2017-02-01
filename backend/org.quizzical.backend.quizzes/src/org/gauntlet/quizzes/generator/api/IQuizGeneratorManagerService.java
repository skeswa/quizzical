package org.gauntlet.quizzes.generator.api;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.quizzes.api.model.Quiz;
import org.gauntlet.quizzes.generator.api.model.QuizGenerationParameters;
import org.quizzical.backend.security.authorization.api.model.user.User;

public interface IQuizGeneratorManagerService {
	public Quiz generate(User user, QuizGenerationParameters params) throws ApplicationException;
}
