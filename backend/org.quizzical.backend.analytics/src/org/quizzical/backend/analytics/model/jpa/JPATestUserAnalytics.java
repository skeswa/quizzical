package org.quizzical.backend.analytics.model.jpa;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.analytics.api.model.Constants.Q7L_MODULE
	+Constants.GNT_TABLE_NAME_SEPARATOR
	+"testuser")
public class JPATestUserAnalytics extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 1646753821796505776L;


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="analytics")
	private Set<JPATestCategoryRating> ratings = new java.util.HashSet<JPATestCategoryRating>();
	
	public JPATestUserAnalytics() {
	}
}
