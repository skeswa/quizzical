package org.quizzical.backend.analytics.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.analytics.api.model.Constants.Q7L_MODULE
	+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"cat_rating")
public class JPATestCategoryRating extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 601068620188199699L;
	
	private Integer rating = 0; //0-100
	
	private Long categoryId;
	
	private Date dateOfLastAttempt;

	@ManyToOne
	private JPATestUserAnalytics analytics;
	
	@OneToMany(targetEntity = JPATestCategoryAttempt.class, cascade=CascadeType.ALL, mappedBy="rating")
	private List<JPATestCategoryAttempt>  attempts = new ArrayList<JPATestCategoryAttempt>();
	
	public JPATestCategoryRating() {
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Date getDateOfLastAttempt() {
		return dateOfLastAttempt;
	}

	public void setDateOfLastAttempt(Date dateOfLastAttempt) {
		this.dateOfLastAttempt = dateOfLastAttempt;
	}

	public JPATestUserAnalytics getAnalytics() {
		return analytics;
	}

	public void setAnalytics(JPATestUserAnalytics analytics) {
		this.analytics = analytics;
	}

	public List<JPATestCategoryAttempt> getAttempts() {
		return attempts;
	}

	public void setAttempts(List<JPATestCategoryAttempt> attempts) {
		this.attempts = attempts;
	}
}

