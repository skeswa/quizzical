package org.gauntlet.quizzes.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;
import org.gauntlet.problems.api.model.Problem;

public class QuizProblem extends BaseEntity implements Serializable {
	
	private Long problemId;
	
	private Integer ordinal;
	
	private Problem problem;
	
	public QuizProblem() {
	}
	
	public QuizProblem(final String name, final String code, final Integer ordinal) {
		this.name = name;
		this.code = code;
		this.ordinal = ordinal;
	}
	
	public QuizProblem(
			final String name,
			final String code,
			final Integer ordinal,
			final Long problemId) {
		this(name, code, ordinal);
		this.problemId = problemId;
	}
	
	public QuizProblem(
			final String name,
			final String code,
			final Integer ordinal,
			final Long problemId,
			final Problem problem) {
		this(name, code, ordinal, problemId);
		this.problem = problem;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
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
