package org.gauntlet.quizzes.api.model;

import java.io.Serializable;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;
import org.quizzical.backend.security.authorization.api.model.user.User;

public class Quiz extends BaseEntity implements Serializable {
	private Long userId;
	
	private QuizType quizType;

	private List<QuizProblem> questions;
	
	public QuizType getQuizType() {
		return quizType;
	}

	public void setQuizType(QuizType quizType) {
		this.quizType = quizType;
	}

	public List<QuizProblem> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuizProblem> questions) {
		this.questions = questions;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Quiz() {
	}
	
	public Quiz(Long userId, String name, String code, List<QuizProblem> questions) {
		this.name = name;
		this.code = code;
		this.questions = questions;
		this.userId = userId;
	}
}
