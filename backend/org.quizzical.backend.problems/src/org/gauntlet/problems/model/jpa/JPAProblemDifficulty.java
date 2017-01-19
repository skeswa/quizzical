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
	+"problem_difficulty")
public class JPAProblemDifficulty extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 8010881153800368246L;
	
	private String color;

	public JPAProblemDifficulty() {
		super();
	}

	public JPAProblemDifficulty(String name, String code, String color) {
		this();
		this.name = name;
		this.code = code;
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}

