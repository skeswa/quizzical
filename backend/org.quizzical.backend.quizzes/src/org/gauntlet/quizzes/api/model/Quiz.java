package org.gauntlet.quizzes.api.model;

import java.io.Serializable;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;

public class Quiz extends BaseEntity implements Serializable {
	
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
	
	public Quiz() {
	}
	
	public Quiz(String name, String code, List<QuizProblem> questions) {
		this.name = name;
		this.code = code;
		this.questions = questions;
	}
}
