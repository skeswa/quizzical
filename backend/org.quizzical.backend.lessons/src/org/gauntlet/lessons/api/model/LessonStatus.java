package org.gauntlet.lessons.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class LessonStatus extends BaseEntity implements Serializable {
	public LessonStatus() {
	}
	
	public LessonStatus(String code, String name) {
		setName(name);
		setCode(code);
	}
}
