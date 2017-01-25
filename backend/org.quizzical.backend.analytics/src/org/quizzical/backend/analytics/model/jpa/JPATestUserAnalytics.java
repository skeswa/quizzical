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
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.analytics.api.model.Constants.Q7L_MODULE
	+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"testuser")
public class JPATestUserAnalytics extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 1646753821796505776L;

	private Long userId;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="analytics")
	private Set<JPATestCategoryRating> ratings = new java.util.HashSet<JPATestCategoryRating>();
	
	public JPATestUserAnalytics() {
	}
	
	public JPATestUserAnalytics(final String code, final String name) {
		setCode(code);
		setName(name);
	}

	public Set<JPATestCategoryRating> getRatings() {
		return ratings;
	}

	public void setRatings(Set<JPATestCategoryRating> ratings) {
		this.ratings = ratings;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
