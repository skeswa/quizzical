package org.gauntlet.quizzes.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class QuizType extends BaseEntity implements Serializable {
	public QuizType() {
	}
	
	public QuizType(String code, String name) {
		setName(name);
		setCode(code);
	}
}
