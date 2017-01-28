package org.gauntlet.problems.model.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"problem_category_lessonitem")
public class JPAProblemCategoryLesson extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 8538105534304495532L;
	
	private Long contentItemId;
	
	@ManyToOne(targetEntity = JPAProblemCategory.class)
	@JoinColumn
	private JPAProblemCategory category;
	

	public JPAProblemCategoryLesson() {
	}

	public JPAProblemCategoryLesson(JPAProblemCategory category, Long contentItemId) {
		this();
		this.code = String.format("%s-Lesson-%d", category.getCode(), contentItemId);
		this.contentItemId = contentItemId;
		this.category = category;
	}

	public Long getContentItemId() {
		return contentItemId;
	}

	public void setContentItemId(Long contentItemId) {
		this.contentItemId = contentItemId;
	}

	public JPAProblemCategory getCategory() {
		return category;
	}

	public void setCategory(JPAProblemCategory category) {
		this.category = category;
	}
}

