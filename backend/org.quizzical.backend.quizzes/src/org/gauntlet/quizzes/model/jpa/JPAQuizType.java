package org.gauntlet.quizzes.model.jpa;

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
	+"quiz_type")
public class JPAQuizType extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 8493281968666965055L;

	public JPAQuizType() {
		super();
	}

	public JPAQuizType(String name, String code) {
		this();
		this.name = name;
		this.code = code;
	}	
}

