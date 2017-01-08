package org.gauntlet.quizzes.model.jpa;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+"quiz_problem_response")
public class JPAQuizProblemResponse extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -8302783166411366183L;

	private String response;
	
	private Boolean correct;
	
	private Boolean skipped;
	
	private Integer secondsElapsed = 0;
	
	@OneToOne(targetEntity=JPAQuizProblem.class, cascade = CascadeType.ALL)
	private JPAQuizProblem quizProblem;	

	public JPAQuizProblemResponse() {}

	public JPAQuizProblemResponse(
			final String response,
			final Boolean correct,
			final Boolean skipped,
			final Integer secondsElapsed,
			final JPAQuizProblem quizProblem) {
		this.response = response;
		this.correct = correct;
		this.skipped = skipped;
		this.secondsElapsed = secondsElapsed;
		this.quizProblem = quizProblem;
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


	public JPAQuizProblem getQuizProblem() {
		return quizProblem;
	}


	public void setQuizProblem(JPAQuizProblem quizProblem) {
		this.quizProblem = quizProblem;
	}

}

