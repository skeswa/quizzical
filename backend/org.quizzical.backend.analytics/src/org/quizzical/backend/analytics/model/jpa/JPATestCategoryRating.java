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
	
	private Long lastAttemptTestId;
	
	private Integer lastAttemptRating;
	
	private Integer lastAttemptCorrect;
	
	private Integer lastAttemptTotal;
	
	private Integer totalAttemptsCorrect;
	
	private Integer totalAttemptsTotal;

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
	
	public Integer getLastAttemptRating() {
		return lastAttemptRating;
	}

	public void setLastAttemptRating(Integer lastAttemptRating) {
		this.lastAttemptRating = lastAttemptRating;
	}

	public JPATestUserAnalytics getAnalytics() {
		return analytics;
	}

	public void setAnalytics(JPATestUserAnalytics analytics) {
		this.analytics = analytics;
	}

	public List<JPATestCategoryAttempt> getAttempts() {
		if (attempts == null)
			attempts = new ArrayList<JPATestCategoryAttempt>();
		return attempts;
	}

	public void setAttempts(List<JPATestCategoryAttempt> attempts) {
		this.attempts = attempts;
	}

	public Long getLastAttemptTestId() {
		return lastAttemptTestId;
	}

	public void setLastAttemptTestId(Long lastAttemptTestId) {
		this.lastAttemptTestId = lastAttemptTestId;
	}

	public Integer getLastAttemptCorrect() {
		return lastAttemptCorrect;
	}

	public void setLastAttemptCorrect(Integer lastAttemptCorrect) {
		this.lastAttemptCorrect = lastAttemptCorrect;
	}

	public Integer getLastAttemptTotal() {
		return lastAttemptTotal;
	}

	public void setLastAttemptTotal(Integer lastAttemptTotal) {
		this.lastAttemptTotal = lastAttemptTotal;
	}

	public Integer getTotalAttemptsCorrect() {
		return totalAttemptsCorrect;
	}

	public void setTotalAttemptsCorrect(Integer totalAttemptsCorrect) {
		this.totalAttemptsCorrect = totalAttemptsCorrect;
	}

	public Integer getTotalAttemptsTotal() {
		return totalAttemptsTotal;
	}

	public void setTotalAttemptsTotal(Integer totalAttemptsTotal) {
		this.totalAttemptsTotal = totalAttemptsTotal;
	}
}

