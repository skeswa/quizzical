package org.gauntlet.quizzes.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class QuizProblem extends BaseEntity implements Serializable {
	
	private Long problemId;
	
	public QuizProblem() {
	}
	
	public QuizProblem(String name, String code, Long problemId) {
		this();
		this.name = name;
		this.code = code;
		this.problemId = problemId;
	}


	public Long getProblemId() {
		return problemId;
	}

	public void setProblemId(Long problemId) {
		this.problemId = problemId;
	}
	
	
}
