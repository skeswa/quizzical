package org.quizzical.backend.analytics.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.math.fraction.Fraction;
import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.analytics.api.model.Constants.Q7L_MODULE
	+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"cat_rating_submit")
public class JPATestCategoryRatingSubmission extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -179339366080849758L;
	private Long submissionId;
	private Date dateAttempted;
	private Long testId;
	
	private Integer submissionScore = 0; //0-100
	private Integer correctCount = 0;
	private Integer skippedCount = 0;

	@ManyToOne
	private JPATestCategoryRating rating;
	
	@OneToMany(targetEntity = JPATestCategoryAttempt.class, cascade=CascadeType.ALL, mappedBy="ratingSubmission")
	private List<JPATestCategoryAttempt>  attempts = new ArrayList<JPATestCategoryAttempt>();
	
	public JPATestCategoryRatingSubmission() {
	}
	
	public JPATestCategoryRatingSubmission(Date dateAttempted) {
		this.dateAttempted = dateAttempted;
	}

	public Long getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}

	public Date getDateAttempted() {
		return dateAttempted;
	}

	public void setDateAttempted(Date dateAttempted) {
		this.dateAttempted = dateAttempted;
	}

	public List<JPATestCategoryAttempt> getAttempts() {
		return attempts;
	}

	public void setAttempts(List<JPATestCategoryAttempt> attempts) {
		this.attempts = attempts;
	}

	public JPATestCategoryRating getRating() {
		return rating;
	}

	public void setRating(JPATestCategoryRating rating) {
		this.rating = rating;
	}
	
	public Integer getSubmissionScore() {
		return submissionScore;
	}

	public void setSubmissionScore(Integer submissionScore) {
		this.submissionScore = submissionScore;
	}

	public Integer getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(Integer correctCount) {
		this.correctCount = correctCount;
	}

	public Integer getSkippedCount() {
		return skippedCount;
	}

	public void setSkippedCount(Integer skippedCount) {
		this.skippedCount = skippedCount;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public Integer calculateScore() {
		final JPATestCategoryAttempt  recentAttempt = getAttempts()
			    .stream()
			    .findFirst().get();
		setTestId(recentAttempt.getTestId());
		
		final List<JPATestCategoryAttempt> correctAttempts = getAttempts()
			    .stream()
			    .filter(p -> p.getSuccessful())
			    .collect(Collectors.toList());
		int val = (int)(new Fraction(correctAttempts.size(),getAttempts().size()).doubleValue()*100);
		final List<JPATestCategoryAttempt> skippedAttempts = getAttempts()
			    .stream()
			    .filter(p -> p.getSkipped())
			    .collect(Collectors.toList());
		setCorrectCount(correctAttempts.size());
		setSkippedCount(skippedAttempts.size());
		setSubmissionScore(val);

		return val;
	}
}

