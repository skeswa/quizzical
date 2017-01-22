package org.quizzical.backend.testdesign.api.model;

import java.io.Serializable;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;

public class TestDesignTemplateContentType extends BaseEntity implements Serializable {
	private List<TestDesignTemplateContentSubType>  subTypes;
	
	public TestDesignTemplateContentType() {
	}
	
	public TestDesignTemplateContentType(final String name, final String code) {
		setName(name);
		setCode(code);
	}

	public List<TestDesignTemplateContentSubType> getSubTypes() {
		return subTypes;
	}

	public void setSubTypes(List<TestDesignTemplateContentSubType> subTypes) {
		this.subTypes = subTypes;
	}
}
