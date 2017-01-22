package org.quizzical.backend.testdesign.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class TestDesignTemplateItem extends BaseEntity implements Serializable {
	
	private TestDesignTemplateSection section;
	
	private Integer ordinal;
	
	private TestDesignTemplateItemDifficultyType difficultyType;
	
	private TestDesignTemplateContentSubType contentSubType;
	
	public TestDesignTemplateItem() {
	}
	
	public TestDesignTemplateItem(final String name, final String code, final Integer ordinal, final TestDesignTemplateItemDifficultyType difficultyType) {
		setOrdinal(ordinal);
		setName(name);
		setCode(code);
	}	
	
	public TestDesignTemplateItem(final TestDesignTemplateContentSubType contentSubType, final TestDesignTemplateSection section, final TestDesignTemplateItemDifficultyType difficultyType, final Integer ordinal) {
		setOrdinal(ordinal);
		setDifficultyType(difficultyType);
		setContentSubType(contentSubType);
		final String code = String.format("%s-%s-%d",section.getCode(),contentSubType.getCode(),getOrdinal());
		setName(code);
		setCode(code);
		setSection(section);
	}

	public TestDesignTemplateSection getSection() {
		return section;
	}

	public void setSection(TestDesignTemplateSection section) {
		this.section = section;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public TestDesignTemplateItemDifficultyType getDifficultyType() {
		return difficultyType;
	}

	public void setDifficultyType(TestDesignTemplateItemDifficultyType difficultyType) {
		this.difficultyType = difficultyType;
	}

	public TestDesignTemplateContentSubType getContentSubType() {
		return contentSubType;
	}

	public void setContentSubType(TestDesignTemplateContentSubType contentSubType) {
		this.contentSubType = contentSubType;
	}
}
