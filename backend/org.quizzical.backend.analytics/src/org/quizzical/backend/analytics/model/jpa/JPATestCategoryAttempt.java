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
import org.quizzical.backend.analytics.api.model.TestCategoryAttempt;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.analytics.api.model.Constants.Q7L_MODULE
	+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"cat_attempt")
public class JPATestCategoryAttempt extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -6972020737204651800L;
	private Long testId;
	private Long testProblemId;
	private Date dateAttempted;
	private Boolean successful;
	private Boolean skipped;

	@ManyToOne
	@JoinColumn
	private JPATestCategoryRatingSubmission ratingSubmission;
	
	public JPATestCategoryAttempt(){
	}
	
	public JPATestCategoryAttempt(TestCategoryAttempt attempt) {
		setTestId(attempt.getTestId());
		setTestProblemId(attempt.getTestProblemId());
		setDateAttempted(attempt.getDateAttempted());
		setSuccessful(attempt.getSuccessful());
		setSkipped(attempt.getSkipped());
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

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public JPATestCategoryRatingSubmission getRatingSubmission() {
		return ratingSubmission;
	}

	public void setRatingSubmission(JPATestCategoryRatingSubmission ratingSubmission) {
		this.ratingSubmission = ratingSubmission;
	}
}

