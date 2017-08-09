package org.gauntlet.problems.api.model;

import java.io.Serializable;
import org.gauntlet.core.model.BaseEntity;

public class ProblemType extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 2945201920853145758L;
	
	private Boolean nonSAT;

	public ProblemType() {
	}
	
	public ProblemType(String name, String code) {
		setName(name);
		setCode(code);
	}

	public Boolean getNonSAT() {
		return nonSAT;
	}

	public void setNonSAT(Boolean nonSAT) {
		this.nonSAT = nonSAT;
	}
}
