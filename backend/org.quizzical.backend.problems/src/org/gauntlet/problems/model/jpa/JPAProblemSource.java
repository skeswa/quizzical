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
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+"problem_source")
public class JPAProblemSource extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 6172362388145761605L;

	public JPAProblemSource() {
		super();
	}

	public JPAProblemSource(String name, String code) {
		this();
		this.name = name;
		this.code = code;
	}	
}

