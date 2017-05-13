package org.gauntlet.lessons.api.model;

import java.io.Serializable;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;

public class UserLessonPlan extends BaseEntity implements Serializable {
	private Long userId;

	private List<UserLesson> lessons;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public UserLessonPlan() {
	}
	
	public UserLessonPlan(String name, String code, Long userId) {
		this.name = name;
		this.code = code;
		this.userId = userId;
	}

	
	public UserLessonPlan(String name, String code, Long userId, List<UserLesson> lessons) {
		this.name = name;
		this.code = code;
		this.userId = userId;
		this.lessons = lessons;
	}

	public List<UserLesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<UserLesson> lessons) {
		this.lessons = lessons;
	}
}
