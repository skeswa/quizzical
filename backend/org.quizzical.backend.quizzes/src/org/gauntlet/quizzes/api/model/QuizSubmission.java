package org.gauntlet.quizzes.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;

public class QuizSubmission extends BaseEntity implements Serializable {
	
	private Quiz quiz;
	
	private List<QuizProblemAnswer> answers = new ArrayList<>();
	
	public QuizSubmission() {
	}
	
	public QuizSubmission(String name, String code, Quiz quiz) {
		this.name = name;
		this.code = code;
		this.quiz = quiz;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public List<QuizProblemAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<QuizProblemAnswer> answers) {
		this.answers = answers;
	}
	
	public void addAnswer(QuizProblemAnswer answer) {
		getAnswers().add(answer);
	}
}
