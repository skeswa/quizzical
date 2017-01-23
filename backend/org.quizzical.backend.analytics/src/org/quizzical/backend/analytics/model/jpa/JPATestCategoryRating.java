package org.quizzical.backend.analytics.model.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
	+"cat_rating")
public class JPATestCategoryRating extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 601068620188199699L;
	
	private Integer rating = 0; //0-100
	
	private Long categoryId;
	
	private Date dateOfLastAttempt;

	@ManyToOne
	private JPATestUserAnalytics analytics;
	
	@OneToMany(targetEntity = JPATestCategoryAttempt.class, cascade=CascadeType.ALL, mappedBy="rating")
	private List<JPATestCategoryAttempt>  attempts;
	
	public JPATestCategoryRating() {
	}
}

