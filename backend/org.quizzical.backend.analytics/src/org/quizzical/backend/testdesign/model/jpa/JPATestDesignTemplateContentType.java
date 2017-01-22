package org.quizzical.backend.testdesign.model.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.testdesign.api.model.Constants.Q7L_MODULE
	+Constants.GNT_TABLE_NAME_SEPARATOR
	+"content_tp")
public class JPATestDesignTemplateContentType extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 4735114052458117856L;
	
	@OneToMany(targetEntity = JPATestDesignTemplateContentSubType.class, fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="type")
	private List<JPATestDesignTemplateContentSubType>  subTypes;

	public JPATestDesignTemplateContentType() {
	}

	public JPATestDesignTemplateContentType(final List<JPATestDesignTemplateContentSubType>  subTypes, final String name, final String code) {
		this.name = name;
		this.code = code;
	}

	public List<JPATestDesignTemplateContentSubType> getSubTypes() {
		return subTypes;
	}

	public void setSubTypes(List<JPATestDesignTemplateContentSubType> subTypes) {
		this.subTypes = subTypes;
	}	
}

