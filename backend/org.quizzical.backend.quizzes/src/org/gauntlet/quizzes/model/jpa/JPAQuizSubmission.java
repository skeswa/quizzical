package org.gauntlet.quizzes.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"quiz_submission")
public class JPAQuizSubmission extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -7656436051296927169L;

	@ManyToOne(targetEntity = JPAQuiz.class)
	@JoinColumn
	private JPAQuiz quiz;
	
	@OneToMany(targetEntity = JPAQuizProblemResponse.class, fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="submission")
	private List<JPAQuizProblemResponse> responses = new ArrayList<>();

	public JPAQuizSubmission() {}

	public JPAQuizSubmission(JPAQuiz quiz) {
		this.code = String.format("%s-%d", quiz.getCode(), System.currentTimeMillis());
	}

	public JPAQuiz getQuiz() {
		return quiz;
	}

	public void setQuiz(JPAQuiz quiz) {
		this.quiz = quiz;
	}

	public List<JPAQuizProblemResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<JPAQuizProblemResponse> responses) {
		this.responses = responses;
	}
}

