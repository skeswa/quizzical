package org.gauntlet.lessons.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.gauntlet.core.model.BaseEntity;
import org.gauntlet.lessons.model.jpa.JPALessonStatus;
import org.gauntlet.lessons.model.jpa.JPALessonType;
import org.gauntlet.quizzes.api.model.Quiz;

public class UserLesson extends BaseEntity implements Serializable {
	private Long userId;
	
	private Long quizId;
	
	private Date dateStarted;
	
	private Date lastDateVisited;
	
	private Boolean lessonFinished;
	
	private LessonType lessonType;
	
	private LessonStatus lessonStatus;	

	private Lesson lesson;
	
	private Quiz quiz;

	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	
	public Date getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
	}

	public Date getLastDateVisited() {
		return lastDateVisited;
	}

	public void setLastDateVisited(Date lastDateVisited) {
		this.lastDateVisited = lastDateVisited;
	}

	public Boolean getLessonFinished() {
		return lessonFinished;
	}

	public void setLessonFinished(Boolean lessonFinished) {
		this.lessonFinished = lessonFinished;
	}

	public Long getQuizId() {
		return quizId;
	}

	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}
	
	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public LessonStatus getLessonStatus() {
		return lessonStatus;
	}

	public void setLessonStatus(LessonStatus lessonStatus) {
		this.lessonStatus = lessonStatus;
	}

	public UserLesson() {
	}
	
	public UserLesson(Long userId, Lesson lesson, Quiz quiz) {
		this.code = String.format("%s-%d", lesson.getCode(), userId);
		this.name = lesson.getName();
		this.quizId = quiz.getId();
		this.userId = userId;
		this.quiz = quiz;
		this.lesson = lesson;
	}
	
	
}
