package org.gauntlet.lessons.model.jpa;

import java.io.Serializable;

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
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"prob_tmpl_lesson")
public class JPALessonProblem extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 8538105534304495532L;
	
	private Long problemId;
	
	private Integer ordinal;
	
	@ManyToOne(targetEntity = JPALesson.class)
	@JoinColumn
	private JPALesson lesson;
	

	public JPALessonProblem() {
		super();
	}

	public JPALessonProblem(String name, String code, Long problemId, Integer ordinal) {
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

	public JPALesson getLesson() {
		return lesson;
	}

	public void setLesson(JPALesson lesson) {
		this.lesson = lesson;
	}
}

