package org.quizzical.backend.security.authorization.model.jpa.user;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
+"role")
public class JPARole extends org.gauntlet.core.model.JPABaseEntity{
	private static final long serialVersionUID = -187767022812534655L;
}
