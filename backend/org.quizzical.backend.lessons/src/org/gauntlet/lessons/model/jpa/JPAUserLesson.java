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
import org.gauntlet.lessons.api.model.Lesson;
import org.gauntlet.quizzes.api.model.Quiz;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"user_lesson")
public class JPAUserLesson extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -5512318597731923379L;

	private Long userId;
	
	private Long quizId;
	
	@ManyToOne(targetEntity = JPALesson.class)
	@JoinColumn
	private JPALesson lesson;
	
	

	public JPAUserLesson() {
		super();
	}

	public JPAUserLesson(String name, String code, Long userId, Long quizId) {
		this();
		this.name = name;
		this.code = code;
		this.userId = userId;
		this.quizId = quizId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getQuizId() {
		return quizId;
	}

	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}

	public JPALesson getLesson() {
		return lesson;
	}

	public void setLesson(JPALesson lesson) {
		this.lesson = lesson;
	}
	
}

