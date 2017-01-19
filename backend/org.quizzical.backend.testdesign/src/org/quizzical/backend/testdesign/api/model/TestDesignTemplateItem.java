package org.quizzical.backend.testdesign.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

public class TestDesignTemplateItem extends BaseEntity implements Serializable {
	
	private TestDesignTemplateSection section;
	
	private Integer ordinal;
	
	private TestDesignTemplateItemDifficultyType difficultyType;
	
	private Long categoryId;
	
	public TestDesignTemplateItem() {
	}
	
	public TestDesignTemplateItem(final Long categoryId, final TestDesignTemplateSection section, final TestDesignTemplateItemDifficultyType difficultyType, final Integer ordinal) {
		setOrdinal(ordinal);
		setDifficultyType(difficultyType);
		setCategoryId(categoryId);
		final String code = String.format("%s-%d",section.getCode(),getOrdinal());
		setName(code);
		setCode(code);
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
