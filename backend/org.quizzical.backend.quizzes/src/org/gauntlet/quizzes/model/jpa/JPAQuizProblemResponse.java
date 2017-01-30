package org.gauntlet.quizzes.model.jpa;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"quiz_problem_response")
public class JPAQuizProblemResponse extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -8302783166411366183L;
	
	@ManyToOne(targetEntity = JPAQuizSubmission.class)
	@JoinColumn
	private JPAQuizSubmission submission;

	private String response;
	
	private Boolean correct;
	
	private Boolean skipped;
	
	private Integer secondsElapsed = 0;
	
	private Long quizProblemId;	

	public JPAQuizProblemResponse() {}

	public JPAQuizProblemResponse(
			final String response,
			final Boolean correct,
			final Boolean skipped,
			final Integer secondsElapsed,
			final Long quizProblemId) {
		this.response = response;
		this.correct = correct;
		this.skipped = skipped;
		this.secondsElapsed = secondsElapsed;
		this.quizProblemId = quizProblemId;
	}

	public String getResponse() {
		return response;
	}


	public void setResponse(String response) {
		this.response = response;
	}


	public Boolean getCorrect() {
		return correct;
	}


	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}


	public Boolean getSkipped() {
		return skipped;
	}


	public void setSkipped(Boolean skipped) {
		this.skipped = skipped;
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

	public JPAQuizSubmission getSubmission() {
		return submission;
	}

	public void setSubmission(JPAQuizSubmission submission) {
		this.submission = submission;
	}
}

