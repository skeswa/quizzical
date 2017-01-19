package org.quizzical.backend.testdesign.model.jpa;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
	@ManyToOne
	private JPATestDesignTemplateSection section;
	
	private Integer ordinal;
	
	private TestDesignTemplateItemDifficultyType difficultyType;
	
	private Long categoryId;
}

