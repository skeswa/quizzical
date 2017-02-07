package org.quizzical.backend.analytics.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;

public class TestCategoryRatingSubmission extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 8791850525572480143L;
	
	private Long submissionId;
	private Date dateAttempted;
	
	private Integer submissionScore = 0; //0-100
	private Integer correctCount = 0;
	private Integer skippedCount = 0;
	
	private List<TestCategoryAttempt> attempts;
	
	public TestCategoryRatingSubmission() {
	}
	
	public TestCategoryRatingSubmission(Date dateAttempted) {
		super();
		this.dateAttempted = dateAttempted;
	}

	public TestCategoryRatingSubmission(Long submissionId, Date dateAttempted) {
		super();
		this.submissionId = submissionId;
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

	public List<TestCategoryAttempt> getAttempts() {
		if (attempts == null)
			attempts = new ArrayList<>();
		return attempts;
	}

	public void setAttempts(List<TestCategoryAttempt> attempts) {
		this.attempts = attempts;
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
}
