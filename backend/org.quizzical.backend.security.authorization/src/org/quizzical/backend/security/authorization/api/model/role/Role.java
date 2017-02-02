package org.quizzical.backend.security.authorization.api.model.role;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class Role extends BaseEntity implements Serializable {

	public Role() {
	}
	
	public Role(final String code, final String name) {
		super.code = code;
		super.name = name;
	}
}
