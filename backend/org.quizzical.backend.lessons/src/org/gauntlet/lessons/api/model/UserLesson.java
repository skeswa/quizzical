package org.gauntlet.lessons.api.model;

import java.io.Serializable;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;
import org.gauntlet.quizzes.api.model.Quiz;
import org.quizzical.backend.security.authorization.api.model.user.User;

public class UserLesson extends BaseEntity implements Serializable {
	private Long userId;

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

	public UserLesson() {
	}
	
	public UserLesson(String name, String code, Lesson lesson, Quiz quiz) {
		this.name = name;
		this.code = code;
		this.quiz = quiz;
		this.lesson = lesson;
	}
}
