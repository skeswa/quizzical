package org.gauntlet.lessons.model.jpa;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"tmplt_lesson")
public class JPALesson extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -9161578622846589427L;
	
    @Basic
    @Column(name = "tmplt_lesson_code", length = 255 ,unique = true)    
    protected String code;

	@Basic
    @Column(name = "categoryId")
    protected Long categoryId;
    
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="lesson")
	private Set<JPALessonProblem> questions = new java.util.HashSet<JPALessonProblem>();
	
	public JPALesson() {
		setCode(String.format("Lesson %d", System.currentTimeMillis()));
	}
	
	public JPALesson(Long userId, Set<JPALessonProblem> questions) {
		this();
		this.questions = questions;
	}

	public Set<JPALessonProblem> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<JPALessonProblem> questions) {
		this.questions = questions;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
