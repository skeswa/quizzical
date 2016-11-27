package org.gauntlet.quizzes.model.jpa;

import java.io.Serializable;
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
	+"quiz_problem_answer")
public class JPAQuizProblemAnswer extends JPABaseEntity implements Serializable {
	private String answer;
	
	private Boolean correct;
	
	private Integer timesSkipped = 0;
	
	private Integer secondsElapsed = 0;
	
	@OneToOne(targetEntity=JPAQuizProblem.class)
	private JPAQuizProblem quizProblem;	
	

	public JPAQuizProblemAnswer() {
		super();
	}

	public JPAQuizProblemAnswer(String answer, 
			Boolean correct, 
			Integer timesSkipped, 
			Integer secondsElapsed,
			JPAQuizProblem quizProblem) {
		this();
		this.answer = answer;
		this.correct = correct;
		this.timesSkipped = timesSkipped;
		this.secondsElapsed = secondsElapsed;
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

	public JPAQuizProblem getQuizProblem() {
		return quizProblem;
	}

	public void setQuizProblem(JPAQuizProblem quizProblem) {
		this.quizProblem = quizProblem;
	}
}

