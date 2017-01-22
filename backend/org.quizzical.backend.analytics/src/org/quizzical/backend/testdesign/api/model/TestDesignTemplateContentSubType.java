package org.quizzical.backend.testdesign.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class TestDesignTemplateContentSubType extends BaseEntity implements Serializable {
	public TestDesignTemplateContentSubType() {
	}
	
	public TestDesignTemplateContentSubType(final String name, final String code) {
		setName(name);
		setCode(code);
	}
	
	public TestDesignTemplateContentSubType(final TestDesignTemplateContentType contentType, final String name, final String code) {
		final String code_ = String.format("%s/%s",contentType.getCode(),code);
		setName(code_);
		setCode(code_);
	}
}
