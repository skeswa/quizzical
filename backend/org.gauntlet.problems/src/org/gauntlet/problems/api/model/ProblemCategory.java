package org.gauntlet.problems.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class ProblemCategory extends BaseEntity implements Serializable {
	public ProblemCategory() {
	}
	
	public ProblemCategory(String name, String code) {
		setName(name);
		setCode(code);
	}
}
