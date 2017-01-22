package org.quizzical.backend.gogo.service;

import org.gauntlet.quizzes.api.dao.IQuizDAOService;

public class CommandQuizzes {

    private final IQuizDAOService quizService;

    public CommandQuizzes(IQuizDAOService quizService) {
        this.quizService = quizService;
    }

    public IQuizDAOService get() {
    	return quizService;
    }
}
