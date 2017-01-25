package org.gauntlet.quizzes.model.jpa;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;
import org.gauntlet.quizzes.api.model.QuizProblemType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"quiz_prob")
public class JPAQuizProblem extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 8538105534304495532L;
	
	private Integer quizOrdinal;
	
	private Integer sectionOrdinal;

	private Integer ordinal;
	
	private Long problemId;
	
	@ManyToOne(targetEntity = JPAQuiz.class)
	@JoinColumn
	private JPAQuiz quiz;
	
	private QuizProblemType type = QuizProblemType.REGULAR;

	public JPAQuizProblem() {
		super();
	}

	public JPAQuizProblem(String name, String code, Long problemId) {
		this();
		this.name = name;
		this.code = code;
		this.problemId = problemId;
	}

	public Long getProblemId() {
		return problemId;
	}

	public void setProblemId(Long problemId) {
		this.problemId = problemId;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public QuizProblemType getType() {
		return type;
	}

	public void setType(QuizProblemType type) {
		this.type = type;
	}

	public Integer getQuizOrdinal() {
		return quizOrdinal;
	}

	public void setQuizOrdinal(Integer quizOrdinal) {
		this.quizOrdinal = quizOrdinal;
	}

	public Integer getSectionOrdinal() {
		return sectionOrdinal;
	}

	public void setSectionOrdinal(Integer sectionOrdinal) {
		this.sectionOrdinal = sectionOrdinal;
	}	
}

