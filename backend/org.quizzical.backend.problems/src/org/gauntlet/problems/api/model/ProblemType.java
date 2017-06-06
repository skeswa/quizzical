package org.gauntlet.problems.api.model;

import java.io.Serializable;
import org.gauntlet.core.model.BaseEntity;

public class ProblemType extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 2945201920853145758L;

	public ProblemType() {
	}
	
	public ProblemType(String name, String code) {
		setName(name);
		setCode(code);
	}
}
