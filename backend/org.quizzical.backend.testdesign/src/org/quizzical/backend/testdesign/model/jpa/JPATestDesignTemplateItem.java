package org.quizzical.backend.testdesign.model.jpa;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItemDifficultyType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSection;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.testdesign.api.model.Constants.Q7L_MODULE
	+Constants.GNT_TABLE_NAME_SEPARATOR
	+"template_item")
public class JPATestDesignTemplateItem extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 7575623596832640276L;

	@ManyToOne
	@JoinColumn
	private JPATestDesignTemplateSection section;
	
	@ManyToOne
	@JoinColumn
	private JPATestDesignTemplateContentSubType contentSubType;
	
	private Integer ordinal;
	
	private TestDesignTemplateItemDifficultyType difficultyType;

	public JPATestDesignTemplateSection getSection() {
		return section;
	}

	public void setSection(JPATestDesignTemplateSection section) {
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

	public JPATestDesignTemplateContentSubType getContentSubType() {
		return contentSubType;
	}

	public void setContentSubType(JPATestDesignTemplateContentSubType contentSubType) {
		this.contentSubType = contentSubType;
	}

	public JPATestDesignTemplateItem() {
	}
	
	public JPATestDesignTemplateItem(JPATestDesignTemplateContentSubType contentSubType,
			Integer ordinal, TestDesignTemplateItemDifficultyType difficultyType) {
		super();
		this.contentSubType = contentSubType;
		this.ordinal = ordinal;
		this.difficultyType = difficultyType;
	}
}

