package org.gauntlet.quizzes.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;
import org.gauntlet.problems.api.model.Problem;

public class QuizProblem extends BaseEntity implements Serializable {
	
	private Long problemId;
	
	private Problem problem;
	
	public QuizProblem() {
	}
	
	public QuizProblem(String name, String code, Long problemId) {
		this();
		this.name = name;
		this.code = code;
		this.problemId = problemId;
	}
	
	public QuizProblem(String name, String code, Problem problem) {
		this();
		this.name = name;
		this.code = code;
		this.problem = problem;
	}
	
	public QuizProblem(String name, String code, Long problemId, Problem problem) {
		this();
		this.name = name;
		this.code = code;
		this.problem = problem;
		this.problemId = problemId;
	}


	public Long getProblemId() {
		return problemId;
	}

	public void setProblemId(Long problemId) {
		this.problemId = problemId;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}
}
