package org.gauntlet.problems.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class ProblemDifficulty extends BaseEntity implements Serializable {
	
	private String color;

	public ProblemDifficulty() {
	}
	
	public ProblemDifficulty(String name, String code, String color) {
		setName(name);
		setCode(code);
		setColor(color);
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
