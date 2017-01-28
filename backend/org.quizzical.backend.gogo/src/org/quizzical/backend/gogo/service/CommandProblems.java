package org.quizzical.backend.gogo.service;

import org.gauntlet.problems.api.dao.IProblemDAOService;

public class CommandProblems {

    private final IProblemDAOService problemService;

    public CommandProblems(IProblemDAOService problemService) {
        this.problemService = problemService;
    }

    public IProblemDAOService get() {
    	return problemService;
    }
}
