package org.gauntlet.problems.api.model;

import java.io.Serializable;


import org.gauntlet.core.model.BaseEntity;

public class ProblemCategoryLesson extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -5384379184748154076L;
	
	private Long contentItemId;
	
	private ProblemCategory category;
	
	public ProblemCategoryLesson() {
	}
	
	public ProblemCategoryLesson(String name, String code) {
		setName(name);
		setCode(code);
	}

	public Long getContentItemId() {
		return contentItemId;
	}

	public void setContentItemId(Long contentItemId) {
		this.contentItemId = contentItemId;
	}

	public ProblemCategory getCategory() {
		return category;
	}

	public void setCategory(ProblemCategory category) {
		this.category = category;
	}
}
