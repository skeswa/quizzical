package org.gauntlet.lessons.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class LessonType extends BaseEntity implements Serializable {
	public LessonType() {
	}
	
	public LessonType(String code, String name) {
		setName(name);
		setCode(code);
	}
}
