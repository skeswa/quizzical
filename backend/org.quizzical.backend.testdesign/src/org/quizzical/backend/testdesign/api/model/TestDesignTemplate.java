package org.quizzical.backend.testdesign.api.model;

import java.io.Serializable;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;
import org.quizzical.backend.security.api.model.user.User;

public class TestDesignTemplate extends BaseEntity implements Serializable {
	private TestDesignTemplateType templateType;

	private List<TestDesignTemplateSection> sections;
	
	public TestDesignTemplateType getTestDesignTemplateType() {
		return templateType;
	}

	public void setTestDesignTemplateType(TestDesignTemplateType templateType) {
		this.templateType = templateType;
	}

	public List<TestDesignTemplateSection> getQuestions() {
		return sections;
	}

	public void setQuestions(List<TestDesignTemplateSection> sections) {
		this.sections = sections;
	}
	

	public TestDesignTemplate() {
	}
	
	public TestDesignTemplate(final String name, final String code) {
		this.name = name;
		this.code = code;
	}	
	
	public TestDesignTemplate(final String name, final String code, final List<TestDesignTemplateSection> sections) {
		this(name, code);
		this.sections = sections;
	}

	public TestDesignTemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(TestDesignTemplateType templateType) {
		this.templateType = templateType;
	}

	public List<TestDesignTemplateSection> getSections() {
		return sections;
	}

	public void setSections(List<TestDesignTemplateSection> sections) {
		this.sections = sections;
	}
}
