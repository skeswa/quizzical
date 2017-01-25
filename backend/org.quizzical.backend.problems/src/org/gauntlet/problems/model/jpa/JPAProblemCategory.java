package org.gauntlet.problems.model.jpa;

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
	+"problem_category")
public class JPAProblemCategory extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 3359224600241497581L;

	public JPAProblemCategory() {
		super();
	}

	public JPAProblemCategory(String name, String code) {
		this();
		this.name = name;
		this.code = code;
	}	
}

