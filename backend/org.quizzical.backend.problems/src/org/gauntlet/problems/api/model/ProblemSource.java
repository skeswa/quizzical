package org.gauntlet.problems.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class ProblemSource extends BaseEntity implements Serializable {
	public ProblemSource() {
	}
	
	public ProblemSource(String name, String code) {
		setName(name);
		setCode(code);
	}
}
