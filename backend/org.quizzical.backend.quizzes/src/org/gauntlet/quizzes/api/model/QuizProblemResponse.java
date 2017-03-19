package org.gauntlet.quizzes.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class QuizProblemResponse extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1145554261745216229L;
	
	public static String code(final Long quizProblemId, final String response) {
		return String.format("%d-%s-%d", quizProblemId, response, System.currentTimeMillis());
	}

	private String response;
	
	private Boolean skipped;
	
	private Boolean correct;
	
	private Boolean reported;
	
	private Integer secondsElapsed = 0;
	
	private QuizProblem quizProblem;
	
	private Long quizProblemId;
	
	public QuizProblemResponse() {}

	public QuizProblemResponse(
			final String code,
			final String response,
			final Boolean skipped,
			final Boolean correct,
			final Boolean reported,
			final Integer secondsElapsed,
			final Long quizProblemId) {
		this.code = code;
		this.response = response;
		this.skipped = skipped;
		this.correct = correct;
		this.reported = reported;
		this.secondsElapsed = secondsElapsed;
		this.quizProblemId = quizProblemId;
	}

	public Boolean getCorrect() {
		return correct;
	}

	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Boolean getSkipped() {
		return skipped;
	}

	public void setSkipped(Boolean skipped) {
		this.skipped = skipped;
	}

	public Boolean getReported() {
		return reported;
	}

	public void setReported(Boolean reported) {
		this.reported = reported;
	}

	public Integer getSecondsElapsed() {
		return secondsElapsed;
	}

	public void setSecondsElapsed(Integer secondsElapsed) {
		this.secondsElapsed = secondsElapsed;
	}

	public Long getQuizProblemId() {
		return quizProblemId;
	}

	public void setQuizProblemId(Long quizProblemId) {
		this.quizProblemId = quizProblemId;
	}

	public QuizProblem getQuizProblem() {
		return quizProblem;
	}

	public void setQuizProblem(QuizProblem quizProblem) {
		this.quizProblem = quizProblem;
	}
}
