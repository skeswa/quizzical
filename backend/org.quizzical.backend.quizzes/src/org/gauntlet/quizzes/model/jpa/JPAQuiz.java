package org.gauntlet.quizzes.model.jpa;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+"quiz")
public class JPAQuiz extends JPABaseEntity implements Serializable {
    @Basic
    @Column(name = "userId", unique=true)
    protected Long userId;
    
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = JPAQuizType.class)
	@JoinColumn
	private JPAQuizType quizType;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="quiz")
	private Set<JPAQuizProblem> questions = new java.util.HashSet<JPAQuizProblem>();
	
	public JPAQuiz() {
		setCode(String.format("Quiz %d", System.currentTimeMillis()));
	}
	
	public JPAQuiz(Long userId, JPAQuizType quizType, Set<JPAQuizProblem> questions) {
		this();
		this.quizType = quizType;
		this.questions = questions;
	}

	public JPAQuizType getQuizType() {
		return quizType;
	}

	public void setQuizType(JPAQuizType quizType) {
		this.quizType = quizType;
	}

	public Set<JPAQuizProblem> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<JPAQuizProblem> questions) {
		this.questions = questions;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
