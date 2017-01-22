package org.quizzical.backend.testdesign.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class TestDesignTemplateType extends BaseEntity implements Serializable {
	public TestDesignTemplateType() {
	}
	
	public TestDesignTemplateType(final String name, final String code) {
		setName(name);
		setCode(code);
	}
}
