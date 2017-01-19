package org.quizzical.backend.testdesign.model.jpa;

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
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.testdesign.api.model.Constants.Q7L_MODULE
	+Constants.GNT_TABLE_NAME_SEPARATOR
	+"content_subtp")
public class JPATestDesignTemplateContentSubType extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 4256511505548372125L;
	
	@ManyToOne
	@JoinColumn
	private JPATestDesignTemplateContentType type;

	public JPATestDesignTemplateContentSubType() {
		super();
	}

	public JPATestDesignTemplateContentSubType(String name, String code) {
		this();
		this.name = name;
		this.code = code;
	}	
}

