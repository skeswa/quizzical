package org.quizzical.backend.testdesign.model.jpa;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.testdesign.api.model.Constants.Q7L_MODULE
	+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"template_tp")
public class JPATestDesignTemplateType extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 1658897443125951772L;

	public JPATestDesignTemplateType() {
		super();
	}

	public JPATestDesignTemplateType(String name, String code) {
		this();
		this.name = name;
		this.code = code;
	}	
}

