package org.gauntlet.quizzes.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;

public class QuizSubmission extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 2568785319357287025L;

	private Quiz quiz;

	private Long quizId;
	
	private List<QuizProblemResponse> responses = new ArrayList<>();
	
	public QuizSubmission() {}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Long getQuizId() {
		return quizId;
	}

	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}

	public List<QuizProblemResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<QuizProblemResponse> responses) {
		this.responses = responses;
	}
	
	@Override
	public String toString() {
		return String.format("{\"id\":%d,\"date\":\"%s\",\"code\":\"%s\"}",getId(),getDateCreated(),getCode());
	}
}
