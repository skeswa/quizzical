package org.gauntlet.quizzes.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class QuizProblemAnswer extends BaseEntity implements Serializable {
	
	private String answer;
	
	private Boolean correct;
	
	private Integer timesSkipped = 0;
	
	private Integer secondsElapsed = 0;
	
	private QuizProblem quizProblem;
	
	public QuizProblemAnswer() {
	}
	
	public QuizProblemAnswer(String answer,
							 Boolean correct,
							 QuizProblem quizProblem) {
		this();
		this.answer = answer;
		this.correct = correct;
		this.quizProblem = quizProblem;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Boolean getCorrect() {
		return correct;
	}

	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}

	public Integer getTimesSkipped() {
		return timesSkipped;
	}

	public void setTimesSkipped(Integer timesSkipped) {
		this.timesSkipped = timesSkipped;
	}

	public Integer getSecondsElapsed() {
		return secondsElapsed;
	}

	public void setSecondsElapsed(Integer secondsElapsed) {
		this.secondsElapsed = secondsElapsed;
	}

	public QuizProblem getQuizProblem() {
		return quizProblem;
	}

	public void setQuizProblem(QuizProblem quizProblem) {
		this.quizProblem = quizProblem;
	}
}
