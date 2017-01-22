package org.quizzical.backend.testdesign.model.jpa;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.testdesign.api.model.Constants.Q7L_MODULE
	+Constants.GNT_TABLE_NAME_SEPARATOR
	+"template")
public class JPATestDesignTemplate extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 4048638638887859408L;

	@ManyToOne
	@JoinColumn
	private JPATestDesignTemplateType templateType;	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="template")
	private Set<JPATestDesignTemplateSection> sections = new java.util.HashSet<JPATestDesignTemplateSection>();

	public JPATestDesignTemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(JPATestDesignTemplateType templateType) {
		this.templateType = templateType;
	}

	public Set<JPATestDesignTemplateSection> getSections() {
		return sections;
	}

	public void setSections(Set<JPATestDesignTemplateSection> sections) {
		this.sections = sections;
	}
}
