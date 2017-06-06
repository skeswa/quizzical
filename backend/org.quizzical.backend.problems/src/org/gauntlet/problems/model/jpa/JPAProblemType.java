package org.gauntlet.problems.model.jpa;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"problem_ptpe")
public class JPAProblemType extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 7260383537297519563L;

	public JPAProblemType() {
		super();
	}

	public JPAProblemType(String name, String code) {
		this();
		this.name = name;
		this.code = code;
	}
}

