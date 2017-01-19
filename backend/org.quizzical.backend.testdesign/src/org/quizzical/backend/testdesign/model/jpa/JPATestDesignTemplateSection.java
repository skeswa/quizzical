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
	private static final long serialVersionUID = 4518315461849076887L;

	private Integer ordinal;
	
	private TestDesignTemplateSectionType type;
	
	@ManyToOne
	private JPATestDesignTemplate template;
	
	@OneToMany(targetEntity = JPATestDesignTemplateItem.class, cascade=CascadeType.ALL, mappedBy="section")
	private List<JPATestDesignTemplateItem>  items;

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public TestDesignTemplateSectionType getType() {
		return type;
	}

	public void setType(TestDesignTemplateSectionType type) {
		this.type = type;
	}

	public JPATestDesignTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JPATestDesignTemplate template) {
		this.template = template;
	}

	public List<JPATestDesignTemplateItem> getItems() {
		return items;
	}

	public void setItems(List<JPATestDesignTemplateItem> items) {
		this.items = items;
	}
	
	public JPATestDesignTemplateSection() {
	}
}

