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

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+"quiz_prob")
public class JPAQuizProblem extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 8538105534304495532L;

	private Integer ordinal;
	
	private Long problemId;
	
	@ManyToOne(targetEntity = JPAQuiz.class)
	@JoinColumn
	private JPAQuiz quiz;

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
}

