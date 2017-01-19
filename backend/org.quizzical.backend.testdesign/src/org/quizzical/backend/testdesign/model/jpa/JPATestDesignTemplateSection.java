package org.quizzical.backend.testdesign.model.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSectionType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.testdesign.api.model.Constants.Q7L_MODULE
	+Constants.GNT_TABLE_NAME_SEPARATOR
	+"template_section")
public class JPATestDesignTemplateSection extends JPABaseEntity implements Serializable {
	private Integer ordinal;
	
	private TestDesignTemplateSectionType type;
	
	@ManyToOne
	private JPATestDesignTemplate template;
	
	@OneToMany(targetEntity = JPATestDesignTemplate.class, cascade=CascadeType.ALL, mappedBy="section")
	@JoinColumn
	private List<JPATestDesignTemplateItem>  items;
}

