package org.gauntlet.lessons.api.model;

import java.io.Serializable;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;

public class Lesson extends BaseEntity implements Serializable {
	private Long categoryId;
	
	private Long contentItemId;

	private List<LessonProblem> questions;

	public List<LessonProblem> getQuestions() {
		return questions;
	}

	public void setQuestions(List<LessonProblem> questions) {
		this.questions = questions;
	}
	
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getContentItemId() {
		return contentItemId;
	}

	public void setContentItemId(Long contentItemId) {
		this.contentItemId = contentItemId;
	}

	public Lesson() {
	}
	
	public Lesson(String name, String code, Long categoryId, Long contentItemId, List<LessonProblem> questions) {
		this.name = name;
		this.code = code;
		this.categoryId = categoryId;
		this.contentItemId = contentItemId;
		this.questions = questions;
	}
}
