package org.quizzical.backend.analytics.model.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.analytics.api.model.Constants.Q7L_MODULE
	+Constants.GNT_TABLE_NAME_SEPARATOR
	+"cat_attempt")
public class JPATestCategoryAttempt extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 1897304303469354401L;
	
	private Long testProblemId;
	private Date dateAttempted;
	private Boolean successful;
	private Boolean skipped;

	@ManyToOne
	@JoinColumn
	private JPATestCategoryRating rating;
	
	public JPATestCategoryAttempt() {
	}

	public Long getTestProblemId() {
		return testProblemId;
	}

	public void setTestProblemId(Long testProblemId) {
		this.testProblemId = testProblemId;
	}

	public Date getDateAttempted() {
		return dateAttempted;
	}

	public void setDateAttempted(Date dateAttempted) {
		this.dateAttempted = dateAttempted;
	}

	public Boolean getSuccessful() {
		return successful;
	}

	public void setSuccessful(Boolean successful) {
		this.successful = successful;
	}

	public Boolean getSkipped() {
		return skipped;
	}

	public void setSkipped(Boolean skipped) {
		this.skipped = skipped;
	}

	public JPATestCategoryRating getRating() {
		return rating;
	}

	public void setRating(JPATestCategoryRating rating) {
		this.rating = rating;
	}
}

